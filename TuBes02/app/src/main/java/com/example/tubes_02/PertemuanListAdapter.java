package com.example.tubes_02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tubes_02.databinding.ItemListPertemuanBinding;

import java.util.ArrayList;

public class PertemuanListAdapter extends BaseAdapter {
    private ArrayList<Pertemuan> pertemuanList;
    private PertemuanPresenter presenter;

    public PertemuanListAdapter(PertemuanPresenter presenter) {
        this.presenter = presenter;
        this.pertemuanList = this.presenter.getPertemuanList();
    }

    private class ViewHolder {
        private final TextView tvTitle;
        private final TextView tvTime;
        private final PertemuanPresenter presenter;

        public ViewHolder(ItemListPertemuanBinding binding, PertemuanPresenter presenter) {
            this.presenter = presenter;
            this.tvTitle = binding.tvTitle;
            this.tvTime = binding.tvTime;
        }

        public void updateView(int idx) {
            String title = this.presenter.getPertemuanTitle(idx);
            String time = this.presenter.getPertemuanTime(idx);
            this.tvTitle.setText(title);
            this.tvTime.setText(time);
        }
    }

    @Override
    public int getCount() {
        return this.pertemuanList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.pertemuanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemListPertemuanBinding binding = null;
        ViewHolder viewHolder;

        if (view == null) {
            binding = ItemListPertemuanBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            view = binding.getRoot();
            viewHolder = new ViewHolder(binding, this.presenter);
            view.setTag(R.id.tag_vh_pertemuan_id, viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag(R.id.tag_vh_pertemuan_id);
        }
        viewHolder.updateView(i);

        return view;
    }

    public void updateData(ArrayList<Pertemuan> pertemuanList){
        this.pertemuanList = pertemuanList;
        this.notifyDataSetChanged();
    }

}
