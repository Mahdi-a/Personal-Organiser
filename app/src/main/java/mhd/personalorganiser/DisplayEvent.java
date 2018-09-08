package mhd.personalorganiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Mahdi on 13/09/2015.
 */
public class DisplayEvent extends Activity implements OnClickListener {

    private SimpleDateFormat eventDateFormatter;
    private SimpleDateFormat eventTimeFormatter;

    private DatabaseHandler mydb;
    private DatePickerDialog eventDatePickerDialog;

    private int hour;
    private int minute;

    private String eventDate;
    private String eventTime;

    private String dateAndTime;


    static final int TIME_DIALOG_ID = 999;

    EditText txtEventName;
    EditText txtEventDate;
    EditText txtEventTime;
    EditText txtEventLocation;

    int idToUpdate = 0;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_event);

        eventDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        eventTimeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

        txtEventName = findViewById(R.id.txtEventName);
        txtEventDate = findViewById(R.id.txtEventDate);
        txtEventTime = findViewById(R.id.txtEventTime);
        txtEventLocation = findViewById(R.id.txtEventLocation);

        mydb = new DatabaseHandler(this);

        Bundle extras = getIntent().getExtras();



        if (extras != null){

            int Value = extras.getInt("id");

            if (Value > 0){

                Cursor rs = mydb.getData(Value, "tblEvent", "eventID");

                idToUpdate = Value;

                rs.moveToFirst();

                String eName =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_EVENT_NAME));

                String eDate =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_EVENT_DATE_TIME));

                String eLocation =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_EVENT_LOCATION));

                String[] dateTime = eDate.split(" ");

                eventDate = dateTime[0];
                eventTime = dateTime[1];


                if (!rs.isClosed()){
                    rs.close();
                }

                Button save = findViewById(R.id.btnSave);
                save.setVisibility(View.INVISIBLE);

                txtEventName.setText(eName);
                txtEventName.setFocusable(false);
                txtEventName.setClickable(false);

                txtEventDate.setText(eventDate);
                txtEventDate.setFocusable(false);
                txtEventDate.setClickable(false);

                txtEventTime.setText(eventTime);
                txtEventTime.setFocusable(false);
                txtEventTime.setClickable(false);

                txtEventLocation.setText(eLocation);
                txtEventLocation.setFocusable(false);
                txtEventLocation.setClickable(false);

            }
        }
        if (txtEventName.isFocusable()){

            setDateField();
            addListenerOnEventTime();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                getMenuInflater().inflate(R.menu.menu_displayevent, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }

        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        this.id = id;
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.editEvent:

                Button save = findViewById(R.id.btnSave);
                save.setVisibility(View.VISIBLE);

                setDateField();
                addListenerOnEventTime();

                txtEventName.setEnabled(true);
                txtEventName.setFocusableInTouchMode(true);
                txtEventName.setClickable(true);

                txtEventDate.setEnabled(true);
                txtEventDate.setFocusableInTouchMode(true);
                txtEventDate.setClickable(true);

                txtEventTime.setEnabled(true);
                txtEventTime.setFocusableInTouchMode(true);
                txtEventTime.setClickable(true);

                txtEventLocation.setEnabled(true);
                txtEventLocation.setFocusableInTouchMode(true);
                txtEventLocation.setClickable(true);

                return true;

            case R.id.deleteEvent:

                String yes = "Yes";
                String no = "No";

                AlertDialog.Builder msgDelete = new AlertDialog.Builder(this);
                msgDelete.setMessage("Do you want to delete this task?")
                        .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteEvent(idToUpdate);
                                Toast.makeText(getApplicationContext(), "Deleted successfully"
                                        , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Events.class);
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

    public String getDateTime(){

        StringBuilder sbDateTime = new StringBuilder();
        String separator = " ";

        sbDateTime.append(txtEventDate.getText().toString()).append(separator);
        sbDateTime.append(txtEventTime.getText().toString());

        dateAndTime = sbDateTime.toString();

        return dateAndTime;
    }

    public void run(View view) {

        getDateTime();

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateEvent(idToUpdate, txtEventName.getText().toString(),
                        dateAndTime.toString(),
                        txtEventLocation.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Events.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(mydb.insertEvent(txtEventName.getText().toString(),
                        dateAndTime.toString(),
                        txtEventLocation.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), Events.class);
                startActivity(intent);
            }
        }
    }

    private void setDateField() {

        txtEventDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        eventDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtEventDate.setText(eventDateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {

        final AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setPositiveButton("OK", null);

        if (view == txtEventDate){

            eventDatePickerDialog.show();
        }
        if (view == txtEventTime){

            showDialog(TIME_DIALOG_ID);
        }
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =

            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    txtEventTime.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void addListenerOnEventTime() {

        txtEventTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }
}

