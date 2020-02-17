package com.example.hrayr.polytech;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hrayr.polytech.dummy.Student;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!Student.check(this)){
            Intent intent = new Intent(this, StudentIdinput.class);
            if(intent != null)
                try {
                    startActivity(intent);
                } catch(java.lang.NullPointerException e){
                    Log.d("MyErrorLog: ", "Error at` " + e.toString());
                }
        }else{

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        TextView studname = (TextView)this.findViewById(R.id.nav_header_studentname);
        studname.setText(Student.FullName(this, false, null));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowDasajam(View v) {
    }
    public void ShowExams(View v) {
        Intent intent = new Intent(this, Exams.class);
        startActivity(intent);
    }
    public void ShowDasacucak(View v) {
        Intent intent = new Intent(this, Dasacucak.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_dasacucak) {
            intent = new Intent(this, Dasacucak.class);
        } else if (id == R.id.nav_exams) {
            intent = new Intent(this, ExamListActivity.class);
        } else if (id == R.id.nav_stugarqner) {
            intent = new Intent(this, StugarqListActivity.class);
        } else if (id == R.id.nav_works) {
            intent = new Intent(this, workListActivity.class);
        } else if (id == R.id.nav_examresults) {
            Log.d("myLog", "ExamResults");
        } else if (id == R.id.nav_myinfo) {
            Log.d("myLog", "MyInfo");
        }
        if(intent != null)
            try {
                startActivity(intent);
            } catch(java.lang.NullPointerException e){
                Log.d("MyErrorLog: ", "Error at` " + e.toString());
            }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
