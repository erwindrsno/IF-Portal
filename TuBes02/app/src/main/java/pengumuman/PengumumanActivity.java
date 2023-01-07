package pengumuman;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.databinding.ActivityPengumumanBinding;

import Users.User;

public class PengumumanActivity extends AppCompatActivity{
    private ActivityPengumumanBinding binding;
    private PengumumanFragment pengumumanFragment;
    private BuatPengumumanFragment buatPengumumanFragment;
    private FragmentManager fragmentManager;
    private PostAuthenticatePengumuman postAuthenticatePengumuman;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityPengumumanBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.pengumumanFragment = new PengumumanFragment();
        this.buatPengumumanFragment = new BuatPengumumanFragment();
        this.fragmentManager = this.getSupportFragmentManager();

        Log.d("masukActivityPengumuman","true");

        if(getIntent().getExtras() != null){
            this.user = getIntent().getParcelableExtra("user");
            Log.d("printUserPengumuman",this.user.getEmail());
        }

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
        this.postAuthenticatePengumuman = new PostAuthenticatePengumuman(this);
//        this.postAuthenticatePengumuman.execute();
    }

    public void changePage(String page) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
//                .setCustomAnimations(
//                        R.anim.fade_in,
//                        R.anim.fade_out
//                );


        if(page.equals("pengumuman")){
            ft.replace(binding.fragmentContainer.getId(),this.pengumumanFragment)
                    .addToBackStack(null);
        }
        if(page.equals("buat_pengumuman")){
            ft.replace(binding.fragmentContainer.getId(),this.buatPengumumanFragment)
                    .addToBackStack(null);
        }

        ft.commit();
    }
}
