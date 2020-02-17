package com.example.hrayr.polytech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by hrayr on 25.04.2017.
 */

public class MainNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static AppCompatActivity as = null;

    MainNavigation(AppCompatActivity as) {
        MainNavigation.as = as;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_dasacucak) {
            intent = new Intent(as, Dasacucak.class);
        } else if (id == R.id.nav_exams) {
            intent = new Intent(as, Exams.class);
        } else if (id == R.id.nav_stugarqner) {
            Log.d("myLog", "Stugarqner");
        } else if (id == R.id.nav_works) {
            Log.d("myLog", "Works");
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
