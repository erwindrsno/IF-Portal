package pertemuan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentPertemuanTimeslotBinding;
import com.example.tubes_02.databinding.ItemListTimeSlotBinding;

import java.util.ArrayList;

public class PertemuanTimeSlotFragment extends Fragment {
    private PertemuanPresenter presenter;
    private FragmentPertemuanTimeslotBinding binding;
    private FragmentManager fm;
    private String notifText;

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
        this.notifText = getResources()
                .getString(R.string.fragment_pertemuan_detail_notification_text);

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

        LayoutInflater inflater = getLayoutInflater();

        this.addSlotChilds(inflater, this.binding.llMondaySlots, mondaySlots,
                this.binding.tvNotifMonday);
        this.addSlotChilds(inflater, this.binding.llTuesdaySlots, tuesdaySlots,
                this.binding.tvNotifTuesday);
        this.addSlotChilds(inflater, this.binding.llWednesdaySlots, wednesdaySlots,
                this.binding.tvNotifWednesday);
        this.addSlotChilds(inflater, this.binding.llThursdaySlots, thursdaySlots,
                this.binding.tvNotifThursday);
        this.addSlotChilds(inflater, this.binding.llFridaySlots, fridaySlots,
                this.binding.tvNotifFriday);
        this.addSlotChilds(inflater, this.binding.llSaturdaySlots, saturdaySlots,
                this.binding.tvNotifSaturday);
        this.addSlotChilds(inflater, this.binding.llSundaySlots, sundaySlots,
                this.binding.tvNotifSunday);
    }

    private void addSlotChilds(LayoutInflater inflater, LinearLayout slotsContainer,
                               ArrayList<TimeSlot> timeSlots, TextView tvNotif) {
        if (timeSlots.size() > 0) {
            for (TimeSlot slot : timeSlots) {
                ItemListTimeSlotBinding bindingItem = ItemListTimeSlotBinding.inflate(inflater);
                bindingItem.tvText.setText(slot.toString());
                slotsContainer.addView(bindingItem.getRoot());
            }
            LinearLayout lastChild = (LinearLayout) slotsContainer
                    .getChildAt(slotsContainer.getChildCount() - 1);
            lastChild.removeView(lastChild.getChildAt(lastChild.getChildCount() - 1));
            tvNotif.setText("");
        } else {
            slotsContainer.removeAllViews();
            tvNotif.setText(this.notifText);
        }
    }
}
