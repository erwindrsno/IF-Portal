package com.example.tubes_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.tubes_02.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PengumumanFragment pengumumanFragment;
    private PertemuanFragment pertemuanFragment;
    private PrasyaratFragment prasyaratFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.pengumumanFragment = new PengumumanFragment();
        this.pertemuanFragment = new PertemuanFragment();
        this.prasyaratFragment = new PrasyaratFragment();

        this.fragmentManager = this.getSupportFragmentManager();

        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.add(this.binding.fragmentContainer.getId(), this.pengumumanFragment)
                .addToBackStack(null)
                .commit();

        this.getSupportFragmentManager().setFragmentResultListener(
                "changePage", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String page = result.getString("page");
                        changePage(page);
                    }
                }
        );
    }
    public void changePage(String page){
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
//                .setCustomAnimations(
//                        R.anim.fade_in,
//                        R.anim.fade_out
//                );

        if(page.equals("pertemuan")){

            ft.replace(binding.fragmentContainer.getId(),this.pertemuanFragment)
                    .addToBackStack(null);
        }
        if(page.equals("pengumuman")){
            ft.replace(binding.fragmentContainer.getId(),this.pengumumanFragment)
                    .addToBackStack(null);
        }
        if(page.equals("prasyarat")){
            ft.replace(binding.fragmentContainer.getId(),this.prasyaratFragment)
                    .addToBackStack(null);
        }

        ft.commit();
    }
}