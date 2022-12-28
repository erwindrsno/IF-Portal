package com.example.tubes_02;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.databinding.ActivityMainBinding;
import com.example.tubes_02.databinding.ActivityPengumumanBinding;

public class PengumumanActivity extends AppCompatActivity {
    private ActivityPengumumanBinding binding;
    private PengumumanFragment pengumumanFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityPengumumanBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.pengumumanFragment = new PengumumanFragment();

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


        if(page.equals("pengumuman")){
            ft.replace(binding.fragmentContainer.getId(),this.pengumumanFragment)
                    .addToBackStack(null);
        }


        ft.commit();
    }
}
