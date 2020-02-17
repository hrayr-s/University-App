package com.example.hrayr.polytech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.hrayr.polytech.dummy.DummyContent;
import com.example.hrayr.polytech.dummy.Student;
import com.example.hrayr.polytech.dummy.WorkDummyContent;

import java.util.List;

/**
 * An activity representing a list of works. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link workDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class workListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    WorkDummyContent workcontent;
    public RecyclerView rcView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        View recyclerView = findViewById(R.id.work_list);
        assert recyclerView != null;
        String stud = Student.load(this);
        if(stud == "false") {
            workcontent = new WorkDummyContent("", false);
        }else{
            workcontent = new WorkDummyContent(stud, true);
        }
        rcView = (RecyclerView) recyclerView;
        setupRecyclerView();
        if (findViewById(R.id.work_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView() {
        //final SimpleItemRecyclerViewAdapter ad =
        rcView.setAdapter(new SimpleItemRecyclerViewAdapter(workcontent.ITEMS));
        Thread timer = new Thread() { //new thread
            public void run() {
                do {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (rcView) {
                                if (workcontent.Done) {
                                    rcView.swapAdapter(new SimpleItemRecyclerViewAdapter(workcontent.ITEMS), false);
                                    rcView.notifyAll();
                                }
                            }
                        }
                    });
                }
                while (!workcontent.Done);

            }
        };
        timer.start();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<WorkDummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<WorkDummyContent.DummyItem> items) {
            mValues = items;
        }

        public void swap(List<WorkDummyContent.DummyItem> mValues){
            this.mValues.clear();
            this.mValues.addAll(mValues);
            notifyDataSetChanged();
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.work_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(workDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        workDetailFragment fragment = new workDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.work_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, workDetailActivity.class);
                        intent.putExtra(workDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public WorkDummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
