package com.example.tubes_02;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tubes_02.databinding.FragmentExitAppBinding;

public class ExitAppDialogFragment extends DialogFragment {
    private FragmentExitAppBinding binding;

    public ExitAppDialogFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.binding = FragmentExitAppBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    public static ExitAppDialogFragment newInstance(String title){
        ExitAppDialogFragment fragment = new ExitAppDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }
}