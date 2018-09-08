package mhd.personalorganiser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton imgBtnFriends = findViewById(R.id.imgBtnFriend);
        final ImageButton imgBtnTask = findViewById(R.id.imgBtnTodoList);
        final ImageButton imgBtnEvent = findViewById(R.id.imgBtnEvent);
        final ImageButton imgBtnGallery = findViewById(R.id.imgBtnGallery);

        final Intent friends = new Intent(getApplicationContext(), Friends.class);
        final Intent events = new Intent(getApplicationContext(), Events.class);
        final Intent tasks = new Intent(getApplicationContext(), ToDoList.class);
        final Intent gallery = new Intent(getApplicationContext(), ImageGallery.class);


        imgBtnFriends.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(friends);
            }
        });

        imgBtnEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(events);
            }
        });

        imgBtnTask.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(tasks);
            }
        });

        imgBtnGallery.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(gallery);
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

        switch (item.getItemId()){
            case R.id.menuFriends:
                Intent intentFriends = new Intent(getApplicationContext(), Friends.class);
                startActivity(intentFriends);
                return true;

            case R.id.menuToDoList:
                Intent intentToDoList = new Intent(getApplicationContext(), ToDoList.class);
                startActivity(intentToDoList);
                return true;

            case R.id.menuEvents:
                Intent intentEvents = new Intent(getApplicationContext(), Events.class);
                startActivity(intentEvents);
                return true;

            case R.id.menuGallery:
                Intent intentGallery = new Intent(getApplicationContext(), ImageGallery.class);
                startActivity(intentGallery);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}