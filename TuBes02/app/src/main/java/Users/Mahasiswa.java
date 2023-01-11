package Users;

import android.os.Parcel;
import android.os.Parcelable;

public class Mahasiswa extends User implements Parcelable {

    public Mahasiswa(String email, String password, String role){
        super(email,password,role);
    }

    protected Mahasiswa(Parcel in) {
        super(in);
    }

    public static final Creator<Mahasiswa> CREATOR = new Creator<Mahasiswa>() {
        @Override
        public Mahasiswa createFromParcel(Parcel in) {
            return new Mahasiswa(in);
        }

        @Override
        public Mahasiswa[] newArray(int size) {
            return new Mahasiswa[size];
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
