package com.piter.piterdiplomna3.ObjectClasses;

/**
 * Created by Piter on 26/09/2016.
 */

public class UserClass {
    private int id;
    private String fname;
    private String lname;
    private int position_id;
    private int organization_id;
    private boolean isChecked = false;

    public UserClass() {
    }

    public UserClass(String fname, int id, String lname, int organization_id, int position_id) {
        this.fname = fname;
        this.id = id;
        this.lname = lname;
        this.organization_id = organization_id;
        this.position_id = position_id;
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

    public int getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(int organization_id) {
        this.organization_id = organization_id;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
