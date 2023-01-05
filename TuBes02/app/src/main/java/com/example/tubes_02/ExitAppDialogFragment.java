package com.example.tubes_02;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.tubes_02.databinding.FragmentExitAppBinding;

public class ExitAppDialogFragment extends DialogFragment {
    private FragmentExitAppBinding binding;
//    private View view;

    public ExitAppDialogFragment(){}

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        this.binding = FragmentExitAppBinding.inflate(inflater,container,false);
//        View view = binding.getRoot();
//        return view;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d("oncreateDialog","masuk");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_exit_app, null);

        builder.setView(view)
                .setTitle("Exit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("onclick","exit");
                        Bundle result = new Bundle();
                        result.putString("toExit","yes");
                        getParentFragmentManager().setFragmentResult("exitApp",result);
                    }
                });

        return builder.create();
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