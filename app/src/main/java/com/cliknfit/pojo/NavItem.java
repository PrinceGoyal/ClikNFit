package com.cliknfit.pojo;

import android.graphics.drawable.Drawable;

/**
 * Created by prince on 18/08/17.
 */

public class NavItem {
    private String name;
    private int img;

    public NavItem(String name,int img){
        this.name=name;
        this.img=img;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
