package pertemuan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubes_02.databinding.FragmentPertemuanTambahRoleDialogBinding;

public class PertemuanTambahPartisipanDialogFragment extends DialogFragment {
    private PertemuanPresenter presenter;
    private FragmentPertemuanTambahRoleDialogBinding binding;

    public PertemuanTambahPartisipanDialogFragment(){}

    public static PertemuanTambahPartisipanDialogFragment newInstance(PertemuanPresenter presenter) {
        PertemuanTambahPartisipanDialogFragment fragment = new PertemuanTambahPartisipanDialogFragment();
        fragment.presenter = presenter;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentPertemuanTambahRoleDialogBinding
                .inflate(inflater, container, false);

        binding.btnTambah.setOnClickListener(this::onTambahClick);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void onTambahClick(View view) {
        if (this.binding.rbLecturer.isChecked()){
            this.presenter.hlmTambahAddSpinner(OtherUser.ROLE_LECTURER);
        } else {
            this.presenter.hlmTambahAddSpinner(OtherUser.ROLE_STUDENT);
        }
        this.dismiss();
    }
}
