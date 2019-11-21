package com.careme.Model;


public class UserItem {
    public String uid;
    public String fn;
    public String ln;
    public String email;
    public String phone;
    public String add;
    public String gender;
    public String age;
    public String pass;
    public String check;
    public String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public UserItem(String uid, String fn, String ln, String email, String phone, String add, String gender, String age, String pass, String check, String img) {
        this.uid = uid;
        this.fn = fn;
        this.ln = ln;
        this.email = email;
        this.phone = phone;
        this.add = add;
        this.gender = gender;
        this.age = age;
        this.pass = pass;
        this.check = check;
        this.img = img;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
