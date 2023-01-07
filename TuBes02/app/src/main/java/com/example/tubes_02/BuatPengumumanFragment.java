package com.example.tubes_02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentBuatPengumumanBinding;

public class BuatPengumumanFragment extends Fragment {
    private FragmentBuatPengumumanBinding binding;

    public BuatPengumumanFragment(){

    }

    public static BuatPengumumanFragment newInstance() {
        BuatPengumumanFragment fragment = new BuatPengumumanFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBuatPengumumanBinding.inflate(inflater);
        View view = binding.getRoot();

        return view;
    }
}
