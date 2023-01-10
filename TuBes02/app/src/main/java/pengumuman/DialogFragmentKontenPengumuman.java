package pengumuman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.tubes_02.databinding.FragmentDialogPengumumanBinding;

import drawer.SignOutDialogFragment;

public class DialogFragmentKontenPengumuman extends DialogFragment {
    private String title;
    private String konten;
    private FragmentDialogPengumumanBinding binding;

    public static DialogFragmentKontenPengumuman newInstance(String title){
        DialogFragmentKontenPengumuman fragment = new DialogFragmentKontenPengumuman();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        this.binding.tvDialogPengumumanTitle.setText(this.title);
        this.binding.tvDialogPengumumanKonten.setText(this.konten);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.binding = FragmentDialogPengumumanBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String konten){
        this.konten = konten;
    }
}
