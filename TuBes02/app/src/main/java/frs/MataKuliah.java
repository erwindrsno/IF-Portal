package frs;

public class MataKuliah {
    private String course_id;
    private String code;
    private String name;
    private int academic_year;
    private String score;

    public MataKuliah(String course_id, String code, String name, int academic_year, String score) {
        this.course_id = course_id;
        this.code = code;
        this.name = name;
        this.academic_year = academic_year;
        this.score = score;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAcademic_year() {
        return academic_year;
    }
}
