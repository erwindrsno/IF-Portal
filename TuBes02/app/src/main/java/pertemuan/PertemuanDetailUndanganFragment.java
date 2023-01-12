package pertemuan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tubes_02.databinding.BtnPertemuanDetailBinding;
import com.example.tubes_02.databinding.FragmentDetailPertemuanBinding;


public class PertemuanDetailUndanganFragment extends PertemuanDetailFragment {
    String id;

    public PertemuanDetailUndanganFragment() {
    }

    public static PertemuanDetailUndanganFragment newInstance(PertemuanPresenter presenter, String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        PertemuanDetailUndanganFragment fragment = new PertemuanDetailUndanganFragment();
        fragment.setArguments(args);
        fragment.presenter = presenter;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.id = this.getArguments().getString("id");
        this.binding = FragmentDetailPertemuanBinding.inflate(inflater, container, false);
        this.presenter.getPertemuanDetailsFromServer(id);
        LinearLayout llRoot = this.binding.getRoot();

        // Mengatur weight dari detail pertemuan
        llRoot.getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 9));

        // Mengatur weight dari button terima, lalu tambahkan ke root
        BtnPertemuanDetailBinding btnBinding = BtnPertemuanDetailBinding.inflate(inflater);
        View llBtn = btnBinding.getRoot();
        llBtn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        llRoot.addView(llBtn);

        // Remove view untuk partisipan karena tidak terpakai.
        this.binding.llFormData.removeView(this.binding.llPartispan);

        // Listener
        btnBinding.btnTerima.setOnClickListener(this::onTerimaClick);

        return llRoot;
    }

    private void onTerimaClick(View view) {
        new AlertDialog.Builder(presenter.getUIContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menerima undangan pertemuan ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PertemuanDetailUndanganFragment.this.presenter
                                .postAcceptInvitation(PertemuanDetailUndanganFragment.this.id);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void updateInvitees(String invitees, String attendees) {
    }
}
