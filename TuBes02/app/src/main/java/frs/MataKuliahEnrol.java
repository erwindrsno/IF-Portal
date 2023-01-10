package frs;

public class MataKuliahEnrol {
    private String id;
    private String code;
    private String name;
    private int semester;

    public MataKuliahEnrol(String id, String code, String name, int semester) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }
}
