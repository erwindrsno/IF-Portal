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

import com.example.tubes_02.PertemuanActivity;
import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityHomeBinding;

import FiturAdmin.AddUserFragment;
import FiturAdmin.HomeAdminFragment;
import Users.User;
import drawer.SignOutDialogFragment;
import frs.FRSActivity;
import login.LoginActivity;
import pengumuman.PengumumanActivity;

public class HomeActivity extends AppCompatActivity implements HomeUI{
    private FragmentManager fm;
    private ActivityHomeBinding binding;
    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    private User user;

    private HomeFragment homeFragment;
    private SignOutDialogFragment signOutDialogFragment;
    private AddUserFragment addUserFragment;
    private HomeAdminFragment homeAdminFragment;

    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.homeFragment = HomeFragment.newInstance("homeFragment");
        this.signOutDialogFragment = SignOutDialogFragment.newInstance("exitAppDialogFragment");
        this.addUserFragment = AddUserFragment.newInstance(this);
        this.homeAdminFragment = HomeAdminFragment.newInstance();

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
            try{
                this.user = getIntent().getParcelableExtra("user");
                this.presenter = new HomePresenter(this.user,this);
                this.presenter.checkUserRole();
                this.presenter.toHideView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ft.add(this.binding.fragmentContainer.getId(), this.homeFragment)
                .addToBackStack(null)
                .commit();

        this.getSupportFragmentManager().setFragmentResultListener("changePage",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(String requestKey, Bundle result){
                String page = result.getString("page");
                Log.d("pagenya ngab",page);
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

    public void changePage(String page){
        FragmentTransaction ft = this.fm.beginTransaction();
        if(page.equals("homeFragment")){

            ft.replace(binding.fragmentContainer.getId(),this.homeFragment)
                    .addToBackStack(null);
            DrawerLayout drawerLayout = findViewById(drawer.getId());
            drawerLayout.closeDrawers();
//            if(this.buatPertemuanFragment.isAdded()){
//                ft.hide(this.buatPertemuanFragment);
//            }
        }
        else if(page.equals("exit")){
            this.signOutDialogFragment.show(this.fm,"dialog");
                ft.addToBackStack(null);
            DrawerLayout drawerLayout = findViewById(drawer.getId());
            drawerLayout.closeDrawers();
        }
        else if(page.equals("fitur_admin")){
            ft.replace(binding.fragmentContainer.getId(),this.homeAdminFragment)
                    .addToBackStack(null);
            DrawerLayout drawerLayout = findViewById(drawer.getId());
            drawerLayout.closeDrawers();
        }
        else if(page.equals("add_user")){

            ft.replace(binding.fragmentContainer.getId(),this.addUserFragment)
                    .addToBackStack(null);
            DrawerLayout drawerLayout = findViewById(drawer.getId());
            drawerLayout.closeDrawers();
        }
//        else if(page.equals("make_announcement")){
//            ft.replace(binding.fragmentContainer.getId(),this.)
//                    .addToBackStack(null);
//            DrawerLayout drawerLayout = findViewById(drawer.getId());
//            drawerLayout.closeDrawers();
//        }
        ft.commit();
    }

    public void closeApplication(){
        this.moveTaskToBack(true);
        this.finish();
    }

    @Override
    public void hideView() {
        this.homeFragment.hideAdminMenu();
    }

    @Override
    public void hideMenu(){

    }
}