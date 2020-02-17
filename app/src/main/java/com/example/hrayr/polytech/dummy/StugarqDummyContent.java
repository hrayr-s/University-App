package com.example.hrayr.polytech.dummy;

import android.widget.ArrayAdapter;

import com.example.hrayr.polytech.GetData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class StugarqDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public String[] names;

    public final String LOG_TAG = "myLogs";
    public ArrayAdapter<String> adapter;
    public Hashtable<Integer, Hashtable<String, String>> ExamDetailList;
    public Hashtable<String, String> Details;
    public Boolean Done;
    public StugarqDummyContent(String StudentId, Boolean StudentSigned) {
        Done = false;
        final GetData Data = new GetData(GetData.EXAM_LIST);
        Data.StudentSigned = StudentSigned;
        Data.StudentID = StudentId;
        Thread t = new Thread()
        {
            public void run() {
                Data.getExams(GetData.EXAM_LIST, 3);
                while(!Data.AllDataResived){
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                names = Data.ExamList;
                ExamDetailList = Data.ExamDetailList;
                // Add some sample items.
                for (int i = 1; i <= Data.ExamList.length; i++) {
                    String title, content, exid;
                    title = names[i-1];
                    addItem(createDummyItem(i, title, makeDetails(i-1)));
                }
                Done = true;
            }
        };
        t.start();
    }

    private String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        Details = ExamDetailList.get(position);
        builder.append(Details.get("type")).append(" քննություն\n");
        builder.append("Դասախոս` ").append(Details.get("lacturer"));
        builder.append("\nԱմսաթիվ` ").append(Details.get("date"));
        builder.append("\nԽումբ` ").append(Details.get("group"));
        builder.append("\nԱռարկա` ").append(Details.get("subject"));
        return builder.toString();
    }

    private void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private DummyItem createDummyItem(int position, String title, String Details) {
        return new DummyItem(String.valueOf(position), title, Details, "");
    }

    /**
     * A dummy item representing a piece of content.
     */
    public class DummyItem {
        public final String id;
        public final String content;
        public final String details;
        public final String name;

        public DummyItem(String id, String content, String details, String name) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.name = name;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
