package FiturAdmin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentAddUserBinding;

public class AddUserFragment extends Fragment {
    private FragmentAddUserBinding binding;
    private Context context;
    public AddUserFragment(){

    }

    public static AddUserFragment newInstance(Context context) {
        AddUserFragment fragment = new AddUserFragment();
        fragment.context = context;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater);
        View view = binding.getRoot();

        //Drop Down
        String [] filter = getResources().getStringArray(R.array.admin_add_user);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.spinnerRole.setAdapter(adapter);


        this.binding.btnAddUser.setOnClickListener(this::onclick);
        return view;
    }

    private void onclick(View view) {
        if(view == this.binding.btnAddUser){
            if(this.binding.newPass.getText().toString().equals(this.binding.confirmPass.getText().toString())){

            }
        }

    }
}
