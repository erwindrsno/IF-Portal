package com.example.tubes_02;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentPrasyaratBinding;

import java.io.File;

public class PrasyaratFragment extends Fragment {
    private FragmentPrasyaratBinding binding;

    public PrasyaratFragment(){

    }

    public static PrasyaratFragment newInstance() {
        PrasyaratFragment fragment = new PrasyaratFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrasyaratBinding.inflate(inflater);
        View view = binding.getRoot();

        return view;
    }

}
