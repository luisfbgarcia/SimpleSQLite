package com.example.luis.simplesqlite;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating database and table
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
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

    public void onClickAdd(View view) {
        // Checking empty fields
        EditText editRollno = (EditText) findViewById(R.id.editRollno);
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editMarks = (EditText) findViewById(R.id.editMarks);

        if (editRollno.getText().toString().trim().length() == 0 ||
                editName.getText().toString().trim().length() == 0 ||
                editMarks.getText().toString().trim().length() == 0) {
            showMessage("Please enter all values");
            return;
        }
        // Inserting record
        db.execSQL("INSERT INTO student VALUES('" + editRollno.getText() + "','" + editName.getText() +
                "','" + editMarks.getText() + "');");
        showMessage("Record added");
    }

    public void onClickDelete(View view) {
        // Checking empty roll number
        EditText editRollno = (EditText) findViewById(R.id.editRollno);
        if (editRollno.getText().toString().trim().length() == 0) {
            showMessage("Please enter Rollno");
            return;
        }
        // Searching roll number
        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
        if (c.moveToFirst()) {
            // Deleting record if found
            db.execSQL("DELETE FROM student WHERE rollno='" + editRollno.getText() + "'");
            showMessage("Record Deleted");
        } else {
            showMessage("Invalid Rollno");
        }
    }

    public void onClickModify(View view) {
        // Checking empty roll number
        EditText editRollno = (EditText) findViewById(R.id.editRollno);
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editMarks = (EditText) findViewById(R.id.editMarks);

        if (editRollno.getText().toString().trim().length() == 0) {
            showMessage("Please enter Rollno");
            return;
        }
        // Searching roll number
        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
        if (c.moveToFirst()) {
            // Modifying record if found
            db.execSQL("UPDATE student SET name='" + editName.getText() + "',marks='" + editMarks.getText() +
                    "' WHERE rollno='" + editRollno.getText() + "'");
            showMessage("Record Modified");
        } else {
            showMessage("Invalid Rollno");
        }
    }

    public void onClickView(View view) {
        // Checking empty roll number
        EditText editRollno = (EditText) findViewById(R.id.editRollno);
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editMarks = (EditText) findViewById(R.id.editMarks);

        if (editRollno.getText().toString().trim().length() == 0) {
            showMessage("Please enter Rollno");
            return;
        }
        // Searching roll number
        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
        if (c.moveToFirst()) {
            // Displaying record if found
            editName.setText(c.getString(1));
            editMarks.setText(c.getString(2));
        } else {
            showMessage("Invalid Rollno");
        }
    }

    public void onClickViewAll(View view) {
        Cursor c = db.rawQuery("SELECT * FROM student", null);
        // Checking if no records found
        if (c.getCount() == 0) {
            showMessage("No records found");
            return;
        }
        // Appending records to a string buffer
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("Rollno: " + c.getString(0) + "\n");
            buffer.append("Name: " + c.getString(1) + "\n");
            buffer.append("Marks: " + c.getString(2) + "\n\n");
        }
        // Displaying all records
        showDialog(buffer.toString());
    }

    public void onClickShowInfo(View view) {
        showDialog("TWDP");
    }

    void showMessage(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
