package com.benlewis.cbr2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase cbrDB;

    int targetStr;
    int targetAgi;
    int targetInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {

            deleteDatabase("cbrDB");

            cbrDB = this.openOrCreateDatabase("CBR", MODE_PRIVATE, null);

            cbrDB.execSQL("CREATE TABLE IF NOT EXISTS heroes " +
                    "(name VARCHAR, str DOUBLE(3), agi DOUBLE(3), int DOUBLE(3))");

            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Warrior', 5.0, 0.0, 0.0) ");

            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Rogue', 0.0, 5.0, 0.0) ");

            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Wizard', 0.0, 0.0, 5.0) ");

            Cursor c = cbrDB.rawQuery("SELECT * FROM heroes", null);

            int nameIndex = c.getColumnIndex("name");
            int strIndex = c.getColumnIndex("str");
            int agiIndex = c.getColumnIndex("agi");
            int intIndex = c.getColumnIndex("int");

            c.moveToFirst();

            if (c.moveToFirst()) {

                do {
                    Log.i("Results: ",
                    c.getString(nameIndex) + " Str:"  +
                    c.getInt(strIndex) + " Agi:" +
                    c.getInt(agiIndex) + " Int:" +
                    c.getInt(intIndex));

                    c.moveToNext();

                } while (c.moveToNext());

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
