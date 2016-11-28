package com.piter.piterdiplomna3.ObjectClasses;

import java.util.ArrayList;
import java.util.List;

public class TaskClass {
        private int id;
        private String title;
        private String description;
        private String status;
        private String begin_date;
        private String end_date;
//        private int parent_id;
        private int user_made_by_id;
        private String user_made_for_id;
        private String team;

    public TaskClass() {
    }

    public TaskClass(String begin_date, String description, String end_date, int id,  String status, String title, int user_made_by_id, String user_made_for_id) {
        this.begin_date = begin_date;
        this.description = description;
        this.end_date = end_date;
        this.id = id;
//        this.parent_id = parent_id;
        this.status = status;
//        this.time_period = time_period;
        this.title = title;
        this.user_made_by_id = user_made_by_id;
        this.user_made_for_id = user_made_for_id;
        this.team = team;
    }
    public TaskClass(String begin_date, String description, String end_date, String status, String title, int user_made_by_id, String user_made_for_id, String team) {
        this.begin_date = begin_date;
        this.description = description;
        this.end_date = end_date;
//        this.parent_id = parent_id;
        this.status = status;
//        this.time_period = time_period;
        this.title = title;
        this.user_made_by_id = user_made_by_id;
        this.user_made_for_id = user_made_for_id;
        this.team = team;
    }


    public TaskClass(int id, int parent_id, String status, int time_period, String title, int user_made_by_id, String user_made_for_id, String description) {

        this.id = id;
//        this.parent_id = parent_id;
        this.status = status;
//        this.time_period = time_period;
        this.title = title;
        this.user_made_by_id = user_made_by_id;
        this.user_made_for_id = user_made_for_id;
        this.description = description;
    }

    public TaskClass(String title, String description, String status, String begin_date, String end_date, String user_made_by_id, String user_made_for_id, String team) {
        this.begin_date = begin_date;
        this.description = description;
        this.end_date = end_date;
        this.status = status;
        this.title = title;
        this.team = team;
        this.user_made_by_id = Integer.parseInt(user_made_by_id);
        this.user_made_for_id = user_made_for_id;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getParent_id() {
//        return parent_id;
//    }

//    public void setParent_id(int parent_id) {
//        this.parent_id = parent_id;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_made_by_id() {
        return user_made_by_id;
    }

    public void setUser_made_by_id(int user_made_by_id) {
        this.user_made_by_id = user_made_by_id;
    }

    public String getUser_made_for_id() {
        return user_made_for_id;
    }

    public void setUser_made_for_id(String user_made_for_id) {
        this.user_made_for_id = user_made_for_id;
    }
}


