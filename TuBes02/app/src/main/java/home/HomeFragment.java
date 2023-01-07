package home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private FragmentHomeBinding binding;

    public HomeFragment(){
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentHomeBinding.inflate(inflater);
        this.binding.btnCoba.setOnClickListener(this);
        return this.binding.getRoot();
    }

    public static HomeFragment newInstance(String title){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnCoba.getId()){
            Bundle result = new Bundle();
            result.putString("page","exit");
            this.getParentFragmentManager().setFragmentResult("changePage",result);
        }
    }
}
