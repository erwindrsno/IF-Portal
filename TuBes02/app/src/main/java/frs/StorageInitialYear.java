package frs;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageInitialYear {
    protected SharedPreferences sharedPref;
    protected final static String NAMA_SHARED_PREF="sp_initial_year";
    protected final static String KEY_INITIAL_YEAR="INITIAL_YEAR";

    public StorageInitialYear(Context context){
        this.sharedPref = context.getSharedPreferences(NAMA_SHARED_PREF, 0);
    }

    public void saveInitialYear(int iy){
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putInt(KEY_INITIAL_YEAR, iy);
        editor.commit();
    }

    public int getInitialYear(){
        return sharedPref.getInt(KEY_INITIAL_YEAR,0);
    }

    public void deleteIY(){
        sharedPref.edit().clear();
    }
}
