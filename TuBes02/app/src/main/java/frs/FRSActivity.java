package frs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.PertemuanActivity;
import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FrsMainBinding;

import java.util.ArrayList;

import Users.User;
import drawer.SignOutDialogFragment;
import home.HomeActivity;
import login.LoginActivity;
import pengumuman.PengumumanActivity;

public class FRSActivity extends AppCompatActivity implements UIFRSemester, UIMatkulSemester, UIMatkulEnrol {

    private FrsMainBinding binding;
    private FRSPresenter frsPresenter;
    private FragmentManager fm;
    private User user;

    private FRSMainFragment frsMainFragment;
    private FRSperSemesterFragment frsperSemesterFragment;
    private FRSDialogAddMatkul frsDialogAddMatkulFragment;
    private FRSDosenFragment frsDosenFragment;

    private SignOutDialogFragment signOutDialogFragment;

    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FrsMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar
        this.toolbar = binding.toolbar;
        this.setSupportActionBar(toolbar);

        this.drawer = binding.drawerLayout;

        //tombol garis tiga
        abdt = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(abdt);
        abdt.syncState();
        //Toolbar

        if(getIntent().getExtras() != null){
            try{
                this.user = getIntent().getParcelableExtra("user");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        frsPresenter = new FRSPresenter(this, this, this, this, this.user);

        frsMainFragment = FRSMainFragment.newInstance("FRSMainFragment", frsPresenter);
        frsDialogAddMatkulFragment = FRSDialogAddMatkul.newInstance("FRSDialogAddMatkul", frsPresenter);
        frsperSemesterFragment = FRSperSemesterFragment.newInstance("FRSperSemesterFragment", frsPresenter, frsDialogAddMatkulFragment);
        frsDosenFragment = FRSDosenFragment.newInstance("FRSDosenFragment");
        this.signOutDialogFragment = SignOutDialogFragment.newInstance("exitAppDialogFragment");



        this.fm = this.getSupportFragmentManager();
        FragmentTransaction ft = this.fm.beginTransaction();

        if(this.user.getRole().equals("student")){
            ft.add(binding.fragmentContainer.getId(), this.frsMainFragment)
                    .commit();
        }
        else{
            ft.add(binding.fragmentContainer.getId(), this.frsDosenFragment)
                    .commit();
        }

        this.getSupportFragmentManager().setFragmentResultListener("changePage", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String page = result.getString("page");
                Log.d("debugPage",page);
                changePage(page);
            }
        });

        this.getSupportFragmentManager().setFragmentResultListener("changeActivity",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(String requestKey, Bundle result){
                String activity = result.getString("activity");
                changeActivity(activity);
            }
        });

        this.getSupportFragmentManager().setFragmentResultListener("exitApp",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(String requestKey, Bundle result){
                String toExit = result.getString("toExit");
                if(toExit.equalsIgnoreCase("yes")){
                    closeApplication();
                }
            }
        });
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

    private void changePage(String page) {
        FragmentTransaction ft = this.fm.beginTransaction();
        if(page.equals("FRSMainFragment")&&this.user.getRole().equals("student")){
            if(this.frsMainFragment.isAdded()){
                ft.show(this.frsMainFragment);
            }
            else{
                ft.add(binding.fragmentContainer.getId(), this.frsMainFragment);
            }
            if(this.frsperSemesterFragment.isAdded()){
                ft.hide(frsperSemesterFragment);
            }
        }
        else if(page.equals("FRSperSemesterFragment")&&this.user.getRole().equals("student")){
            Log.d("masuk fragment", "masuk");
            if(this.frsperSemesterFragment.isAdded()){
                ft.show(this.frsperSemesterFragment);
            }
            else{
                ft.add(binding.fragmentContainer.getId(), this.frsperSemesterFragment);
            }
            if(this.frsMainFragment.isAdded()){
                ft.hide(frsMainFragment);
            }
        }
        else if(page.equals("exit")){
            this.signOutDialogFragment.show(this.fm,"dialog");
            ft.addToBackStack(null);
            DrawerLayout drawerLayout = findViewById(drawer.getId());
            drawerLayout.closeDrawers();
        }
        ft.commit();
    }

    @Override
    public void updateListSemester(ArrayList<Integer> semester) {
        frsMainFragment.updateListSemester(semester);
    }

    @Override
    public void updateListMatkul(ArrayList<MataKuliah> matkuls) {
        frsperSemesterFragment.updateListMatkul(matkuls);
    }

    @Override
    public void updateListMatkulE(ArrayList<MataKuliahEnrol> matkulE) {
        frsDialogAddMatkulFragment.updateListMatkulE(matkulE);
    }

    public void closeApplication(){
        this.moveTaskToBack(true);
        this.finish();
    }
}
