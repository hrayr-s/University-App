package com.example.hrayr.polytech;

import android.os.AsyncTask;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.hrayr.polytech.dummy.ExamDummyContent;
import com.example.hrayr.polytech.dummy.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hrayr on 25.04.2017.
 */

public class GetData {

    public static final int EXAM_LIST = 10001;
    public static final int EXAM_DETAIL = 10002;
    public static final int EXAM_RESULT = 10003;
    public static final int DASACUCAK = 10004;
    public static final int WORK_LIST = 10005;
    public static final int WORK_DETAIL = 10006;
    public static final int STUDENT_EXIST = 10007;
    public static final int STUDENT_INFO = 10008;

    public MyTask mtsk;
    public Hashtable<String, String>  dict = new Hashtable<String, String> ();
    public Hashtable<Integer, String>  Exlist = new Hashtable<Integer, String> ();
    public Hashtable<Integer, Hashtable<String, String>> ExamDetailList = new Hashtable<Integer, Hashtable<String, String>>(),
            WorkDetailList = new Hashtable<Integer, Hashtable<String, String>>();
    public String[] ExamList, WorkList;
    public Boolean AllDataResived = false,
                    StudentExist = false;
    public String id;
    public String StudentID;
    public Boolean StudentSigned = false;
    public String RequestURI = "";
    public static String run(String url) throws IOException {
        OkHttpClient client;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.header("Content-Type").equals("application/json"))
                return response.body().string();
            else
                return "{\"error\": \"None Json returned\"}";
        }
    }

    public GetData(int what_exacly) {
        switch (what_exacly) {
            default:
                break;
        }
    }

    public void getExams(int type) {
        this.RequestURI = "http://81.16.9.113:61080/external/exams.json";
        if(this.StudentSigned) {
            this.RequestURI += "?student="+this.StudentID;
        }
        mtsk = new MyTask(type);
        mtsk.execute();
    }


    public void getExams(int type, int eType) {
        this.RequestURI = "http://81.16.9.113:61080/external/exams.json?type=" + eType;
        if(this.StudentSigned) {
            this.RequestURI += "&student="+this.StudentID;
        }
        mtsk = new MyTask(type);
        mtsk.execute();
    }

    public void getExams(int type, String id) {
        mtsk = new MyTask(type, id);
        mtsk.execute();
        this.id = id;
    }
    public Boolean Student(String id) {
        mtsk = new MyTask(STUDENT_EXIST, id);
        mtsk.execute();
        this.id = id;
        return true;
    }

    public Hashtable<String, String> Student() {
        this.RequestURI = "http://81.16.9.113:61080/external/studentlist.json?id=" + this.id;
        mtsk = new MyTask(STUDENT_INFO, this.id);
        mtsk.execute();
        return this.dict;
    }

    public void getWorks(int type) {
        this.RequestURI = "http://81.16.9.113:61080/external/works.json";
        if(this.StudentSigned) {
            this.RequestURI += "?student="+this.StudentID;
        }
        mtsk = new MyTask(type);
        mtsk.execute();
    }

    static class Exams {

        public static String[] getExamList(MyTask m, String RequestURI) {
            String[] list;
            JSONObject dataJsonObj = null;

            Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
            Hashtable<String, String> Details;
            Hashtable<Integer, Hashtable<String, String>> ExamDetailList = new Hashtable<Integer, Hashtable<String, String>>();

            try {
                String strJson = run(RequestURI);

                dataJsonObj = new JSONObject(strJson);
                if (dataJsonObj.has("error"))
                    list = new String[0];
                else{
                    JSONArray friends = dataJsonObj.getJSONArray("exams");
                    list = new String[friends.length()];
                    for (int i = 0; i < friends.length(); i++) {
                        Details = new Hashtable<String, String>();
                        JSONObject friend = friends.getJSONObject(i);
                        String date = friend.getString("date");
                        String id = friend.getString("id");
                        String group = friend.getString("group");
                        String subject = friend.getString("subject");
                        String type = friend.getString("type");
                        Details.put("lacturer", friend.getString("lacturer"));
                        Details.put("date", friend.getString("date"));
                        Details.put("group", friend.getString("group"));
                        Details.put("subject", friend.getString("subject"));
                        Details.put("type", friend.getString("type"));
                        Details.put("id", id);
                        list[i] = subject + " " + group + " " + date + " " + type;
                        hashtable.put( i, id);
                        ExamDetailList.put(i, Details);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                list = new String[0];
            } catch  (IOException e) {
                e.printStackTrace();
                list = new String[0];
            }
            m.list = hashtable;
            m.DetailList = ExamDetailList;
            return list;
        }

        public static Hashtable<String, String> getExamDetails(String id, MyTask m) {
            JSONObject dataJsonObj = null;

            Hashtable<String, String> list = new Hashtable<String, String>();

            try {
                String strJson = run("http://81.16.9.113:61080/external/exams.json/" + id);
                dataJsonObj = new JSONObject(strJson);
                JSONArray friends = dataJsonObj.getJSONArray("exam");

                JSONObject friend = friends.getJSONObject(0);
                list.put("lacturer", friend.getString("lacturer"));
                list.put("date", friend.getString("date"));
                list.put("group", friend.getString("group"));
                list.put("subject", friend.getString("subject"));
                list.put("type", friend.getString("type"));
                list.put("id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m.hashtable = list;
            return list;
        }

    }

    static class student {

        public static Boolean getStudent(String id, MyTask m) {
            JSONObject dataJsonObj = null;
            Boolean exist = false;
            try {
                String strJson = run("http://81.16.9.113:61080/external/student.json/" + id);
                dataJsonObj = new JSONObject(strJson);
                if(dataJsonObj.has("StudentExist")) {
                    Boolean StudExist = dataJsonObj.getBoolean("StudentExist");
                    if(StudExist)
                        exist = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return exist;
        }


        public static Hashtable<String, String> Details(String RequestURI, MyTask m) {
            JSONObject dataJsonObj = null;

            Hashtable<String, String> list = new Hashtable<String, String>();

            try {
                String strJson = run(RequestURI);
                dataJsonObj = new JSONObject(strJson);
                JSONArray friends = dataJsonObj.getJSONArray("details");

                JSONObject friend = friends.getJSONObject(0);
                list.put("firstname", friend.getString("firstname"));
                list.put("lastname", friend.getString("lastname"));
                list.put("group", friend.getString("group"));
                list.put("email", friend.getString("email"));
                list.put("id", friend.getString("id"));
                list.put("fullname", list.get("firstname") + " " + list.get("lastname"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m.hashtable = list;
            return list;
        }
    }

    static class Works {
        public static String[] List(MyTask m, String RequestURI) {
            String[] list;
            JSONObject dataJsonObj = null;

            Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
            Hashtable<String, String> Details;
            Hashtable<Integer, Hashtable<String, String>> DictDetailList = new Hashtable<Integer, Hashtable<String, String>>();

            try {
                String strJson = run(RequestURI);

                dataJsonObj = new JSONObject(strJson);
                if (dataJsonObj.has("error"))
                    list = new String[0];
                else{
                    /*
                    * group
                    * subject
                    * lacturer
                    * worktype
                    * second_type
                    * tokosavorum1
                    * tokosavorum2
                    * tokosavorum3
                    * pashtpanum
                    * semestr
                    * */

                    JSONArray friends = dataJsonObj.getJSONArray("works");
                    list = new String[friends.length()];
                    for (int i = 0; i < friends.length(); i++) {
                        Details = new Hashtable<String, String>();
                        JSONObject friend = friends.getJSONObject(i);
                        String tokosavorum1 = friend.getString("tokosavorum1");
                        String tokosavorum2 = friend.getString("tokosavorum2");
                        String tokosavorum3 = friend.getString("tokosavorum3");
                        String pashtpanum = friend.getString("pashtpanum");
                        String id = friend.getString("id");
                        String group = friend.getString("group");
                        String subject = friend.getString("subject");
                        String worktype = friend.getString("worktype");
                        String second_type = friend.getString("second_type");
                        String lacturer = friend.getString("lacturer");

                        Details.put("lacturer", lacturer);
                        Details.put("tokosavorum1", tokosavorum1);
                        Details.put("tokosavorum2", tokosavorum2);
                        Details.put("tokosavorum3", tokosavorum3);
                        Details.put("pashtpanum", pashtpanum);
                        Details.put("group", group);
                        Details.put("subject", subject);
                        Details.put("type", worktype + " " + second_type);
                        Details.put("id", id);
                        list[i] = worktype + " " + second_type + " " + subject + " առարկայից։ " + group + "՝ " + pashtpanum;
                        hashtable.put( i, id);
                        DictDetailList.put(i, Details);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                list = new String[0];
            } catch  (IOException e) {
                e.printStackTrace();
                list = new String[0];
            }
            m.list = hashtable;
            m.DetailList = DictDetailList;
            return list;
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        Hashtable<String, String>  hashtable = new Hashtable<String, String> ();
        Hashtable<Integer, String>  list = new Hashtable<Integer, String> ();
        Hashtable<Integer, Hashtable<String, String>> DetailList = new Hashtable<Integer, Hashtable<String, String>>();
        int type;
        String id;
        MyTask(int type) {
            this.type = type;
        }

        MyTask(int type, String id) {
            this.type = type;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... act) {
            switch(this.type) {
                case EXAM_LIST:
                    ExamList = Exams.getExamList(this, RequestURI);
                    break;
                case EXAM_DETAIL:
                    hashtable = Exams.getExamDetails(this.id, this);
                    break;
                case STUDENT_EXIST:
                    StudentExist = student.getStudent(this.id, this);
                    break;
                case STUDENT_INFO:
                    dict = student.Details(this.id, this);
                    break;
                case WORK_LIST:
                    WorkList = Works.List(this, RequestURI);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(this.type == EXAM_LIST || this.type == EXAM_DETAIL) {
                Exlist = this.list;
                dict = this.hashtable;
                ExamDetailList = this.DetailList;
            }
            if(this.type == WORK_LIST) {
                dict = this.hashtable;
                WorkDetailList = this.DetailList;
            }
            AllDataResived = true;
        }
    }
}
