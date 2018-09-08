package mhd.personalorganiser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Mahdi on 6/09/2015.
 */
public class ImageGallery extends Activity {

    private DatabaseHandler mydb = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagegallery);


        int numOfImgs = 0;

        numOfImgs = mydb.getNumberOfRows("tblPic");

        Bitmap[] arrayPics;
//        int[] arrayPicsID = new int[numOfImgs];

        arrayPics = mydb.getAllImages();
//        arrayPicsID = mydb.getImagesIds();

        ArrayList<Bitmap> arrayListImages = new ArrayList<Bitmap>();

        for (int i = 0; i < numOfImgs; i++){

            arrayListImages.add(i, arrayPics[i]);
        }

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this, arrayListImages));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Send intent to SingleViewActivity
                Intent i = new Intent(getApplicationContext(), SingleViewActivity.class);

                // Pass image index
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imagegallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {

            case R.id.menuAddImage:
                Intent intentAddImage = new Intent(getApplicationContext(), AddImage.class);
                startActivity(intentAddImage);
                return true;


            case R.id.menuEvents:
                Intent intentEvents = new Intent(getApplicationContext(), Events.class);
                startActivity(intentEvents);
                return true;

            case R.id.menuToDoList:
                Intent intentToDoList = new Intent(getApplicationContext(), ToDoList.class);
                startActivity(intentToDoList);
                return true;

            case R.id.menuFriends:
                Intent intenFriends = new Intent(getApplicationContext(), Friends.class);
                startActivity(intenFriends);
                return true;

            case R.id.menuHome:
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
