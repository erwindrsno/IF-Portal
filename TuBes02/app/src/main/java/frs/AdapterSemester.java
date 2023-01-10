package frs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.FrsItemListSemesterBinding;

import java.util.ArrayList;

public class AdapterSemester extends BaseAdapter {

    ArrayList<Integer> semester;
    FRSPresenter presenter;
    FRSMainFragment fragment;
    FrsItemListSemesterBinding binding;

    public AdapterSemester(FRSPresenter presenter, FRSMainFragment fragment){
        super();
        this.semester = new ArrayList<>();
        this.presenter = presenter;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return semester.size();
    }

    @Override
    public Object getItem(int i) {
        return semester.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        binding = FrsItemListSemesterBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        if(view==null){
            view = binding.getRoot();
            vh = new ViewHolder(view, binding, i, presenter, fragment);
            view.setTag(vh);
        }
        else{
            vh = (ViewHolder) view.getTag();
        }
        vh.updateView(this.semester.get(i));
        return view;
    }

    public void update(ArrayList<Integer> semester){
        this.semester = semester;
        notifyDataSetChanged();
    }

    private class ViewHolder{

        FrsItemListSemesterBinding binding;
        private FRSPresenter presenter;
        private int position;
        private int academic_year;
        private FRSMainFragment fragment;

        public ViewHolder(View view, FrsItemListSemesterBinding binding, int i, FRSPresenter presenter, FRSMainFragment fragment){
            this.binding = binding;
            this.position = i;
            this.academic_year = presenter.getSemester(i);
            this.presenter = presenter;
            this.fragment = fragment;
            
            binding.listItemSemester.setOnClickListener(this::OnClick);
        }

        private void OnClick(View view) {
            if(view.getId()==binding.listItemSemester.getId()){
                Bundle result = new Bundle();
                result.putString("page", "FRSperSemesterFragment");
                result.putInt("position", this.position);
                result.putInt("academic_year", this.academic_year);
                fragment.getParentFragmentManager().setFragmentResult("semesterPosition", result);
                fragment.getParentFragmentManager().setFragmentResult("changePage",result);
            }
        }
        public void updateView(int smst){
            this.binding.itemSemesterBawah.setText(smst+"");
        }
    }
}
