package com.benlewis.cbr2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase cbrDB;

    double targetStr;
    double targetAgi;
    double targetIntel;

    EditText strEdit;
    EditText agiEdit;
    EditText intelEdit;

    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goButton = (Button) findViewById(R.id.goButton);
        strEdit = (EditText) findViewById(R.id.strEdit);
        agiEdit = (EditText) findViewById(R.id.agiEdit);
        intelEdit = (EditText) findViewById(R.id.intEdit);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               targetStr = Double.valueOf(strEdit.getText().toString());
               targetAgi = Double.valueOf(agiEdit.getText().toString());
                targetIntel = Double.valueOf(intelEdit.getText().toString());

                createDatabase();
            }
        });
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

    public void createDatabase() {
        try {

            deleteDatabase("CBR");

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
            int intelIndex = c.getColumnIndex("int");

            c.moveToFirst();

            if (c.moveToFirst()) {

                do {

                    String name = c.getString(nameIndex);
                    double str = c.getDouble(strIndex);
                    double agi = c.getDouble(agiIndex);
                    double intel = c.getDouble(intelIndex);

                    Log.i("Test: ", name + String.valueOf(getSimilarity(str, targetStr, agi, targetAgi, intel, targetIntel)));

                } while (c.moveToNext());

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public double getSimilarity( double str, double strT, double agi, double agiT, double intel, double intelT)
    {
        double total = 0;
        double a = (agi-agiT);
        double b = (str-strT);
        double c = (intel-intelT);

        if (a < 0) {
            total += toPositive(a);
        } else {
            total += a;
        }

        if (b < 0) {
            total += toPositive(b);
        } else {
            total += b;
        }

        if (c < 0) {
            total += toPositive(c);
        } else {
            total += c;
        }

        return total;
    }

    public double toPositive (double x) {
        return (x * -1.0);
    }
}
