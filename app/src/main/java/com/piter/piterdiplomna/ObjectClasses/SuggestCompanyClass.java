package com.piter.piterdiplomna.ObjectClasses;

/**
 * Created by Piter on 19/11/2016.
 */

public class SuggestCompanyClass {
    private String name;
    private String title;

    public SuggestCompanyClass() {
    }

    public SuggestCompanyClass(String name) {
        this.name = name;
    }
//    public SuggestCompanyClass(String title) {
//        this.title = title;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
