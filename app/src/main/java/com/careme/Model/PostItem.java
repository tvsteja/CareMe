package com.careme.Model;


public class PostItem {
    public String uid;
    public String hl;
    public String add;
    public String hr;
    public String date;
    public String time;
    public String desc;
    public String img;
    public String type;
    public String phone;
    public String postBy = "";
    public String email = "";
    private String isFav = "";
    private String exp = "";
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String pid;

    public PostItem(String uid, String hl, String add, String hr, String date, String time, String exp, String desc, String img,
                    String type, String phone, String pid, String postBy, String email, String isFav) {
        this.uid = uid;
        this.hl = hl;
        this.add = add;
        this.hr = hr;
        this.date = date;
        this.time = time;
        this.exp = exp;
        this.desc = desc;
        this.img = img;
        this.type = type;
        this.phone = phone;
        this.pid = pid;
        this.postBy = postBy;
        this.email = email;
        this.isFav = isFav;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String isFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }
}
