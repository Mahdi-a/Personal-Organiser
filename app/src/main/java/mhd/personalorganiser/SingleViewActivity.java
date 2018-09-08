package mhd.personalorganiser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


/**
 * Created by Mahdi on 6/09/2015.
 */


public class SingleViewActivity extends Activity {

    private DatabaseHandler mydb = new DatabaseHandler(this);
    private int[] ids;
    private Bitmap[] imgs;
    private int idToDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleimage);

        // Get intent data
        Intent i = getIntent();

        //Get images Id
        ids = mydb.getImagesIds();

        //Get images
        imgs = mydb.getAllImages();

        // Selected image id
        int position = i.getExtras().getInt("id");

        idToDelete = ids[position];

        ImageView imageView = (ImageView) findViewById(R.id.singleImage);
        imageView.setImageBitmap(imgs[position]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_singleview, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.deleteImage:

                mydb.deleteImage(idToDelete);
                Intent intentGallery = new Intent(getApplicationContext(), ImageGallery.class);
                startActivity(intentGallery);
        }
        return true;
    }


}