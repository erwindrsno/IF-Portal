package pengumuman.model;

import java.util.ArrayList;

public class Pengumuman {
    private String id;
    private String title;
    private String updated_at;
    private String created_at;
    private ArrayList<Tag> tags;
    private String content;

    public Pengumuman(String id, String title, String updated_at, String created_at){
        this.id = id;
        this.title = title;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.tags = new ArrayList<>();
    }

    public Pengumuman(String title, String content, String tags){
        this.tags = new ArrayList<>();
        this.title = title;
        this.content = content;
        this.tags.add(new Tag(tags));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
