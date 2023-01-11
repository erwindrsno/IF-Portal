package FiturAdmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentAddUserBinding;

import Users.Admin;
import Users.Dosen;
import Users.Mahasiswa;
import Users.User;

public class AddUserFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private FragmentAddUserBinding binding;
    private Context context;
    private String role;
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

        this.binding.spinnerRole.setOnItemSelectedListener(this);

        this.binding.btnAddUser.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnAddUser.getId()){
            String name = this.binding.etNama.getText().toString();
            String email = this.binding.etEmail.getText().toString();
            String password = this.binding.newPass.getText().toString();
            String confirmPassword = this.binding.confirmPass.getText().toString();
            if(password.equals(confirmPassword)){
                if(this.role.equals("admin")){
                    User userBaru = new Admin(email,password,this.role);
                }
                else if(this.role.equals("lecturer")){
                    User userBaru = new Dosen(email, password, this.role);
                }
                else if(this.role.equals("student")){
                    User userBaru = new Mahasiswa(email,password,this.role);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == this.binding.spinnerRole.getId()){
            if(adapterView.getItemAtPosition(i).toString().toLowerCase().equalsIgnoreCase("dosen")){
                this.role = "lecturer";
            }
            else if(adapterView.getItemAtPosition(i).toString().toLowerCase().equalsIgnoreCase("mahasiswa")){
                this.role = "student";
            }
            Log.d("role new user",this.role);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
