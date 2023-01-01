package login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.example.tubes_02.HomeActivity;
import com.example.tubes_02.R;
import com.example.tubes_02.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginUI{
    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Log.d("test masuk activity","masuk");

        this.binding.btnLogin.setOnClickListener(this);

        this.presenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnLogin.getId()){
            String email = this.binding.etEmail.getText().toString();
            String password = this.binding.etPassword.getText().toString();
            String role = this.binding.etRole.getText().toString();

            String email2 = "default.admin@domain.local";
            String password2 = "mu8XyUogLi6Dk7";
            String role2 = "admin";

            this.presenter.newUser(email2,password2,role2);

            PostAuthenticate authTask = new PostAuthenticate(this, this.presenter);
            authTask.execute(this.presenter.getUser());
        }
    }

    @Override
    public void updateView(boolean valid) {
        if(valid){
            Log.d("Login attempt","sukses!");
            Log.d("getUserRoleNameOnSuccess",this.presenter.getUser().getRole());

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user", (Parcelable) this.presenter.getUser());
            startActivity(intent);
        }
        else{
            this.binding.tvLoginFail.setText(R.string.login_fail);
            Log.d("getUserRoleName",this.presenter.getUser().getRole());
        }
    }
}