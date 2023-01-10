package Users;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Dosen extends User implements Parcelable {
    private List<TimeSlot> timeSlots;

    public Dosen(String email, String password, String role) {
        super(email, password, role);
        this.timeSlots = new ArrayList<>();
    }

    public void addTimeSlot(){}

    protected Dosen(Parcel in) {
        super(in);
        this.timeSlots = new ArrayList<>();
        in.readTypedList(this.timeSlots, TimeSlot.CREATOR);
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
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
        parcel.writeTypedList(this.timeSlots);
    }


}
