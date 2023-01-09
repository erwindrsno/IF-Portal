package FiturAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentBuatPengumumanBinding;
import com.example.tubes_02.databinding.FragmentHomeAdminBinding;


public class HomeAdminFragment extends Fragment {
    private FragmentHomeAdminBinding binding;
    public HomeAdminFragment(){

    }

    public static HomeAdminFragment newInstance() {
        HomeAdminFragment fragment = new HomeAdminFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeAdminBinding.inflate(inflater);
        View view = binding.getRoot();
        this.binding.btnAddUser.setOnClickListener(this::onclick);
        return view;
    }

    private void onclick(View view) {

    }
}
