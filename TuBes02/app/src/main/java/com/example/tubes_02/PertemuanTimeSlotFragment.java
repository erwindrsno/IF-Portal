package com.example.tubes_02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubes_02.databinding.FragmentPertemuanTimeslotBinding;

import java.util.ArrayList;

public class PertemuanTimeSlotFragment extends Fragment {
    private PertemuanPresenter presenter;
    private FragmentPertemuanTimeslotBinding binding;
    private FragmentManager fm;

    public PertemuanTimeSlotFragment() {
    }

    public static PertemuanTimeSlotFragment newInstance(PertemuanPresenter presenter) {
        PertemuanTimeSlotFragment fragment = new PertemuanTimeSlotFragment();
        fragment.presenter = presenter;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = FragmentPertemuanTimeslotBinding.inflate(inflater, container, false);
        this.fm = getParentFragmentManager();

        Spinner spinnerLecturer = this.binding.spinnerLecturer;
        ArrayAdapter<OtherUser> adapter = new ArrayAdapter<>(spinnerLecturer.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                this.presenter.getLecturerList());
        spinnerLecturer.setAdapter(adapter);

        this.binding.btnSearch.setOnClickListener(this::onSearchClick);
        if (this.presenter.loggedInUserIsLecturer()) {
            this.binding.fabAdd.setOnClickListener(this::onAddClick);
        } else {
            this.binding.getRoot().removeView(this.binding.fabAdd);
        }

        return this.binding.getRoot();
    }

    private void onAddClick(View view) {
        Bundle args = new Bundle();
        args.putString("page", "tambahJadwal");
        this.fm.setFragmentResult("changePage", args);
    }

    private void onSearchClick(View view) {
        int pos = this.binding.spinnerLecturer.getSelectedItemPosition();
        this.presenter.getTimeslotsFromServer(this.presenter.getUserId(OtherUser.ROLE_LECTURER, pos));
    }


    public void updateTimeSlots(ArrayList<TimeSlot> mondaySlots,
                                ArrayList<TimeSlot> tuesdaySlots,
                                ArrayList<TimeSlot> wednesdaySlots,
                                ArrayList<TimeSlot> thursdaySlots,
                                ArrayList<TimeSlot> fridaySlots,
                                ArrayList<TimeSlot> saturdaySlots,
                                ArrayList<TimeSlot> sundaySlots) {
        String notifText =
                getResources().getString(R.string.fragment_pertemuan_detail_notification_text);

        ListView lvMonday = this.binding.lvMonday;
        ListView lvTuesday = this.binding.lvTuesday;
        ListView lvWednesday = this.binding.lvWednesday;
        ListView lvThursday = this.binding.lvThursday;
        ListView lvFriday = this.binding.lvFriday;
        ListView lvSaturday = this.binding.lvSaturday;
        ListView lvSunday = this.binding.lvSunday;

        // Monday
        if (mondaySlots.size() > 0) {
            lvMonday.setAdapter(new ArrayAdapter<TimeSlot>(lvMonday.getContext(),
                    R.layout.item_list_time_slot, mondaySlots));
            this.binding.tvNotifMonday.setText("");
        } else {
            lvMonday.setAdapter(null);
            this.binding.tvNotifMonday.setText(notifText);
        }

        //Tuesday
        if (tuesdaySlots.size() > 0) {
            lvTuesday.setAdapter(new ArrayAdapter<TimeSlot>(lvTuesday.getContext(),
                    R.layout.item_list_time_slot, tuesdaySlots));
            this.binding.tvNotifTuesday.setText("");
        } else {
            lvTuesday.setAdapter(null);
            this.binding.tvNotifTuesday.setText(notifText);
        }

        //Wednesday
        if (wednesdaySlots.size() > 0) {
            lvWednesday.setAdapter(new ArrayAdapter<TimeSlot>(lvWednesday.getContext(),
                    R.layout.item_list_time_slot, wednesdaySlots));
            this.binding.tvNotifWednesday.setText("");
        } else {
            lvWednesday.setAdapter(null);
            this.binding.tvNotifWednesday.setText(notifText);
        }

        if (thursdaySlots.size() > 0) {
            lvThursday.setAdapter(new ArrayAdapter<TimeSlot>(lvThursday.getContext(),
                    R.layout.item_list_time_slot, thursdaySlots));
            this.binding.tvNotifThursday.setText("");
        } else {
            lvThursday.setAdapter(null);
            this.binding.tvNotifThursday.setText(notifText);
        }

        if (fridaySlots.size() > 0) {
            lvFriday.setAdapter(new ArrayAdapter<TimeSlot>(lvFriday.getContext(),
                    R.layout.item_list_time_slot, fridaySlots));
            this.binding.tvNotifFriday.setText("");
        } else {
            lvFriday.setAdapter(null);
            this.binding.tvNotifFriday.setText(notifText);
        }

        if (saturdaySlots.size() > 0) {
            lvSaturday.setAdapter(new ArrayAdapter<TimeSlot>(lvSaturday.getContext(),
                    R.layout.item_list_time_slot, saturdaySlots));
            this.binding.tvNotifSaturday.setText("");
        } else {
            lvSaturday.setAdapter(null);
            this.binding.tvNotifSaturday.setText(notifText);
        }

        if (sundaySlots.size() > 0) {
            lvSunday.setAdapter(new ArrayAdapter<TimeSlot>(lvSunday.getContext(),
                    R.layout.item_list_time_slot, sundaySlots));
            this.binding.tvNotifSunday.setText("");
        } else {
            lvSunday.setAdapter(null);
            this.binding.tvNotifSunday.setText(notifText);
        }
    }
}
