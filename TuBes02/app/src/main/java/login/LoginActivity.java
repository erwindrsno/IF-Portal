package login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityLoginBinding;

import home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginUI, AdapterView.OnItemSelectedListener{
    private ActivityLoginBinding binding;
    private LoginPresenter presenter;
    private String email;
    private String password;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        //dropdown roles
        String[] arrRoles = getResources().getStringArray(R.array.roles);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrRoles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.dropdownRoles.setAdapter(adapter);

        this.binding.dropdownRoles.setOnItemSelectedListener(this);
        //dropdown roles

        this.binding.btnLogin.setOnClickListener(this);

        this.presenter = new LoginPresenter(this,this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnLogin.getId()){
            this.email = this.binding.etEmail.getText().toString();
            this.password = this.binding.etPassword.getText().toString();

//            String email2 = "reinasya@mail.com";
//            String password2 = "1q2w3e45";
//            String role2 = "mahasiswa";

            String email2 = "default.admin@domain.local";
            String password2 = "mu8XyUogLi6Dk7";
            String role2 = "admin";
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            this.presenter.validateUser(email2,password2,role2);
//            this.presenter.validateUser(this.email,this.password,this.role);
        }
    }

    @Override
    public void updateViewFromAPI(boolean valid, String title) {
        if(valid){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user", (Parcelable) this.presenter.getUser());
            startActivity(intent);
        }
        else{
            if(title.equalsIgnoreCase("wrongCredentials")){
                Toast toast = Toast.makeText(this,R.string.error_input, Toast.LENGTH_LONG);
                toast.show();
            }
            else if(title.equalsIgnoreCase("timeOutError")){
                Toast toast = Toast.makeText(this,R.string.timeout, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void updateViewForInputValidation() {
        Toast toast = Toast.makeText(this,R.string.error_input, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == this.binding.dropdownRoles.getId()){
            this.role = adapterView.getItemAtPosition(i).toString().toLowerCase();
            Log.d("rolebaru",this.role);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}