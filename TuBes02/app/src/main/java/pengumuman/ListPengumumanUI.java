package pengumuman;

import java.util.ArrayList;

import pengumuman.model.Pengumuman;
import pengumuman.model.Tag;

public interface ListPengumumanUI {
    void updateList(ArrayList<Pengumuman> daftarPengumuman);
    void updateDialogView(Pengumuman pengumuman);
    void updateTag(ArrayList<Tag> tagArrayList);
}