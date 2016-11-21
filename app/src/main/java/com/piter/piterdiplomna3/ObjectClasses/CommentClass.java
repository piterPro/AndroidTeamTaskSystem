package com.piter.piterdiplomna3.ObjectClasses;

/**
 * Created by Piter on 19/10/2016.
 */

public class CommentClass {
    private String text;
    private String create_date_time;
    private int task_id;
    private int user_id;

    public CommentClass() {
    }

    public CommentClass(String create_date_time, int task_id, String text, int user_id) {
        this.create_date_time = create_date_time;
        this.task_id = task_id;
        this.text = text;
        this.user_id = user_id;
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
}
