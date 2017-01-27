package com.piter.piterdiplomna.ObjectClasses;

/**
 * Created by Piter on 19/10/2016.
 */

public class CommentClass {
    private int id;
    private String text;
    private String create_date_time;
    private int task_id;
    private int user_id;
    private String fname;
    private String lname;

    public CommentClass() {
    }

    public CommentClass(String create_date_time, int task_id, String text, int user_id) {
        this.create_date_time = create_date_time;
        this.task_id = task_id;
        this.text = text;
        this.user_id = user_id;
    }
    public CommentClass(String create_date_time, int task_id, String text, int user_id, String fname, String lname) {
        this.create_date_time = create_date_time;
        this.task_id = task_id;
        this.text = text;
        this.user_id = user_id;
        this.fname = fname;
        this.lname = lname;
    }

    public String getCreate_date_time() {
        return create_date_time;
    }

    public void setCreate_date_time(String create_date_time) {
        this.create_date_time = create_date_time;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
