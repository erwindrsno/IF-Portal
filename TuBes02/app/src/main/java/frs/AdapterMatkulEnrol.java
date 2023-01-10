package frs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.FrsItemListMatkulDialogBinding;

import java.util.ArrayList;

public class AdapterMatkulEnrol extends BaseAdapter {

    ArrayList<MataKuliahEnrol> matkulE;
    FRSPresenter presenter;
    FRSDialogAddMatkul fragment;
    FrsItemListMatkulDialogBinding binding;

    public AdapterMatkulEnrol(FRSPresenter presenter, FRSDialogAddMatkul fragment){
        super();
        this.presenter = presenter;
        this.fragment = fragment;
        this.matkulE = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return matkulE.size();
    }

    @Override
    public Object getItem(int i) {
        return matkulE.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderEn vh;
        binding = FrsItemListMatkulDialogBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        if(view==null){
            view = binding.getRoot();
            vh = new AdapterMatkulEnrol.ViewHolderEn(view, binding, i, presenter, fragment);
            view.setTag(vh);
        }
        else{
            vh = (ViewHolderEn) view.getTag();
        }
        vh.updateView(this.matkulE.get(i));
        return view;
    }

    public void update(ArrayList<MataKuliahEnrol> matkulE){
        this.matkulE = matkulE;
        notifyDataSetChanged();
    }

    private class ViewHolderEn{

        FrsItemListMatkulDialogBinding binding;
        private FRSPresenter presenter;
        private int position;
        private FRSDialogAddMatkul fragment;

        public ViewHolderEn(View view, FrsItemListMatkulDialogBinding binding, int i, FRSPresenter presenter, FRSDialogAddMatkul fragment){
            this.binding = binding;
            this.position = i;
            this.presenter = presenter;
            this.fragment = fragment;

            this.binding.listItemDialog.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            Bundle result = new Bundle();
            String name = this.presenter.getMatkulE(position).getName();
            String id = this.presenter.getMatkulE(position).getId();
            result.putString("namaMatkulE", name);
            result.putString("idMatkulE", id);
            fragment.getParentFragmentManager().setFragmentResult("pilihMatkulEnrol", result);
        }

        public void updateView(MataKuliahEnrol smst){
            String code = smst.getCode();
            String name = smst.getName();
            String id = smst.getId();
            this.binding.itemMatkulDialog.setText(name);
        }
    }
}
