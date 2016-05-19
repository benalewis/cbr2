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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase cbrDB;

    double targetStr;
    double targetAgi;
    double targetIntel;

    double strWe;
    double agiWe;
    double intWe;

    int warCounter = 0;
    int rogCounter = 0;
    int wizCounter = 0;

    EditText strWeight;
    EditText agiWeight;
    EditText intelWeight;

    EditText strEdit;
    EditText agiEdit;
    EditText intelEdit;

    TextView resultText;

    Button goButton;

    ArrayList<Hero> heroList = new ArrayList<Hero>();

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

        strWeight = (EditText) findViewById(R.id.strWeight);
        agiWeight = (EditText) findViewById(R.id.agiWeight);
        intelWeight = (EditText) findViewById(R.id.intWeight);

        resultText = (TextView) findViewById(R.id.resultText);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                targetStr = Double.valueOf(strEdit.getText().toString());
                targetAgi = Double.valueOf(agiEdit.getText().toString());
                targetIntel = Double.valueOf(intelEdit.getText().toString());

                strWe = Double.valueOf(strWeight.getText().toString());
                agiWe = Double.valueOf(agiWeight.getText().toString());
                intWe = Double.valueOf(intelWeight.getText().toString());

                createDatabase(); getAnswer();
                 }

                catch (Exception e) {
                    e.printStackTrace();
                }
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

            double similarity = 0;
            double resultSimilarity = 100;

            deleteDatabase("CBR");

            cbrDB = this.openOrCreateDatabase("CBR", MODE_PRIVATE, null);

            cbrDB.execSQL("CREATE TABLE IF NOT EXISTS heroes " +
                    "(name VARCHAR, str DOUBLE(3), agi DOUBLE(3), int DOUBLE(3))");

            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Warrior', 5.0, 0.0, 0.0) ");
            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Warrior', 4.5, 0.5, 0.0) ");
            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Warrior', 4.0, 0.0, 1.0) ");

            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Rogue', 0.5, 3.5, 1.0) ");
            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Rogue', 0.0, 5.0, 0.0) ");
            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Rogue', 0.5, 4.0, 0.5) ");

            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Wizard', 0.0, 0.0, 5.0) ");
            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Wizard', 1.0, 0.0, 4.0) ");
            cbrDB.execSQL("INSERT INTO heroes (name, str, agi, int) VALUES ('Wizard', 0.0, 0.5, 4.5) ");

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

                    //Log.i("Test: ", name + " " + String.valueOf(


                    similarity = getSimilarity(str, targetStr, strWe,
                            agi, targetAgi, agiWe,
                            intel, targetIntel, intWe);

                    heroList.add(new Hero(name, similarity));

                } while (c.moveToNext());
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public double getSimilarity( double str, double strT, double strW,
                                 double agi, double agiT, double agiW,
                                 double intel, double intelT, double intW)
    {
        double total = 0;
        double a = (agi-agiT) * agiW;
        double b = (str-strT) * strW;
        double c = (intel-intelT) * intW;

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

    public void getAnswer() {

        Collections.sort(heroList, new Comparator<Hero>() {
            @Override
            public int compare(Hero lhs, Hero rhs) {
                return Double.compare(lhs.getSimilarity(), rhs.getSimilarity());
            }
        });

        Log.i("X", heroList.toString());

        for (int i = 0; i < 3; i++) {

            Log.i("Top " + (i+1) + ": ", heroList.get(i).getHero());

            switch (heroList.get(i).getHero()) {

                case "Rogue":
                    rogCounter++;
                    break;
                case "Warrior":
                    warCounter++;
                    break;
                case "Wizard":
                    wizCounter++;
                    break;
            }
        }

        resultText.setText(highestInt(warCounter, rogCounter, wizCounter));

        heroList.clear();
    }

    public String highestInt ( int a, int b, int c) {

        int largest = a; String result = "Warrior";

        if (b > largest) { largest = b; result = "Rogue"; }
        if (c > largest) { result = "Wizard"; }

        return result;
    }
}
