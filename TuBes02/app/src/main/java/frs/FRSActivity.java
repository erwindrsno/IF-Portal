package frs;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.databinding.FrsMainBinding;

import java.util.ArrayList;

import Users.User;

public class FRSActivity extends AppCompatActivity implements UIFRSemester, UIMatkulSemester, UIMatkulEnrol {

    private FrsMainBinding binding;
    private FRSPresenter frsPresenter;
    private FragmentManager fm;
    private User user;

    private FRSMainFragment frsMainFragment;
    private FRSperSemesterFragment frsperSemesterFragment;
    private FRSDialogAddMatkul frsDialogAddMatkulFragment;
    private FRSDosenFragment frsDosenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FrsMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
}
