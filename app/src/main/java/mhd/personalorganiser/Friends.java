package mhd.personalorganiser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Mahdi on 6/09/2015.
 */
public class Friends extends Activity {

    DatabaseHandler mydb;

    Button btnDelete;
    Button btnCancel;

    ArrayList<String> arrayFriendsName = new ArrayList<>();
    ArrayList arrayFriendsID = new ArrayList();

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        list = findViewById(R.id.listViewFriends);

        mydb = new DatabaseHandler(this);

        Bundle mode = getIntent().getExtras();

        ArrayList arrayFriends = mydb.getAllFriends();

        for ( int i = 0; i < arrayFriends.size(); i++ ){

            String friendRecord = arrayFriends.get(i).toString();
            String[] friendDetail = friendRecord.split(",");
            String name = friendDetail[1];
            String id = friendDetail[0];
            arrayFriendsName.add(name);
            arrayFriendsID.add(id);
        }

        if (mode != null) {

            btnCancel.setEnabled(true);
            btnCancel.setVisibility(View.VISIBLE);
            btnDelete.setEnabled(true);
            btnDelete.setVisibility(View.VISIBLE);

            ArrayAdapter arrayAdapter =
                    new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, arrayFriendsName);

            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            list.setAdapter(arrayAdapter);
            list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        }
        else{

            btnCancel.setEnabled(false);
            btnCancel.setVisibility(View.INVISIBLE);

            btnDelete.setEnabled(false);
            btnDelete.setVisibility(View.INVISIBLE);

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayFriendsName);

            list = findViewById(R.id.listViewFriends);
            list.setAdapter(arrayAdapter);
            list.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    int id = Integer.parseInt(arrayFriendsID.get(position).toString());

                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", id);

                    Intent intent = new Intent(getApplicationContext(), DisplayFriend.class);

                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.addFriend:Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(),DisplayFriend.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;

            case R.id.menuEditFriends:
                Bundle editFriendsBundle = new Bundle();
                editFriendsBundle.putInt("mode", 1);

                Intent intentEdit = new Intent(getApplicationContext(), Friends.class);
                intentEdit.putExtras(editFriendsBundle);

                startActivity(intentEdit);
                return true;

            case R.id.menuEvents:
                Intent intentEvents = new Intent(getApplicationContext(), Events.class);
                startActivity(intentEvents);
                return true;

            case R.id.menuToDoList:
                Intent intentToDoList = new Intent(getApplicationContext(), ToDoList.class);
                startActivity(intentToDoList);
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

        Intent intent = new Intent(getApplicationContext(),Friends.class);
        startActivity(intent);
    }

    public void delete (View view){

        SparseBooleanArray checked = list.getCheckedItemPositions();

        for ( int i = 0; i < arrayFriendsID.size(); i++ ){

            if ( checked.get(i)) {

                String friend_id =  arrayFriendsID.get(i).toString();
                int id = Integer.parseInt(friend_id);
                mydb.deleteFriend(id);
            }
        }

        Intent intent = new Intent(getApplicationContext(),Friends.class);
        startActivity(intent);
    }
}
