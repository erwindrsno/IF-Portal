package Users;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Dosen extends User implements Parcelable {
    public Dosen(String email, String password, String role) {
        super(email, password, role);
    }


    protected Dosen(Parcel in) {
        super(in);
    }

    public static final Creator<Dosen> CREATOR = new Creator<Dosen>() {
        @Override
        public Dosen createFromParcel(Parcel in) {
            return new Dosen(in);
        }

        @Override
        public Dosen[] newArray(int size) {
            return new Dosen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(super.getEmail());
        parcel.writeString(super.getPassword());
        parcel.writeString(super.getRole());
        parcel.writeString(super.getToken());
    }


}
