package com.liu;

import java.io.Serializable;


public class Info implements Serializable {

    int door;
    String tel;
    String aim;
    int pas;

    String image = "";

    public Info() {
        // TODO Auto-generated constructor stub
    }

    public Info(String image){
        this.image = image;
    }

    public Info(int door, String tel, String aim, int pas) {
        super();
        this.door = door;
        this.tel = tel;
        this.aim = aim;
        this.pas = pas;
    }

    public String getImage() {
        return image;
    }

    public int getPas() {
        return pas;
    }
    public void setInfomation(int pas) {
        this.pas = pas;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

}
