package com.example.tubes_02;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.databinding.ActivityHomeBinding;

import android.os.Bundle;
import android.util.Log;

import Users.User;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fm;
    private ActivityHomeBinding binding;
    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    private HomeFragment homeFragment;
    private ExitAppDialogFragment exitAppDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.homeFragment = HomeFragment.newInstance("homeFragment");
        this.exitAppDialogFragment = ExitAppDialogFragment.newInstance("exitAppDialogFragment");


        //Toolbar
        this.toolbar = binding.toolbar;
        this.setSupportActionBar(toolbar);

        this.drawer = binding.drawerLayout;

        //tombol garis tiga
        abdt = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(abdt);
        abdt.syncState();
        //Toolbar

        Log.d("homeActivity","masuk");

        this.fm = this.getSupportFragmentManager();
        FragmentTransaction ft = this.fm.beginTransaction();

        if(getIntent().getExtras() != null){
            User user = getIntent().getParcelableExtra("user");
            Log.d("homeUserActivity",user.getEmail());
        }

        ft.add(this.binding.fragmentContainer.getId(), this.homeFragment)
                .addToBackStack(null)
                .commit();

        this.getSupportFragmentManager().setFragmentResultListener("changePage",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(String requestKey, Bundle result){
                String page = result.getString("page");
                changePage(page);
            }
        });
    }

    public void changePage(String page){
        FragmentTransaction ft = this.fm.beginTransaction();
        if(page.equals("homeFragment")){
            if(this.homeFragment.isAdded()){
                ft.show(this.homeFragment);
            }
            else{
                ft.add(this.binding.fragmentContainer.getId(),this.homeFragment);
            }
//            if(this.buatPertemuanFragment.isAdded()){
//                ft.hide(this.buatPertemuanFragment);
//            }
        }
        else if(page.equals("exit")){
            if(this.exitAppDialogFragment.isAdded()){
                ft.show(this.exitAppDialogFragment);
            }
            else{
                ft.add(this.binding.fragmentContainer.getId(),this.exitAppDialogFragment);
            }
        }
    }
}