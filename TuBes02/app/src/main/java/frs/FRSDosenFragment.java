package frs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FrsDosenFragmentBinding;

public class FRSDosenFragment extends Fragment {

    FrsDosenFragmentBinding binding;

    private FRSDosenFragment(){}

    public static FRSDosenFragment newInstance(String title){
        FRSDosenFragment fragment = new FRSDosenFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FrsDosenFragmentBinding.inflate(inflater);

        return binding.getRoot();
    }
}
