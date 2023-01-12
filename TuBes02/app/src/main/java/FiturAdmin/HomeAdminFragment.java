package FiturAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentBuatPengumumanBinding;
import com.example.tubes_02.databinding.FragmentHomeAdminBinding;


public class HomeAdminFragment extends Fragment implements View.OnClickListener{
    private FragmentHomeAdminBinding binding;
    public HomeAdminFragment(){

    }

    public static HomeAdminFragment newInstance() {
        HomeAdminFragment fragment = new HomeAdminFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeAdminBinding.inflate(inflater);
        View view = binding.getRoot();
        this.binding.btnAddUser.setOnClickListener(this);
        this.binding.btnMakeAnnoncement.setOnClickListener(this);
        Menu menu = this.binding.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnAddUser.getId()){
            Bundle result = new Bundle();
            result.putString("page","add_user");
            this.getParentFragmentManager().setFragmentResult("changePage",result);
        }
        else if(view.getId() == this.binding.btnMakeAnnoncement.getId()){
            Bundle result = new Bundle();
            result.putString("page","make_announcement");
            this.getParentFragmentManager().setFragmentResult("changePage",result);
        }
    }
}
