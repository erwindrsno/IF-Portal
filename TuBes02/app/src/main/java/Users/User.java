package Users;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class User{
    private String email;
    private String password;
    private String role;
    private String token;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        role = in.readString();
        token = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
