package pertemuan;

import androidx.annotation.Nullable;

public class UndanganPertemuan extends Pertemuan{
    private boolean isAttending;

    public UndanganPertemuan(String id, String title, String startDateTime, String endDateTime,
                             @Nullable String description, boolean isAttending) {
        super(id, title, startDateTime, endDateTime, description);
        this.isAttending = isAttending;
    }

    public boolean isAttending() {
        return isAttending;
    }

    public void setIsAttending(boolean isAttending){
        this.isAttending = isAttending;
    }
}
