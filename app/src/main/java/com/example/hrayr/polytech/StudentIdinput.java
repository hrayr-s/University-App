package com.example.hrayr.polytech;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hrayr.polytech.dummy.Student;

public class StudentIdinput extends AppCompatActivity implements View.OnClickListener {

    GetData data;
    EditText etText;
    Button button_continuewithoutid, button_addstudentid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_idinput);
        etText = (EditText)findViewById(R.id.input_studentid);

        button_continuewithoutid = (Button)findViewById(R.id.button_continuewithoutid);
        button_continuewithoutid.setOnClickListener(this);
        button_addstudentid = (Button)findViewById(R.id.button_addstudentid);
        button_addstudentid.setOnClickListener(this);
        data = new GetData(GetData.STUDENT_EXIST);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.button_continuewithoutid:
                Student.NoId(v.getContext());
                break;
            case R.id.button_addstudentid:
                data.Student(etText.getText().toString());
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        do {
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(data.AllDataResived) {
                                        synchronized (v) {
                                            if (data.StudentExist) {
                                                Student.save(v.getContext(), data.id);
                                                Student.FullName(v.getContext(), true, data.dict.get("fullname").toString());
                                                finish();
                                            } else {
                                                Snackbar.make(v, "Սխալ ID։ Փորձեք նորից։", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                            Log.d("Student", data.id);
                                            Log.d("Student", Student.load(v.getContext()));
                                        }
                                    }
                                }
                            });
                        }
                        while (!data.AllDataResived);
                    }
                }.start();
                break;
            default:
                break;
        }


    }
}
