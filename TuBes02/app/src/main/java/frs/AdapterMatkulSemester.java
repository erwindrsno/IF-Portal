package frs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.FrsItemListMatkulBinding;

import java.util.ArrayList;

public class AdapterMatkulSemester extends BaseAdapter {

    ArrayList<MataKuliah> matkulS;
    FRSPresenter presenter;
    FRSperSemesterFragment fragment;
    FrsItemListMatkulBinding binding;
    int academic_year;

    public AdapterMatkulSemester(FRSPresenter presenter, FRSperSemesterFragment fragment, int academic_year){
        super();
        this.matkulS = new ArrayList<>();
        this.presenter = presenter;
        this.fragment = fragment;
        this.academic_year = academic_year;
    }

    @Override
    public int getCount() {
        return matkulS.size();
    }

    @Override
    public Object getItem(int i) {
        return matkulS.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderMS vh;
        binding = FrsItemListMatkulBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        if(view==null){
            view = binding.getRoot();
            vh = new ViewHolderMS(view, binding, i, presenter, fragment);
            view.setTag(vh);
        }
        else{
            vh = (ViewHolderMS) view.getTag();
        }
        vh.updateView(this.matkulS.get(i));
        return view;
    }

    public void update(ArrayList<MataKuliah> matkulS){
        this.matkulS = matkulS;
        notifyDataSetChanged();
    }

    private class ViewHolderMS{

        FrsItemListMatkulBinding binding;
        private FRSPresenter presenter;
        private int position;
        private FRSperSemesterFragment fragment;

        public ViewHolderMS(View view, FrsItemListMatkulBinding binding, int i, FRSPresenter presenter, FRSperSemesterFragment fragment){
            this.binding = binding;
            this.position = i;
            this.presenter = presenter;
            this.fragment = fragment;
            
            this.binding.btnDelMatkul.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            if(view.getId()==this.binding.btnDelMatkul.getId()){
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(presenter.matkulS.size()>0) {
                                    presenter.deleteEnrolAPI(presenter.matkulS.get(position).getCourse_id(), academic_year);
                                    break;
                                }

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.context);
                builder.setMessage("Yakin ingin menghapus matakuliah?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();
            }
        }

        public void updateView(MataKuliah smst){
            String code = smst.getCode();
            String name = smst.getName();
            String id = smst.getCourse_id();
            this.binding.itemMatkul.setText(code);
            this.binding.namaMatkul.setText(name);
            notifyDataSetChanged();
        }
    }
}
