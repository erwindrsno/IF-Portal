package frs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FrsMainFragmentBinding;

import java.util.ArrayList;

public class FRSMainFragment extends Fragment implements UIFRSemester{
    private FrsMainFragmentBinding binding;
    private FRSPresenter presenter;
    private AdapterSemester adapterSemester;

    private FRSMainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FrsMainFragmentBinding.inflate(inflater);

        this.adapterSemester = new AdapterSemester(presenter, this);
        this.binding.listSemester.setAdapter(adapterSemester);

        //load data semester disini
        presenter.getSemestertoAdapter();

        return binding.getRoot();
    }

    public static FRSMainFragment newInstance(String title, FRSPresenter presenter){
        FRSMainFragment fragment = new FRSMainFragment();
        fragment.presenter = presenter;
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateListSemester(ArrayList<Integer> semester) {
        this.adapterSemester.update(semester);
    }
}
