package com.example.tubes_02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentDetailPertemuanBinding;


public class PertemuanDetailFragment extends Fragment {
    protected FragmentDetailPertemuanBinding binding;
    protected PertemuanPresenter presenter;

    public PertemuanDetailFragment() {
    }

    public static PertemuanDetailFragment newInstance(PertemuanPresenter presenter, String id) {
        Bundle args = new Bundle();
        args.putString("id", id);

        PertemuanDetailFragment fragment = new PertemuanDetailFragment();
        fragment.setArguments(args);
        fragment.presenter = presenter;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String id = this.getArguments().getString("id");
        this.binding = FragmentDetailPertemuanBinding.inflate(inflater, container, false);

        this.presenter.getPertemuanDetailsFromServer(id);
        this.presenter.getParticipantsFromServer(id);

        return binding.getRoot();
    }

    public void updateTitle(String title) {
        this.binding.tvTitle.setText(title);
    }

    public void updateOrganizer(String organizer) {
        this.binding.tvOrganizer.setText(organizer);
    }

    public void updateDate(String date) {
        this.binding.tvDate.setText(date);
    }

    public void updateTime(String startTime, String endTime) {
        this.binding.tvTimeStart.setText(startTime);
        this.binding.tvTimeEnd.setText(endTime);
    }

    public void updateDescription(String desc) {
        this.binding.tvDesc.setText(desc);
    }


    public void updateInvitees(String invitees, String attendees) {
        this.binding.tvParticipantAttending.setText(attendees.length() > 0 ? attendees : "-");
        this.binding.tvParticipantInvited.setText(invitees.length() > 0 ? invitees : "-");
    }

    public void removeParticipantField() {
        this.binding.llFormData.removeView(this.binding.llPartispan);
    }
}
