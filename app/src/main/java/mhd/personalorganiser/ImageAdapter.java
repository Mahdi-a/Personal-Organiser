package mhd.personalorganiser;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Mahdi on 6/09/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Bitmap> arrayListImages = new ArrayList<Bitmap>();

    // Constructor
    public ImageAdapter(Context c, ArrayList<Bitmap> arrayListImages) {
        mContext = c;
        this.arrayListImages = arrayListImages;
    }

    public int getCount() {
        return arrayListImages.size();
    }

    public Object getItem(int position) {
        return arrayListImages.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        imageView = new ImageView(mContext);
        imageView.setImageBitmap(arrayListImages.get(position));
        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(1, 1, 1, 1);

        return imageView;
    }

}