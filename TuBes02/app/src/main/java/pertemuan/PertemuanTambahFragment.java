package pertemuan;

import android.app.DatePickerDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubes_02.databinding.FragmentPertemuanTambahBinding;
import com.example.tubes_02.databinding.SpinnerPartisipanBinding;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class PertemuanTambahFragment extends Fragment {
    private FragmentPertemuanTambahBinding binding;
    private PertemuanPresenter presenter;
    private ViewGroup llPartisipan;
    private ArrayList<Spinner> partisipanSpinners;
    private FragmentManager fm;
    private Calendar now;

    public PertemuanTambahFragment() {
    }

    public static PertemuanTambahFragment newInstance(PertemuanPresenter presenter) {
        PertemuanTambahFragment fragment = new PertemuanTambahFragment();
        fragment.presenter = presenter;
        presenter.getUsersFromServer();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inisialisasi atribut
        this.binding = FragmentPertemuanTambahBinding.inflate(inflater, container, false);
        this.fm = getParentFragmentManager();
        this.llPartisipan = this.binding.llFieldPartisipan;
        this.partisipanSpinners = new ArrayList<>();
        this.now = Calendar.getInstance();

        // Listener
        this.binding.ivTambahPartisipan.setOnClickListener((View v) -> {
            this.presenter.showTambahPartisipanDialog();
        });
        this.binding.etJamMulai.setOnClickListener(this::onTimeMulaiClick);
        this.binding.etJamSelesai.setOnClickListener(this::onTimeSelesaiClick);
        this.binding.etTanggalMulai.setOnClickListener(this::onDateClick);
        this.binding.btnSimpan.setOnClickListener(this::onSimpanClick);
        this.binding.btnSchedule.setOnClickListener(this::onScheduleClick);


        return this.binding.getRoot();
    }

    private void onScheduleClick(View view) {
        Bundle result = new Bundle();
        result.putString("page", "jadwal");
        this.fm.setFragmentResult("changePage", result);
    }

    private void onDelPartisipanClick(View view) {
        View spinnerPartisipan = (View) view.getParent();
        this.partisipanSpinners.remove(spinnerPartisipan.getTag());
        this.llPartisipan.removeView(spinnerPartisipan);

    }

    private void onDateClick(View view) {
        int currYear = now.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH);
        int currDay = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this.getContext(), this::onDateSet, currYear,
                currMonth, currDay);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String text = IFPortalDateTimeFormatter.formatDate(year, month + 1, dayOfMonth);
        this.binding.etTanggalMulai.setText(text);
    }

    private void onTimeSelesaiClick(View view) {
        if (this.binding.etJamMulai.getText().length() > 0) {
            String startTimeStr = this.binding.etJamMulai.getText().toString();
            TimePickerDialog dialog = TimePickerDialog.newInstance(this::onTimeSelesaiSet,
                    this.now.get(Calendar.HOUR_OF_DAY), this.now.get(Calendar.MINUTE), true);
            dialog.setMinTime(IFPortalDateTimeFormatter.getHourFromTime(startTimeStr),
                    IFPortalDateTimeFormatter.getMinuteFromTime(startTimeStr), 0);
            dialog.show(this.fm.beginTransaction(), null);
        } else {
            Toast.makeText(getActivity(), "Harap isi Waktu Mulai terlebih dahulu.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void onTimeSelesaiSet(TimePickerDialog timePickerDialog, int hour, int minute,
                                  int second) {
        String text = IFPortalDateTimeFormatter.formatTime(hour, minute);
        this.binding.etJamSelesai.setText(text);
    }

    private void onTimeMulaiClick(View view) {
        String dateStr = this.binding.etTanggalMulai.getText().toString();
        if (dateStr.length() > 0) {
            TimePickerDialog dialog = TimePickerDialog.newInstance(this::onTimeMulaiSet,
                    this.now.get(Calendar.HOUR_OF_DAY), this.now.get(Calendar.MINUTE), true);
            if (IFPortalDateTimeFormatter.getDayFromDate(dateStr) == now.get(Calendar.DAY_OF_MONTH)) {
                dialog.setMinTime(this.now.get(Calendar.HOUR_OF_DAY),
                        this.now.get(Calendar.MINUTE),
                        0);
            }
            dialog.show(this.fm.beginTransaction(), null);
        } else {
            Toast.makeText(getActivity(), "Harap isi Tanggal Pertemuan terlebih dahulu.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void onTimeMulaiSet(TimePickerDialog timePickerDialog, int hour, int minute,
                                int second) {
        String text = IFPortalDateTimeFormatter.formatTime(hour, minute);
        this.binding.etJamMulai.setText(text);
        String endTimeStr = this.binding.etJamSelesai.getText().toString();
        if (endTimeStr.length() > 0) {
            Log.d("Debug", "" + (IFPortalDateTimeFormatter.getHourFromTime(endTimeStr) < hour));
            if ((IFPortalDateTimeFormatter.getHourFromTime(endTimeStr) == hour &&
                    IFPortalDateTimeFormatter.getMinuteFromTime(endTimeStr) < minute) ||
                    IFPortalDateTimeFormatter.getHourFromTime(endTimeStr) < hour) {
                this.binding.etJamSelesai.setText("");
                Toast.makeText(getActivity(), "Waktu Selesai lebih awal dari Waktu Mulai. " +
                                "Silahkan isi kembali Waktu Selesai.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addSpinnerPartisipan(String role) {
        LayoutInflater inflater = this.getLayoutInflater();
        SpinnerPartisipanBinding viewPartisipanBinding = SpinnerPartisipanBinding.inflate(inflater);
        View viewPartisipan = viewPartisipanBinding.getRoot();
        viewPartisipan.setTag(this.partisipanSpinners.size());

        // Buat option
        TextView tvRole = (TextView) ((ViewGroup) viewPartisipan).getChildAt(0);
        Spinner spinnerPartisipan = (Spinner) ((ViewGroup) viewPartisipan).getChildAt(2);
        ArrayAdapter<OtherUser> adapter = null;
        Log.d("Debug", role);
        switch (role) {
            case OtherUser.ROLE_STUDENT:
                tvRole.setText("Mahasiswa");
                adapter = new ArrayAdapter<>(spinnerPartisipan.getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        this.presenter.getStudentsList());
                spinnerPartisipan.setTag(OtherUser.ROLE_STUDENT);
                break;
            case OtherUser.ROLE_LECTURER:
                tvRole.setText("Dosen");
                adapter = new ArrayAdapter<>(spinnerPartisipan.getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        this.presenter.getLecturerList());
                spinnerPartisipan.setTag(OtherUser.ROLE_LECTURER);
                break;
        }
        if (adapter != null) {
            spinnerPartisipan.setAdapter(adapter);
            this.partisipanSpinners.add(spinnerPartisipan);
        }

        // Listener delete
        ((ViewGroup) viewPartisipan).getChildAt(3)
                .setOnClickListener(this::onDelPartisipanClick);

        // Tambahkan ke UI
        this.llPartisipan.addView(viewPartisipan);
    }

    private void onSimpanClick(View v) {
        String judul = this.binding.etJudul.getText().toString();
        String tanggal = this.binding.etTanggalMulai.getText().toString();
        String jamMulai = this.binding.etJamMulai.getText().toString();
        String jamSelesai = this.binding.etJamSelesai.getText().toString();
        String deskripsi = this.binding.etDesk.getText().toString();
        if (judul.length() > 0 && tanggal.length() > 0 && jamMulai.length() > 0 &&
                jamSelesai.length() > 0) {
            ArrayList<String> idUndangan = new ArrayList<>();
            for (Spinner spinner : partisipanSpinners) {
                int pos = spinner.getSelectedItemPosition();
                String role = (String) spinner.getTag();
                if (pos > AdapterView.INVALID_POSITION && role != null) {
                    idUndangan.add(this.presenter.getUserId(role, pos));
                }
            }
            this.presenter.createAppointment(judul, tanggal, jamMulai, jamSelesai, deskripsi, idUndangan);
        } else {
            Toast.makeText(getActivity(), "Field selain deskripsi dan partisipan wajib diisi.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void resetForm() {
        this.binding.etJudul.setText("");
        this.binding.etTanggalMulai.setText("");
        this.binding.etJamMulai.setText("");
        this.binding.etJamSelesai.setText("");
        this.binding.etDesk.setText("");
        this.llPartisipan.removeAllViews();
    }

    public void addSpinner(String role) {
        this.addSpinnerPartisipan(role);
    }
}
