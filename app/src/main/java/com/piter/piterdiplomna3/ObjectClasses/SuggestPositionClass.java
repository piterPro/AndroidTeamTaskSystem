package com.piter.piterdiplomna3.ObjectClasses;

/**
 * Created by Piter on 19/11/2016.
 */

public class SuggestPositionClass {
    private String title;

    public SuggestPositionClass() {
    }

    public SuggestPositionClass(String name) {
        this.title = name;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }
}
