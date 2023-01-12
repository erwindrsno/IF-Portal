package pertemuan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubes_02.databinding.FragmentPertemuanTimeslotAddBinding;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public class PertemuanTimeSlotAddFragment extends Fragment {
    private PertemuanPresenter presenter;
    private FragmentPertemuanTimeslotAddBinding binding;
    private FragmentManager fm;

    public PertemuanTimeSlotAddFragment() {
    }

    public static PertemuanTimeSlotAddFragment newInstance(PertemuanPresenter presenter) {
        PertemuanTimeSlotAddFragment fragment = new PertemuanTimeSlotAddFragment();
        fragment.presenter = presenter;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = FragmentPertemuanTimeslotAddBinding.inflate(inflater, container, false);
        this.fm = getParentFragmentManager();

        this.binding.tvName.setText(this.presenter.getLoggedInUserName());
        Spinner spinnerDay = this.binding.spinnerDay;
        spinnerDay.setAdapter(new ArrayAdapter<String>(spinnerDay.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                IFPortalDateTimeFormatter.DAYS_IDN));
        this.binding.etJamMulai.setOnClickListener(this::onTimeMulaiClick);
        this.binding.etJamSelesai.setOnClickListener(this::onTimeSelesaiClick);
        this.binding.btnSimpan.setOnClickListener(this::onSaveClick);
        return binding.getRoot();
    }

    private void onSaveClick(View view) {
        String startTime = this.binding.etJamMulai.getText().toString();
        String endTime = this.binding.etJamSelesai.getText().toString();
        int dayIdx = this.binding.spinnerDay.getSelectedItemPosition();
        if (startTime.length() > 0 && endTime.length() > 0) {
            this.presenter.addTimeSlot(dayIdx, startTime, endTime);
        } else {
            Toast.makeText(getActivity(), "Semua field wajib diisi.",
                    Toast.LENGTH_LONG).show();
        }
    }


    private void onTimeSelesaiClick(View view) {
        if (this.binding.etJamMulai.getText().length() > 0) {
            String startTimeStr = this.binding.etJamMulai.getText().toString();
            TimePickerDialog dialog = TimePickerDialog.newInstance(this::onTimeSelesaiSet,
                    IFPortalDateTimeFormatter.getHourFromTime(startTimeStr),
                    IFPortalDateTimeFormatter.getMinuteFromTime(startTimeStr),
                    true);
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
        TimePickerDialog dialog = TimePickerDialog.newInstance(this::onTimeMulaiSet,
                0, 0, true);
        dialog.show(this.fm.beginTransaction(), null);
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
}
