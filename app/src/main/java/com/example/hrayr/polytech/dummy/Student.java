package com.example.hrayr.polytech.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hrayr on 27.04.2017.
 */

public class Student {
    public static final String studentID = "student_id";
    public static final String sharedPrefFile = "Student";
    public static final String withoutID = "no_id";

    static SharedPreferences sPref;

    public static Boolean check(Context context) {
        sPref = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        return sPref.contains(studentID) || sPref.contains(withoutID);
    }

    //public static Boolean checkNoId(Context context) {}

    public static String load(Context context) {
        sPref = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String savedText;
        Log.d("Student: ", sPref.getString(studentID, ""));
        if (sPref.getString(withoutID, "").equals("false"))
            savedText = sPref.getString(studentID, "");
        else
            savedText = "false";
        return savedText;
    }

    public static void save(Context context, String Id) {
        sPref = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(studentID, Id.toString());
        ed.putString(withoutID, "false");
        ed.commit();
        //Toast.makeText(MainActivity.this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    public static void NoId(Context context) {
        sPref = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(withoutID, "true");
        ed.commit();
    }

    public static String FullName(Context context, Boolean save, String name) {
        sPref = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        if(save) {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString("student_fullname", name);
            ed.commit();
            return name;
        }else{
            String savedText;
            //Log.d("Student: ", sPref.getString(studentID, ""));
            if (sPref.getString(withoutID, "").equals("false"))
                savedText = sPref.getString("student_fullname", "");
            else
                savedText = "false";
            return savedText;
        }
    }

}
