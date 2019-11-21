package com.careme.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

@SuppressLint("CommitPrefEdits")
public class PreferenceManager extends Application {

    static SharedPreferences preferences;
    static SharedPreferences.Editor prefEditor;

    private Activity activeActivity;

    public static int getCurrentPos() {
        return preferences.getInt("position", 0);
    }

    public static void setCurrentPos(int position) {
        prefEditor.putInt("position", position).commit();
    }

    public static void SetPhoneNo(String PhoneNo) {
        prefEditor.putString("PhoneNo", PhoneNo).commit();
    }

    public static String GetPhoneNo() {
        return preferences.getString("PhoneNo", "");
    }

    public static void SetProfile(String Profile) {
        prefEditor.putString("Profile", Profile).commit();
    }

    public static String GetProfile() {
        return preferences.getString("Profile", "");
    }

    public static void SetUID(String UID) {
        prefEditor.putString("UID", UID).commit();
    }

    public static String GetUID() {
        return preferences.getString("UID", "");
    }

    public static void SetEmail(String Email) {
        prefEditor.putString("Email", Email).commit();
    }

    public static String GetEmail() {
        return preferences.getString("Email", "");
    }

    public static void SetPassword(String Password) {
        prefEditor.putString("Password", Password).commit();
    }

    public static String GetPassword() {
        return preferences.getString("Password", "");
    }

    public static void SetFn(String Fn) {
        prefEditor.putString("Fn", Fn).commit();
    }

    public static String GetFn() {
        return preferences.getString("Fn", "");
    }

    public static void SetLn(String Ln) {
        prefEditor.putString("Ln", Ln).commit();
    }

    public static String GetLn() {
        return preferences.getString("Ln", "");
    }

    public static void SetAddress(String Address) {
        prefEditor.putString("Address", Address).commit();
    }

    public static String GetAddress() {
        return preferences.getString("Address", "");
    }

    public static void SetAge(String Age) {
        prefEditor.putString("Age", Age).commit();
    }

    public static String GetAge() {
        return preferences.getString("Age", "");
    }

    public static void SetGender(String Gender) {
        prefEditor.putString("Gender", Gender).commit();
    }

    public static String GetGender() {
        return preferences.getString("Gender", "");
    }

    public static void Setfb_dtsg_ag(String fb_dtsg_ag) {
        prefEditor.putString("fb_dtsg_ag", fb_dtsg_ag).commit();
    }

    public static String Getfb_dtsg_ag() {
        return preferences.getString("fb_dtsg_ag", "");
    }

    public static void Setgid(String gid) {
        prefEditor.putString("gid", gid).commit();
    }

    public static String Getgid() {
        return preferences.getString("gid", "");
    }

    public static void SetLOginid(String LOginid) {
        prefEditor.putString("LOginid", LOginid).commit();
    }

    public static String GetLOginid() {
        return preferences.getString("LOginid", "");
    }

    public static void Setnextcursor(String nextcursor) {
        prefEditor.putString("nextcursor", nextcursor).commit();
    }

    public static String Getnextcursor() {
        return preferences.getString("nextcursor", "");
    }

    public static void Setglink(String glink) {
        prefEditor.putString("glink", glink).commit();
    }

    public static String Getglink() {
        return preferences.getString("glink", "");
    }

    public static void Setflink(String flink) {
        prefEditor.putString("flink", flink).commit();
    }

    public static String Getflink() {
        return preferences.getString("flink", "http://m.me/");
    }

    public static void SetFbMsg(String FbMsg) {
        prefEditor.putString("FbMsg", FbMsg).commit();
    }

    public static String GetFbMsg() {
        return preferences.getString("FbMsg", "");
    }

    public static void SetCookie(String Cookie) {
        prefEditor.putString("Cookie", Cookie).commit();
    }

    public static String GetCookie() {
        return preferences.getString("Cookie", "datr=j-68WbHzQz_3pmJ8s8TBJbQk; sb=kO68WWT-K-x24npCYZMP46fh; c_user=100023163136370; xs=24%3AE8xdjkUChf2MxA%3A2%3A1544530365%3A16787%3A10067; pl=n; spin=r.4644838_b.trunk_t.1545109590_s.1_v.2_; x-referer=eyJyIjoiL2dyb3Vwcy8yMTE3MjM4NDU1MTU2ODY0L21lbWJlcnMvIiwiaCI6Ii9ncm91cHMvMjExNzIzODQ1NTE1Njg2NC9tZW1iZXJzLyIsInMiOiJtIn0%3D; fr=0r3oLjL6ujXxlfU06.AWVHz2B1VaKzzKnCISMgU0QSKbI.BZvO6Q.As.AAA.0.0.BcGINB.AWUPNl0i; m_pixel_ratio=1; wd=645x626; presence=EDvF3EtimeF1545110381EuserFA21B23163136370A2EstateFDutF1545110381849CEchFDp_5f1B23163136370F2CC");
    }

    public static void Setpostid(String postid) {
        prefEditor.putString("postid", postid).commit();
    }

    public static String Getpostid() {
        return preferences.getString("postid", "");
    }

    public static void Setcountno(String countno) {
        prefEditor.putString("countno", countno).commit();
    }

    public static String Getcountno() {
        return preferences.getString("countno", "");
    }

    public static void SettargetUid(String targetUid) {
        prefEditor.putString("targetUid", targetUid).commit();
    }

    public static String GettargetUid() {
        return preferences.getString("targetUid", "");
    }

    public static void SetpassCode(String passCode) {
        prefEditor.putString("passCode", passCode).commit();
    }

    public static String GetpassCode() {
        return preferences.getString("passCode", "");
    }

    public static void SetphoneNo(String phoneNo) {
        prefEditor.putString("phoneNo", phoneNo).commit();
    }

    public static String GetphoneNo() {
        return preferences.getString("phoneNo", "");
    }

    public static void SetmovieDesc(String movieDesc) {
        prefEditor.putString("movieDesc", movieDesc).commit();
    }

    public static String GetmovieDesc() {
        return preferences.getString("movieDesc", "");
    }

    public static void SetmovieDFile(String movieDFile) {
        prefEditor.putString("movieDFile", movieDFile).commit();
    }

    public static String GetmovieDFile() {
        return preferences.getString("movieDFile", "");
    }

    public static String Getlastservicename() {
        return preferences.getString("lastnam", ".000webhostapp.com/");
    }

    public static void Setuser_id(String user_id) {
        prefEditor.putString("user_id", user_id).commit();
    }

    public static String getEula() {
        return preferences.getString("eula", "");
    }

    public static void Setsession_id(String session_id) {
        prefEditor.putString("session_id", session_id).commit();
    }

    public static String Getsession_id() {
        return preferences.getString("session_id", "");
    }

    public static String Getuser_id() {
        return preferences.getString("user_id", "");
    }

    public static void SetMStatus(Boolean status) {
        prefEditor.putBoolean("Mstatus", status).commit();
    }

    public static Boolean GetMStatus() {
        return preferences.getBoolean("Mstatus", false);
    }

    public static void SetloopStatus(Boolean status) {
        prefEditor.putBoolean("loopstatus", status).commit();
    }

    public static Boolean GetloopStatus() {
        return preferences.getBoolean("loopstatus", false);
    }

    public static void SetIStatus(Boolean status) {
        prefEditor.putBoolean("loopstatus", status).commit();
    }

    public static Boolean GetIStatus() {
        return preferences.getBoolean("loopstatus", false);
    }

    public static void SetValid(Boolean valid) {
        prefEditor.putBoolean("valid", valid).commit();
    }

    public static Boolean GetValid() {
        return preferences.getBoolean("valid", false);
    }

    public static void SetCount(int count) {
        prefEditor.putInt("loopcount", count).commit();
    }

    public static int GetCount() {
        return preferences.getInt("loopcount", 0);
    }

    public static void SetBackCount(int count) {
        prefEditor.putInt("backcount", count).commit();
    }

    public static int GetBackCount() {
        return preferences.getInt("backcount", 0);
    }

    public static void SetloopStartStatus(Boolean status) {
        prefEditor.putBoolean("startloopstatus", status).commit();
    }

    public static Boolean GetloopStartStatus() {
        return preferences.getBoolean("startloopstatus", false);
    }

    public static void Setgroup(Boolean group) {
        prefEditor.putBoolean("group", group).commit();
    }

    public static Boolean Getgroup() {
        return preferences.getBoolean("group", false);
    }

    public static void Setgroups(Boolean groups) {
        prefEditor.putBoolean("groups", groups).commit();
    }

    public static Boolean Getgroups() {
        return preferences.getBoolean("groups", false);
    }

    public static void SetStatus(Boolean Status) {
        prefEditor.putBoolean("Status", Status).commit();
    }

    public static Boolean GetStatus() {
        return preferences.getBoolean("Status", false);
    }

    public static void SetPhoneStatus(Boolean PhoneStatus) {
        prefEditor.putBoolean("PhoneStatus", PhoneStatus).commit();
    }

    public static Boolean GetPhoneStatus() {
        return preferences.getBoolean("PhoneStatus", false);
    }

    public static void SetTextEvent(Boolean TextEvent) {
        prefEditor.putBoolean("TextEvent", TextEvent).commit();
    }

    public static Boolean GetTextEvent() {
        return preferences.getBoolean("TextEvent", false);
    }

    public static void Setsingle(Boolean single) {
        prefEditor.putBoolean("single", single).commit();
    }

    public static Boolean Getsingle() {
        return preferences.getBoolean("single", false);
    }

    public static int getNum1() {
        return preferences.getInt("num1", 5);
    }

    public static void setNum1(int num1) {
        prefEditor.putInt("num1", num1).commit();
    }

    public static int getNum2() {
        return preferences.getInt("num2", 10);
    }

    public static void setNum2(int num2) {
        prefEditor.putInt("num2", num2).commit();
    }

//    public static void setUserList(ArrayList<String> myList, String id) {
//        Gson gson = new Gson();
//        String list = gson.toJson(myList);
//        prefEditor.putString(id, list).commit();
//    }
//
//    public static ArrayList<String> getUserList(String id) {
//
//        Gson gson = new Gson();
//        String my = preferences.getString(id, "");
//        ArrayList<String> listItems = gson.fromJson(my, new TypeToken<ArrayList<String>>() {
//        }.getType());
//
//        return listItems;
//    }

    public static void Setapkversion(Boolean status) {
        prefEditor.putBoolean("apkversion", status).commit();
    }

//    public static void setPhoneList(ArrayList<String> myList) {
//        Gson gson = new Gson();
//        String list = gson.toJson(myList);
//        prefEditor.putString("myp", list).commit();
//    }
//
//    public static ArrayList<String> getPhoneList() {
//
//        Gson gson = new Gson();
//        String my = preferences.getString("myp", "");
//        ArrayList<String> listItems = gson.fromJson(my, new TypeToken<ArrayList<String>>() {
//        }.getType());
//
//        return listItems;
//    }

//    public static void setTeamTwoList(ArrayList<Players> myList) {
//        Gson gson = new Gson();
//        String list = gson.toJson(myList);
//        prefEditor.putString("mytwo", list).commit();
//    }
//
//    public static ArrayList<Players> getTeamTwoList() {
//
//        Gson gson = new Gson();
//        String my = preferences.getString("mytwo", "");
//        ArrayList<Players> listItems = gson.fromJson(my, new TypeToken<ArrayList<Players>>() {
//        }.getType());
//
//        return listItems;
//    }

    public static Boolean Getapkversion() {
        return preferences.getBoolean("apkversion", false);
    }

    public static void Setminsec(String status) {
        prefEditor.putString("selectdata", status).commit();
    }

    public static String Getminsec() {
        return preferences.getString("selectdata", "5 To 10 Sec");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("news", MODE_PRIVATE);
        prefEditor = preferences.edit();
        setupActivityListener();
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (PreferenceManager.GetValid()) {

                }

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;

            }

            @Override
            public void onActivityPaused(Activity activity) {
                activeActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public Activity getActiveActivity() {
        return activeActivity;
    }

}