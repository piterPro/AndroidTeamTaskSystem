package com.piter.piterdiplomna3.ObjectClasses;

/**
 * Created by Piter on 26/09/2016.
 */

public class UserClass {
    private int id;
    private String fname;
    private String lname;
    private String company_name;
    private String position_name;
    private boolean isChecked = false;

    public UserClass() {
    }

    public UserClass(String fname, int id, String lname, String company_name, String position_name) {
        this.fname = fname;
        this.id = id;
        this.lname = lname;
        this.company_name = company_name;
        this.position_name = position_name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
