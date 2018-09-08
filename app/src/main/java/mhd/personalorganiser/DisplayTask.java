package mhd.personalorganiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Mahdi on 10/09/2015.
 */
public class DisplayTask extends Activity {

    private DatabaseHandler mydb;
    EditText taskName;
    EditText taskLocation;
    Spinner taskStatus;

    int idToUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_task);

        String[] statusValue = {"Not Completed", "Completed"};

        ArrayAdapter<String> stringArrayAdapter =
                new ArrayAdapter<String>
                        (this, android.R.layout.simple_spinner_dropdown_item, statusValue);

        taskStatus = (Spinner) findViewById(R.id.spinnerStatus);
        taskStatus.setAdapter(stringArrayAdapter);

        taskName = (EditText) findViewById(R.id.txtTaskName);
        taskLocation = (EditText) findViewById(R.id.txtLocation);
        taskStatus = (Spinner) findViewById(R.id.spinnerStatus);

        mydb = new DatabaseHandler(this);

        Bundle extras = getIntent().getExtras();

        if ( extras != null ){

            int Value = extras.getInt("id");

            if ( Value > 0 ){

                Cursor rs = mydb.getData(Value, "tblTask", "taskID");
                idToUpdate = Value;
                rs.moveToFirst();

                String tName =
                        rs.getString(rs.getColumnIndex(mydb.TABLE_TASK_NAME));

                String tLocation =
                        rs.getString(rs.getColumnIndex(mydb.TABLE_TASK_LOCATION));

                String tStatus =
                        rs.getString(rs.getColumnIndex(mydb.TABLE_TASK_STATUS));

                if ( !rs.isClosed() ){

                    rs.close();
                }

                Button save = (Button) findViewById(R.id.btnSave);
                save.setVisibility(View.INVISIBLE);

                taskName.setText((CharSequence) tName);
                taskName.setFocusable(false);
                taskName.setClickable(false);

                taskLocation.setText((CharSequence) tLocation);
                taskLocation.setFocusable(false);
                taskLocation.setClickable(false);

                int statusIndex;

                if (tStatus.equals("Not Completed")){
                    statusIndex = 0;
                }
                else{
                    statusIndex = 1;
                }

                taskStatus.setSelection(statusIndex);
                taskStatus.setFocusable(false);
                taskStatus.setClickable(false);

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            int Value = extras.getInt("id");

            if (Value > 0) {

                getMenuInflater().inflate(R.menu.menu_displaytask, menu);
            }
            else {

                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.editTask:

                Button save = (Button) findViewById(R.id.btnSave);
                save.setVisibility(View.VISIBLE);

                taskName.setEnabled(true);
                taskName.setFocusableInTouchMode(true);
                taskName.setClickable(true);

                taskLocation.setEnabled(true);
                taskLocation.setFocusableInTouchMode(true);
                taskLocation.setClickable(true);

                taskStatus.setEnabled(true);
                taskStatus.setFocusableInTouchMode(true);
                taskStatus.setClickable(true);

                return true;


            case R.id.deleteTask:

                String yes = "Yes";
                String no = "No";

                AlertDialog.Builder msgDelete = new AlertDialog.Builder(this);
                msgDelete.setMessage("Do you want to delete this task?")
                        .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteTask(idToUpdate);
                                Toast.makeText(getApplicationContext(), "Deleted successfully"
                                        , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ToDoList.class);
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton(no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog deleteAlert = msgDelete.create();
                deleteAlert.setTitle("Are you sure");
                deleteAlert.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run(View view)
    {
        Bundle extras = getIntent().getExtras();
        if(extras !=null){

            int Value = extras.getInt("id");

            if(Value>0){

                if(mydb.updateTask(idToUpdate, taskName.getText().toString(),
                        taskLocation.getText().toString(),
                        taskStatus.getSelectedItem().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),ToDoList.class);
                    startActivity(intent);
                }
                else{

                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(mydb.insertTask(taskName.getText().toString(), taskLocation.getText().toString(),
                        taskStatus.getSelectedItem().toString())){

                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }

                else{

                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), ToDoList.class);
                startActivity(intent);
            }
        }
    }
}
