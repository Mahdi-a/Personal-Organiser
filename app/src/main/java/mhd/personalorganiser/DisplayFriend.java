package mhd.personalorganiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
 * Created by Mahdi on 7/09/2015.
 */
public class DisplayFriend extends Activity {

    private DatabaseHandler mydb;

    private Spinner spinnerGender;

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtAge;
    private EditText txtStreet;
    private EditText txtSuburb;
    private EditText txtState;
    private EditText txtCountry;

    private int idToUpdate = 0;

    private Button btnSave;
    private Button btnShowOnMap;

    private String completeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_friend);


        String[] genderValue = {"Male", "Female"};
        ArrayAdapter<String> stringArrayAdapter =
                new ArrayAdapter<>
                        (this, android.R.layout.simple_spinner_dropdown_item, genderValue);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerGender.setAdapter(stringArrayAdapter);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        spinnerGender = findViewById(R.id.spinnerGender);
        txtAge = findViewById(R.id.txtAge);
        txtStreet = findViewById(R.id.txtStreet);
        txtSuburb = findViewById(R.id.txtSuburb);
        txtState = findViewById(R.id.txtState);
        txtCountry = findViewById(R.id.txtCountry);

        btnShowOnMap = findViewById(R.id.btnShowOnMap);
        btnSave = findViewById(R.id.btnSave);

        btnShowOnMap.setVisibility(View.INVISIBLE);

        mydb = new DatabaseHandler(this);

        Bundle extras = getIntent().getExtras();

        if ( extras != null ){

            int Value = extras.getInt("id");

            if ( Value > 0 ){

                Cursor rs = mydb.getData(Value, "tblFriend", "friendID");
                idToUpdate = Value;
                rs.moveToFirst();

                String fname =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_FRIEND_FN));

                String lname =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_FRIEND_LN));

                String gen =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_FRIEND_GENDER));

                String age =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_FRIEND_AGE));

                String add =
                        rs.getString(rs.getColumnIndex(DatabaseHandler.TABLE_FRIEND_ADDRESS));

                String[] address = add.split(",");

                String street = address[0];
                String suburb = address[1];
                String state = address[2];
                String country = address[3];

                if (!rs.isClosed()){
                    rs.close();
                }


                btnShowOnMap.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);

                txtFirstName.setText(fname);
                txtFirstName.setFocusable(false);
                txtFirstName.setClickable(false);

                txtLastName.setText(lname);
                txtLastName.setFocusable(false);
                txtLastName.setClickable(false);

                int genderIndex;

                if (gen.equals("Male")){
                    genderIndex = 0;
                }
                else{
                    genderIndex = 1;
                }

                spinnerGender.setSelection(genderIndex);
                spinnerGender.setFocusable(false);
                spinnerGender.setClickable(false);

                txtAge.setText(age);
                txtAge.setFocusable(false);
                txtAge.setClickable(false);

                txtStreet.setText(street);
                txtStreet.setFocusable(false);
                txtStreet.setClickable(false);

                txtSuburb.setText(suburb);
                txtSuburb.setFocusable(false);
                txtSuburb.setClickable(false);

                txtState.setText(state);
                txtState.setFocusable(false);
                txtState.setClickable(false);

                txtCountry.setText(country);
                txtCountry.setFocusable(false);
                txtCountry.setClickable(false);
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

                getMenuInflater().inflate(R.menu.menu_displayfriend, menu);
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

            case R.id.editFriend:

                btnShowOnMap.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);

                txtFirstName.setEnabled(true);
                txtFirstName.setFocusableInTouchMode(true);
                txtFirstName.setClickable(true);

                txtLastName.setEnabled(true);
                txtLastName.setFocusableInTouchMode(true);
                txtLastName.setClickable(true);

                spinnerGender.setEnabled(true);
                spinnerGender.setFocusableInTouchMode(true);
                spinnerGender.setClickable(true);

                txtAge.setEnabled(true);
                txtAge.setFocusableInTouchMode(true);
                txtAge.setClickable(true);

                txtStreet.setEnabled(true);
                txtStreet.setFocusableInTouchMode(true);
                txtStreet.setClickable(true);

                txtSuburb.setEnabled(true);
                txtSuburb.setFocusableInTouchMode(true);
                txtSuburb.setClickable(true);

                txtState.setEnabled(true);
                txtState.setFocusableInTouchMode(true);
                txtState.setClickable(true);

                txtCountry.setEnabled(true);
                txtCountry.setFocusableInTouchMode(true);
                txtCountry.setClickable(true);

                return true;

            case R.id.deleteFriend:

                String yes = "Yes";
                String no = "No";

                AlertDialog.Builder msgDelete = new AlertDialog.Builder(this);
                msgDelete.setMessage("Do you want to delete this friend?")
                        .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteFriend(idToUpdate);
                                Toast.makeText(getApplicationContext(), "Deleted successfully"
                                        , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Friends.class);
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

    public void run(View view) {

        getAddress();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            int Value = extras.getInt("id");
            if (Value > 0) {

                if (mydb.updateFriend(idToUpdate, txtFirstName.getText().toString(),
                        txtLastName.getText().toString(),
                        spinnerGender.getSelectedItem().toString(),
                        txtAge.getText().toString(), completeAddress)) {

                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Friends.class);
                    startActivity(intent);
                }
                else {

                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                if (mydb.insertFriend(txtFirstName.getText().toString(), txtLastName.getText().toString(),
                        spinnerGender.getSelectedItem().toString(), txtAge.getText().toString(),
                        completeAddress)) {

                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), Friends.class);
                startActivity(intent);
            }
        }
    }

    public void getAddress() {

        StringBuilder sbAddress = new StringBuilder();
        String separator = ", ";

        sbAddress.append(txtStreet.getText().toString()).append(separator);
        sbAddress.append(txtSuburb.getText().toString()).append(separator);
        sbAddress.append(txtState.getText().toString()).append(separator);
        sbAddress.append(txtCountry.getText().toString());

        completeAddress = sbAddress.toString();

    }

    public void showOnMap(View view){

        StringBuilder addressOnMap = new StringBuilder();
        String separator = "+";
        String preFix = "geo:0,0?q=";

        addressOnMap.append(preFix).append(separator);
        addressOnMap.append(txtStreet.getText().toString()).append(separator);
        addressOnMap.append(txtSuburb.getText().toString()).append(separator);
        addressOnMap.append(txtState.getText().toString()).append(separator);
        addressOnMap.append(txtCountry.getText().toString());

        String geoLocation = addressOnMap.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(geoLocation));
        startActivity(intent);
    }
}

