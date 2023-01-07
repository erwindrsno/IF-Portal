package drawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentSignOutDialogBinding;

public class SignOutDialogFragment extends DialogFragment {
    private FragmentSignOutDialogBinding binding;
//    private View view;

    public SignOutDialogFragment(){}

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
        View view = inflater.inflate(R.layout.fragment_sign_out_dialog, null);

        builder.setView(view)
                .setTitle("Sign Out")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle result = new Bundle();
                        result.putString("activity","login");
                        getParentFragmentManager().setFragmentResult("changeActivity",result);
                    }
                });

        return builder.create();
    }

    public static SignOutDialogFragment newInstance(String title){
        SignOutDialogFragment fragment = new SignOutDialogFragment();
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