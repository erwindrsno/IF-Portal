package frs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import Users.User;

public class FRSPresenter{
    protected User user;
    protected Context context;
    protected int initial_year;
    protected int active_year;
    protected StorageInitialYear storageIY;

    protected ArrayList<Integer> semester;
    protected UIFRSemester uifrSemester;

    protected ArrayList<MataKuliah> matkulS;
    protected UIMatkulSemester uiMatkulSemester;

    protected ArrayList<MataKuliahEnrol> matkulE;
    protected UIMatkulEnrol uiMatkulEnrol;

    public FRSPresenter(UIFRSemester uifrSemester, UIMatkulSemester uiMatkulSemester, UIMatkulEnrol uiMatkulEnrol, Context context, User user){
        this.context = context;
        this.user = user;

        storageIY = new StorageInitialYear(context);
        initial_year=0;

        this.uifrSemester = uifrSemester;
        this.semester = new ArrayList<>();

        this.uiMatkulSemester = uiMatkulSemester;
        this.matkulS = new ArrayList<>();

        this.uiMatkulEnrol = uiMatkulEnrol;
        this.matkulE = new ArrayList<>();
    }

    public void getSemestertoAdapter(){
        Log.d("initial year presenter", initial_year+"");
        if(semester!=null&&initial_year==0){
            Log.d("load semester", "masuk");
            int temp = this.storageIY.getInitialYear()+0;
            Log.d("temp initial year", temp+"");
            if(temp==0){

                Context context = this.context;
                CharSequence text = "Harap tunggu!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                getSemester task = new getSemester(this.context, this);
                task.execute(this.user);
            }
            else if(temp>0){
                initial_year=temp;
            }
        }
        if(initial_year>0&&semester!=null){
            ArrayList<Integer> semesterTemp = new ArrayList<>();
            int tahun = initial_year/10;
            int tahun1 = tahun;
            for(int idx=tahun1; idx<tahun+7; idx++){
                for(int j=1; j<=3; j++){
                    String a = idx+""+j;
                    int temp = Integer.parseInt(a);
                    semesterTemp.add(temp);
                }
            }
            semester = semesterTemp;
            this.uifrSemester.updateListSemester(semester);
        }
    }

    public void setInitialYear(int initial_year){
        this.initial_year = initial_year;
        this.storageIY.saveInitialYear(initial_year);
        this.getSemestertoAdapter();
    }

    public String getUserToken(){
        return this.user.getToken();
    }

    public int getSemester(int i){
        return semester.get(i);
    }

    public void addSemester(int smst){
        this.semester.add(smst);
        this.uifrSemester.updateListSemester(semester);
    }

    public void setMatkulSemestertoAdapter(int academic_year){
        if(matkulS!=null){
            matkulS.clear();
//            user = new Mahasiswa("reinasya@mail.com", "1q2w3e45", "student");
//            this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkZWYyZjkwNC0wZTFiLTQyNDctYTM5Yi00NjdjZTRmMDk1ZGYiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMjY5NjB9.TkCb0-ofNsCrUvhSJRYgP9h_gJARHRgRdqXOvEV0c18");

            Context context = this.context;
            CharSequence text = "Harap tunggu!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            getMatkulSemester task = new getMatkulSemester(this.context, this);
            task.execute(this.user, academic_year);
        }
    }

    public void setMatkuls(ArrayList<MataKuliah> matkuls){
        this.matkulS = matkuls;
    }

    public void addMatkul(MataKuliah matkul){
        this.matkulS.add(matkul);
        this.uiMatkulSemester.updateListMatkul(matkulS);
    }

    public MataKuliah getMatkul(int i) { return matkulS.get(i); }

    public void setMatkulEnroltoAdapterAwal(){
        if(matkulE!=null){
            matkulE.clear();
//            user = new Mahasiswa("reinasya@mail.com", "1q2w3e45", "student");
//            this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkZWYyZjkwNC0wZTFiLTQyNDctYTM5Yi00NjdjZTRmMDk1ZGYiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMjY5NjB9.TkCb0-ofNsCrUvhSJRYgP9h_gJARHRgRdqXOvEV0c18");

            Context context = this.context;
            CharSequence text = "Harap tunggu!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            getMatkulForEnrol taskE = new getMatkulForEnrol(this.context, this);
            taskE.executeAwal(user);
        }
    }

    public void setMatkulEnroltoAdapterSearch(String search){
        if(matkulE!=null){
            matkulE.clear();
//            user = new Mahasiswa("reinasya@mail.com", "1q2w3e45", "student");
//            this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkZWYyZjkwNC0wZTFiLTQyNDctYTM5Yi00NjdjZTRmMDk1ZGYiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMjY5NjB9.TkCb0-ofNsCrUvhSJRYgP9h_gJARHRgRdqXOvEV0c18");

            Context context = this.context;
            CharSequence text = "Harap tunggu!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            getMatkulForEnrol taskE = new getMatkulForEnrol(this.context, this);
            taskE.executeSearch(user, search);
        }
    }

    public void addMatkulE(MataKuliahEnrol matkule){
        this.matkulE.add(matkule);
        this.uiMatkulEnrol.updateListMatkulE(matkulE);
    }

    public MataKuliahEnrol getMatkulE(int i){
        return matkulE.get(i);
    }

    public void addEnrolAPI(String id, int academic_year){
//        user = new Mahasiswa("reinasya@mail.com", "1q2w3e45", "student");
//        this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkZWYyZjkwNC0wZTFiLTQyNDctYTM5Yi00NjdjZTRmMDk1ZGYiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMjY5NjB9.TkCb0-ofNsCrUvhSJRYgP9h_gJARHRgRdqXOvEV0c18");

        Context context = this.context;
        CharSequence text = "Harap tunggu!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        postTambahEnrol post = new postTambahEnrol(this.context, this);
        post.execute(user, id, academic_year);
    }

    public void deleteEnrolAPI(String id, int academic_year){
//        user = new Mahasiswa("reinasya@mail.com", "1q2w3e45", "student");
//        this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkZWYyZjkwNC0wZTFiLTQyNDctYTM5Yi00NjdjZTRmMDk1ZGYiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMjY5NjB9.TkCb0-ofNsCrUvhSJRYgP9h_gJARHRgRdqXOvEV0c18");

        deleteEnrolMatkul deleteT = new deleteEnrolMatkul(this.context, this);
        deleteT.execute(user, id, academic_year);
    }

    public void searchActiveYear(){
//        user = new Mahasiswa("reinasya@mail.com", "1q2w3e45", "student");
//        this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkZWYyZjkwNC0wZTFiLTQyNDctYTM5Yi00NjdjZTRmMDk1ZGYiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMjY5NjB9.TkCb0-ofNsCrUvhSJRYgP9h_gJARHRgRdqXOvEV0c18");

        Context context = this.context;
        CharSequence text = "Harap tunggu!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        getAcademicYear getActive = new getAcademicYear(this.context, this);
        getActive.execute(user);
    }

    public int getActiveYear(){
        if(active_year>1){
            return active_year;
        }
        else{
            searchActiveYear();
            return active_year;
        }
    }

    public void setActive_year(int active_year) {
        this.active_year = active_year;
    }
}
