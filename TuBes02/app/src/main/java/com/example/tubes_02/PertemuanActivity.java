package com.example.tubes_02;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.databinding.ActivityPengumumanBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class PertemuanActivity extends AppCompatActivity implements PertemuanPresenter.PertemuanUI {
    private ActivityPengumumanBinding binding;
    private HashMap<String, Fragment> fragments;
    private FragmentManager manager;
    private PertemuanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent receivedIntent = getIntent();

        // Inisialisasi atribut-atribut
        this.binding = ActivityPengumumanBinding.inflate(this.getLayoutInflater());
        this.fragments = new HashMap<>();
        this.manager = this.getSupportFragmentManager();
//        this.presenter = new PertemuanPresenter(this,
//                (User) receivedIntent.getParcelableExtra("user"));
        this.presenter = new PertemuanPresenter(this, null);

        // Buat fragment-fragment, masukkan ke Hash<ap
        Fragment home = PertemuanHomeFragment.newInstance(this.presenter);
        Fragment tambah = PertemuanTambahFragment.newInstance(this.presenter);
        fragments.put("home", home);
        fragments.put("tambah", tambah);

        this.presenter.getThisWeekScheduleFromServer();

        // Listener
        this.manager.setFragmentResultListener("changePage", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String page = result.getString("page");
                        changePage(page);
                    }
                });

        this.manager.setFragmentResultListener("showDetail", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String id = result.getString("id");
                        String page = result.getString("page");
                        fragments.put("detail", PertemuanDetailFragment.newInstance(presenter, id));
                        changePage(page);
                    }
                });

        // Set view
        setContentView(this.binding.getRoot());

        this.changePage("home");
    }

    public void changePage(String page) {
        this.binding.drawerLayout.closeDrawers();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        try {
            ft.replace(this.binding.fragmentContainer.getId(), this.fragments.get(page));
            if (!page.equals("home")) {
                ft.addToBackStack(null);
            }
        } catch (NullPointerException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Halaman Tidak Ditemukan")
                    .setMessage("Halaman yang di-request tidak ditemukan.")
                    .setPositiveButton("OK", null)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
        ft.commit();
    }

    @Override
    public Context getUIContext() {
        return this;
    }

    @Override
    public void fragmentTambahAddSpinner(String role) {
        PertemuanTambahFragment fragment = (PertemuanTambahFragment) this.fragments.get("tambah");
        fragment.addSpinner(role);
    }

    @Override
    public void showDialog() {
        DialogFragment fragment = PertemuanTambahPartisipanDialogFragment
                .newInstance(this.presenter);
        FragmentTransaction transaction = this.manager.beginTransaction();
        fragment.show(transaction, "");
    }

    @Override
    public void showErrorMsg(String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void updatePertemuanList(ArrayList<Pertemuan> pertemuanList) {
        PertemuanHomeFragment fragment = (PertemuanHomeFragment) this.fragments.get("home");

        if (fragment != null) {
            fragment.updateListData(pertemuanList);
        }
    }

    @Override
    public void updateTitleDetail(String title) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null){
            detailFragment.updateTitle(title);
        }
    }

    @Override
    public void updateOrganizerDetail(String organizer) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null){
            detailFragment.updateOrganizer(organizer);
        }
    }

    @Override
    public void updateTimeDetail(String pertemuanDate, String pertemuanStartTime, String pertemuanEndTime) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null){
            detailFragment.updateDate(pertemuanDate);
            detailFragment.updateTime(pertemuanStartTime, pertemuanEndTime);
        }
    }

    @Override
    public void updateDescDetail(String description) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null){
            detailFragment.updateDescription(description);
        }
    }
}
