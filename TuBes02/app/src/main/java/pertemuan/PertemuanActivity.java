package pertemuan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityPengumumanBinding;

import java.util.ArrayList;
import java.util.HashMap;

import Users.User;
import drawer.SignOutDialogFragment;
import frs.FRSActivity;
import home.HomeActivity;
import login.LoginActivity;
import pengumuman.PengumumanActivity;

public class PertemuanActivity extends AppCompatActivity implements PertemuanPresenter.PertemuanUI {
    private ActivityPengumumanBinding binding;
    private HashMap<String, Fragment> fragments;
    private FragmentManager manager;
    private PertemuanPresenter presenter;
    private User user;

    private SignOutDialogFragment signOutDialogFragment;

    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receivedIntent = getIntent();
        User user = receivedIntent.getParcelableExtra("user");

        // Inisialisasi atribut-atribut
        this.binding = ActivityPengumumanBinding.inflate(this.getLayoutInflater());
        this.fragments = new HashMap<>();
        this.manager = this.getSupportFragmentManager();
        this.presenter = new PertemuanPresenter(this, user);

        //Toolbar
        this.toolbar = binding.toolbar;
        this.setSupportActionBar(toolbar);

        this.drawer = binding.drawerLayout;

        //tombol garis tiga
        abdt = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(abdt);
        abdt.syncState();
        //Toolbar

        // Buat fragment-fragment, masukkan ke Hash<ap
        Fragment home = PertemuanHomeFragment.newInstance(this.presenter);
        Fragment tambah = PertemuanTambahFragment.newInstance(this.presenter);
        Fragment jadwal = PertemuanTimeSlotFragment.newInstance(this.presenter);
        Fragment tambahJadwal = PertemuanTimeSlotAddFragment.newInstance(this.presenter);
//        this.signOutDialogFragment = SignOutDialogFragment.newInstance("exitAppDialogFragment");
        this.signOutDialogFragment = SignOutDialogFragment.newInstance("exitAppDialogFragment");
        fragments.put("home", home);
        fragments.put("tambah", tambah);
        fragments.put("jadwal", jadwal);
        fragments.put("tambahJadwal", tambahJadwal);
//        fragments.put("signoutDialogFragment",signoutDialogFragment);

        if(getIntent().getExtras() != null){
            try{
                this.user = getIntent().getParcelableExtra("user");
//                this.presenter = new HomePresenter(this.user,this);
//                this.presenter.checkUserRole();
//                this.presenter.toHideView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.getSupportFragmentManager().setFragmentResultListener("changeActivity",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(String requestKey, Bundle result){
                String activity = result.getString("activity");
                changeActivity(activity);
            }
        });

        // Listener
        this.manager.setFragmentResultListener("changePage", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String page = result.getString("page");
                        if(page.equalsIgnoreCase("exit")){
                            signOutDialogFragment.show(manager,"dialog");
                            DrawerLayout drawerLayout = findViewById(drawer.getId());
                            drawerLayout.closeDrawers();
                        }
                        else{
                            changePage(page);
                        }
                    }
                });

        this.manager.setFragmentResultListener("showDetail", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey,
                                                 @NonNull Bundle result) {
                        String id = result.getString("id");
                        String page = result.getString("page");
                        if (page.equals("detailPertemuan"))
                            fragments.put("detail", PertemuanDetailFragment
                                    .newInstance(presenter, id));
                        else
                            fragments.put("detail", PertemuanDetailUndanganFragment
                                    .newInstance(presenter, id));
                        changePage("detail");
                    }
                });

        // Set view
        setContentView(this.binding.getRoot());

        this.changePage("home");
    }

    public void changeActivity(String activity){
        Intent intent;
        switch(activity){
            case "home":
                Log.d("actHome",true+"");
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "frs/prs":
                Log.d("actFrs/prs",true+"");
                intent = new Intent(this, FRSActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "pengumuman":
                intent = new Intent(this, PengumumanActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "pertemuan":
//                Log.d("actPertemuan",true+"");
                intent = new Intent(this, PertemuanActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "login":
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
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
        fragment.show(transaction, null);
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
        if (detailFragment != null) {
            detailFragment.updateTitle(title);
        }
    }

    @Override
    public void updateOrganizerDetail(String organizer) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null) {
            detailFragment.updateOrganizer(organizer);
        }
    }

    @Override
    public void updateTimeDetail(String pertemuanDate, String pertemuanStartTime, String pertemuanEndTime) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null) {
            detailFragment.updateDate(pertemuanDate);
            detailFragment.updateTime(pertemuanStartTime, pertemuanEndTime);
        }
    }

    @Override
    public void updateDescDetail(String description) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null) {
            detailFragment.updateDescription(description);
        }
    }

    @Override
    public void showPertemuanSuccessMsg(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Berhasil")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        changePage("home");
                        PertemuanTambahFragment fragment =
                                (PertemuanTambahFragment) fragments.get("tambah");
                        fragment.resetForm();
                    }
                })
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                changePage("home");
                                PertemuanTambahFragment fragment =
                                        (PertemuanTambahFragment) fragments.get("tambah");
                                fragment.resetForm();
                            }
                        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void showInvitationSuccessMsg(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Berhasil")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        changePage("home");
                    }
                })
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                changePage("home");
                            }
                        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void updateDetailFragmentInvitees(String invitees,
                                             String attendees) {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null) {
            detailFragment.updateInvitees(invitees, attendees);
        }
    }

    @Override
    public void removeParticipantDetail() {
        PertemuanDetailFragment detailFragment =
                (PertemuanDetailFragment) this.fragments.get("detail");
        if (detailFragment != null) {
            detailFragment.removeParticipantField();
        }
    }

    @Override
    public void updateTimeSlots(ArrayList<TimeSlot> mondaySlots,
                                ArrayList<TimeSlot> tuesdaySlots,
                                ArrayList<TimeSlot> wednesdaySlots,
                                ArrayList<TimeSlot> thursdaySlots,
                                ArrayList<TimeSlot> fridaySlots,
                                ArrayList<TimeSlot> saturdaySlots,
                                ArrayList<TimeSlot> sundaySlots) {
        PertemuanTimeSlotFragment fragment =
                (PertemuanTimeSlotFragment) this.fragments.get("jadwal");
        if (fragment != null) {
            fragment.updateTimeSlots(mondaySlots, tuesdaySlots, wednesdaySlots, thursdaySlots,
                    fridaySlots, saturdaySlots, sundaySlots);
        }
    }

    @Override
    public void showTimeSlotSuccessMsg(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Berhasil")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        changePage("jadwal");
                    }
                })
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                changePage("jadwal");
                            }
                        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
