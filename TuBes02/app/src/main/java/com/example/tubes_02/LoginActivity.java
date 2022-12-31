package com.example.tubes_02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tubes_02.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginUI{
    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnLogin.setOnClickListener(this);

        this.presenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnLogin.getId()){
            String email = this.binding.etEmail.getText().toString();
            String password = this.binding.etPassword.getText().toString();
            String role = this.binding.etRole.getText().toString();

//            String email2 = "default.admin@domain.local";
//            String password2 = "mu8XyUogLi6Dk7";
//            String role2 = "admin";

            this.presenter.newUser(email,password,role);

            PostAuthenticate authTask = new PostAuthenticate(this, this.presenter);
            authTask.execute(this.presenter.getUser());
        }
    }

    @Override
    public void updateView(boolean valid) {
        if(valid){
            Log.d("Login attempt","sukses!");
        }
        else{
            this.binding.tvLoginFail.setText(R.string.login_fail);
            Log.d("getUserRoleName",this.presenter.getUser().getRole());
        }
    }
}