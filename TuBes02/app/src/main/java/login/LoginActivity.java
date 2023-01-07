package login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import home.HomeActivity;
import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginUI{
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

        Log.d("test masuk activity","masuk");

        this.binding.btnAdmin.setOnClickListener(this);
        this.binding.btnDosen.setOnClickListener(this);
        this.binding.btnMahasiswa.setOnClickListener(this);

        this.binding.btnLogin.setOnClickListener(this);

        this.presenter = new LoginPresenter(this,this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnAdmin.getId()){
            this.role = this.binding.btnAdmin.getText().toString();
        }
        if(view.getId() == this.binding.btnDosen.getId()){
            this.role = this.binding.btnDosen.getText().toString();
        }
        if(view.getId() == this.binding.btnMahasiswa.getId()){
            this.role = this.binding.btnMahasiswa.getText().toString();
        }
        if(view.getId() == this.binding.btnLogin.getId()){
            this.email = this.binding.etEmail.getText().toString();
            this.password = this.binding.etPassword.getText().toString();

//            String email2 = "default.admin@domain.local";
//            String password2 = "mu8XyUogLi6Dk7";
//            String role2 = "admin";

//            this.presenter.validateUser(email,password,role);
            this.presenter.validateUser(this.email,this.password,this.role);
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
                this.binding.tvLoginFail.setText(R.string.error_input);
            }
            else if(title.equalsIgnoreCase("timeOutError")){
                this.binding.tvLoginFail.setText(R.string.timeout);
            }
        }
    }

    @Override
    public void updateViewForInputValidation() {
        this.binding.tvLoginFail.setText(R.string.error_input);
    }
}