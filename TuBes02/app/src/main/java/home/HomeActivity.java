package home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityHomeBinding;

import Users.User;
import drawer.ExitAppDialogFragment;
import pengumuman.PengumumanActivity;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fm;
    private ActivityHomeBinding binding;
    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    private User user;

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
        abdt = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(abdt);
        abdt.syncState();
        //Toolbar

        Log.d("homeActivity","masuk");

        this.fm = this.getSupportFragmentManager();
        FragmentTransaction ft = this.fm.beginTransaction();

        if(getIntent().getExtras() != null){
            this.user = getIntent().getParcelableExtra("user");
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
        switch(activity){
            case "home":
                Log.d("actHome",true+"");
                break;

            case "frs/prs":
                Log.d("actFrs/prs",true+"");
                break;

            case "pengumuman":
                Log.d("actPengumuman",true+"");
                Intent intent = new Intent(this, PengumumanActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "pertemuan":
                Log.d("actPertemuan",true+"");
                break;
        }
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
//        else if(page.equals("exit")){
//            if(this.exitAppDialogFragment.isAdded()){
////                Log.d("fragment","exit");
////                ft.show(this.exitAppDialogFragment);
//            }
//            else{
//                this.exitAppDialogFragment.show(this.fm,"dialog");
//                Log.d("fragment","exit");
//                    ft.addToBackStack(null);
//            }
//        }
        ft.commit();
    }

    public void closeApplication(){
        this.moveTaskToBack(true);
        this.finish();
    }
}