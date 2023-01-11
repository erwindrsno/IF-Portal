package pengumuman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityPengumumanBinding;

import java.util.ArrayList;

import Users.User;
import home.HomeActivity;
import login.LoginActivity;
import pengumuman.model.Pengumuman;

public class PengumumanActivity extends AppCompatActivity implements ListPengumumanUI{
    private ActivityPengumumanBinding binding;
    private PengumumanFragment pengumumanFragment;
    private DialogFragmentKontenPengumuman dialogFragmentKontenPengumuman;
    private BuatPengumumanFragment buatPengumumanFragment;
    private FragmentManager fragmentManager;
    private User user;
    private PengumumanPresenter presenter;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityPengumumanBinding.inflate(getLayoutInflater());
        View view = this.binding.getRoot();
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
        setContentView(view);

//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.binding.etFilter.getWindowToken(), 0);

        //Toolbar
        this.toolbar = this.binding.toolbar;
        this.setSupportActionBar(toolbar);

        this.drawer = this.binding.drawerLayout;

        //tombol garis tiga
        abdt = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(abdt);
        abdt.syncState();
        //Toolbar

        Log.d("masukActPengumuman",true+"");

        //presenter
        this.presenter = new PengumumanPresenter(this,this);

        //instantiate fragment
        this.dialogFragmentKontenPengumuman = DialogFragmentKontenPengumuman.newInstance("Dialog Konten Pengumuman");
        this.pengumumanFragment = PengumumanFragment.newInstance(this.presenter, this.dialogFragmentKontenPengumuman);
//        this.buatPengumumanFragment = BuatPengumumanFragment.newInstance(this);

        if(getIntent().getExtras() != null){
            this.user = getIntent().getParcelableExtra("user");
            this.presenter.setUser(this.user);
            this.presenter.executeGetPengumumanAPI(true);
        }

        this.fragmentManager = this.getSupportFragmentManager();

        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.add(this.binding.fragmentContainer.getId(), this.pengumumanFragment)
                .addToBackStack(null)
                .commit();

//        this.getSupportFragmentManager().setFragmentResultListener(
//                "changePage", this, new FragmentResultListener() {
//                    @Override
//                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                        String page = result.getString("page");
//                        changePage(page);
//                    }
//                }
//        );
        this.getSupportFragmentManager().setFragmentResultListener("changeActivity",this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(String requestKey, Bundle result){
                String activity = result.getString("activity");
                changeActivity(activity);
            }
        });
    }

    public void changeActivity(String activity){
        Intent intent;
        switch(activity){
            case "home":
//                Log.d("actHome",true+"");
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "frs/prs":
                Log.d("actFrs/prs",true+"");
                break;

            case "pengumuman":
                intent = new Intent(this, PengumumanActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                break;

            case "pertemuan":
                Log.d("actPertemuan",true+"");
                break;

            case "login":
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
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

    @Override
    public void updateList(ArrayList<Pengumuman> daftarPengumuman) {
        this.pengumumanFragment.updateListToAdapter(daftarPengumuman);
    }

    @Override
    public void updateDialogView(Pengumuman pengumuman) {
        this.pengumumanFragment.openDialog(pengumuman);
    }
}
