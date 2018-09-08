package mhd.personalorganiser;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


/**
 * Created by Mahdi on 16/09/2015.
 */
public class AddImage extends Activity implements OnClickListener {

    protected static ImageView imageView;
    protected Button getImage, saveImage;
    private String selectedImagePath;
    private static final int SELECT_PICTURE = 1;

    private DatabaseHandler mydb = new DatabaseHandler(this);

    Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_image);

        imageView = findViewById(R.id.imageView);

        getImage = findViewById(R.id.get_image);
        getImage.setOnClickListener(this);

        saveImage = findViewById(R.id.save_image);
        saveImage.setOnClickListener(this);

    }

    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.get_image:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        SELECT_PICTURE);
                uri = intent.getData();
                break;

            case R.id.save_image:

                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();

                mydb.saveImage(bitmap);
                imageView.destroyDrawingCache();

                Intent intentImageGallery = new Intent(getApplicationContext(), ImageGallery.class);
                startActivity(intentImageGallery);

                break;

            default:
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}