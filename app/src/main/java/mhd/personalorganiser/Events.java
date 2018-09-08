package mhd.personalorganiser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * Created by Mahdi on 6/09/2015.
 */
public class Events extends Activity {

    private DatabaseHandler mydb;

    private Button btnDelete;
    private Button btnCancel;

    private Spinner spinnerFilterEvents;

    private ArrayList<String > arrayEventsName = new ArrayList<String>();
    private ArrayList arrayEventID = new ArrayList();
    private ArrayList arrayEvents;

    private ListView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        String[] filter = {"All", "Past", "Future"};

        final ArrayAdapter<String> arrayAdapterFilter =
                new ArrayAdapter<String>
                        (this, android.R.layout.simple_spinner_dropdown_item, filter);

        spinnerFilterEvents = (Spinner) findViewById(R.id.spinnerFilterEvents);
        spinnerFilterEvents.setAdapter(arrayAdapterFilter);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDelete = (Button) findViewById(R.id.btnDeleteEvent);

        eventList = (ListView)findViewById(R.id.listViewEvents);

        mydb = new DatabaseHandler(this);

        Bundle mode = getIntent().getExtras();

        final ArrayAdapter arrayAdapterEvents;

        if ( mode != null){

            arrayAdapterEvents =
                    new ArrayAdapter
                            (this, android.R.layout.simple_list_item_multiple_choice, arrayEventsName);
        }
        else{

            arrayAdapterEvents =
                    new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayEventsName);
        }

        spinnerFilterEvents.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg4) {

                int status = spinnerFilterEvents.getSelectedItemPosition();

                if (status == 0){

                    arrayAdapterEvents.clear();
                    arrayEventsName.clear();
                    arrayEventID.clear();
                    arrayEvents = mydb.filterEvents("All");
                    arrayAdapterEvents.notifyDataSetChanged();
                }

                if (status == 1){

                    arrayAdapterEvents.clear();
                    arrayEventsName.clear();
                    arrayEventID.clear();
                    arrayEvents = mydb.filterEvents("Past");
                    arrayAdapterEvents.notifyDataSetChanged();
                }

                if (status == 2){

                    arrayAdapterEvents.clear();
                    arrayEventsName.clear();
                    arrayEventID.clear();
                    arrayEvents = mydb.filterEvents("Future");
                    arrayAdapterEvents.notifyDataSetChanged();
                }

                for ( int i = 0; i < arrayEvents.size(); i++ ){

                    String eventRecord = arrayEvents.get(i).toString();
                    String[] eventDetail = eventRecord.split(",");
                    String name = eventDetail[1];
                    String id = eventDetail[0];
                    arrayEventsName.add(name);
                    arrayEventID.add(id);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mode != null) {

            btnCancel.setEnabled(true);
            btnCancel.setVisibility(View.VISIBLE);

            btnDelete.setEnabled(true);
            btnDelete.setVisibility(View.VISIBLE);

            eventList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            eventList.setAdapter(arrayAdapterEvents);
            eventList.setChoiceMode(eventList.CHOICE_MODE_MULTIPLE);
        }
        else {

            btnCancel.setEnabled(false);
            btnCancel.setVisibility(View.INVISIBLE);

            btnDelete.setEnabled(false);
            btnDelete.setVisibility(View.INVISIBLE);

            eventList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            eventList.setChoiceMode(eventList.CHOICE_MODE_SINGLE);

            eventList.setAdapter(arrayAdapterEvents);

            eventList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    int id = Integer.parseInt(arrayEventID.get(position).toString());

                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", id);

                    Intent intent = new Intent(getApplicationContext(), DisplayEvent.class);

                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){

            case R.id.newEvent:

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(), DisplayEvent.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;

            case R.id.menuEditEventList:

                Bundle edit = new Bundle();
                edit.putInt("mode", 1);
                Intent intentEdit = new Intent(getApplicationContext(), Events.class);
                intentEdit.putExtras(edit);
                startActivity(intentEdit);

                return true;

            case R.id.menuFriends:

                Intent intentFriends = new Intent(getApplicationContext(), Friends.class);
                startActivity(intentFriends);

                return true;

            case R.id.menuToDoList:

                Intent intentEvents = new Intent(getApplicationContext(), ToDoList.class);
                startActivity(intentEvents);

                return true;

            case R.id.menuGallery:

                Intent intentGallery = new Intent(getApplicationContext(), ImageGallery.class);
                startActivity(intentGallery);

                return true;

            case R.id.menuHome:

                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {

            moveTaskToBack(true);
        }

        return super.onKeyDown(keycode, event);
    }

    public void cancel(View view){

        Intent intent = new Intent(getApplicationContext(),Events.class);
        startActivity(intent);
    }

    public void deleteEvent(View view){

        SparseBooleanArray checked = eventList.getCheckedItemPositions();

        for (int i = 0; i < arrayEventID.size(); i++){

            if (checked.get(i)){

                String event_id = arrayEventID.get(i).toString();
                int id = Integer.parseInt(event_id);
                mydb.deleteEvent(id);
            }
        }

        Intent intent = new Intent(getApplicationContext(), Events.class);
        startActivity(intent);
    }
}
