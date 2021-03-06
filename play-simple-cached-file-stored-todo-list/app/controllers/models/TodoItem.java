package controllers.models;

import java.util.Date;

public class TodoItem {
    private String id;
    private String title;
    private boolean done;
    private Date creationDate;

    public TodoItem() {
    }

    public TodoItem(String id, String title, boolean done, Date creationDate) {
        this.id = id;
        this.title = title;
        this.done = done;
        this.creationDate = creationDate;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
