package com.example.tubes_02;

public class Pengumuman {
    private String judul;
    private String[] tags;
    private String isi;

    public Pengumuman(String judul, String []tags, String isi){
        this.judul = judul;
        this.tags = tags;
        this.isi = isi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTags() {
        String tag = "";
        for (int i = 0; i < tags.length; i++) {
            tag +=tags[i] + ", ";
        }
        return tag;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }


}
