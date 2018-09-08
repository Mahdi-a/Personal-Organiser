package mhd.personalorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Mahdi on 6/09/2015.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AssignmentDatabase.db";

    private static final String TABLE_FRIEND = "tblFriend";
    private static final String TABLE_FRIEND_ID = "friendID";
    public static final String TABLE_FRIEND_FN = "friendFN";
    public static final String TABLE_FRIEND_LN = "friendLN";
    public static final String TABLE_FRIEND_GENDER = "friendGender";
    public static final String TABLE_FRIEND_AGE = "friendAge";
    public static final String TABLE_FRIEND_ADDRESS = "friendAddress";

    private static final String TABLE_TASK = "tblTask";
    private static final String TABLE_TASK_ID = "taskID";
    public static final String TABLE_TASK_NAME = "taskName";
    public static final String TABLE_TASK_LOCATION = "taskLocation";
    public static final String TABLE_TASK_STATUS = "taskStatus";

    private static final String TABLE_EVENT = "tblEvent";
    private static final String TABLE_EVENT_ID = "eventID";
    public static final String TABLE_EVENT_NAME = "eventName";
    public static final String TABLE_EVENT_DATE_TIME = "eventDateTime";
    public static final String TABLE_EVENT_LOCATION = "eventLocation";

    private static final String TABLE_PIC = "tblPic";
    private static final String TABLE_PIC_ID = "picID";
    private static final String TABLE_PIC_IMAGE = "picImage";



    public DatabaseHandler (Context context){

        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creating tables.
        db.execSQL(
                "create table tblFriend" +
                       "(friendID integer primary key, " +
                        "friendFN text, friendLN text, " +
                        "friendGender text, friendAge int, " +
                        "friendAddress text)"
        );

        db.execSQL(
                "create table tblTask" +
                        "(taskID integer primary key, " +
                        "taskName text, taskLocation text, " +
                        "taskStatus text)"
        );

        db.execSQL(
                "create table tblEvent" +
                        "(eventID integer primary key, " +
                        "eventName text, eventDateTime datetime, " +
                        "eventLocation text)"
        );

        db.execSQL(
                "create table tblPic (picID integer primary key, picImage blob)"
        );

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS tblFriend");
        db.execSQL("DROP TABLE IF EXISTS tblTask");
        db.execSQL("DROP TABLE IF EXISTS tblEvents");
        db.execSQL("DROP TABLE IF EXISTS tblPic");

        // Create tables again
        onCreate(db);
    }

    public boolean insertFriend
            (String fName, String lName, String gender, String age, String address){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_FRIEND_FN, fName);
        contentValues.put(TABLE_FRIEND_LN, lName);
        contentValues.put(TABLE_FRIEND_GENDER, gender);
        contentValues.put(TABLE_FRIEND_AGE, age);
        contentValues.put(TABLE_FRIEND_ADDRESS, address);

        database.insert(TABLE_FRIEND, null, contentValues);

        return true;
    }

    public boolean insertTask (String tName, String tLocation, String tStatus){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_TASK_NAME, tName);
        contentValues.put(TABLE_TASK_LOCATION, tLocation);
        contentValues.put(TABLE_TASK_STATUS, tStatus);

        database.insert(TABLE_TASK, null, contentValues);

        return true;
    }

    public boolean insertEvent (String eName, String eDate, String eLocation){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_EVENT_NAME, eName);
        contentValues.put(TABLE_EVENT_DATE_TIME, eDate);
        contentValues.put(TABLE_EVENT_LOCATION, eLocation);

        database.insert(TABLE_EVENT, null, contentValues);

        return true;
    }


    public Cursor getData (int id, String table, String tableID){

        SQLiteDatabase database = this.getReadableDatabase();

        return database.rawQuery("select * from "+ table +" where "+ tableID +"="+id+"", null);
    }

    public int getNumberOfRows( String tblName){
        SQLiteDatabase database = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(database, tblName );
    }

    public boolean updateFriend
            (Integer id, String fName, String lName, String gender, String age, String address){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_FRIEND_FN, fName);
        contentValues.put(TABLE_FRIEND_LN, lName);
        contentValues.put(TABLE_FRIEND_GENDER, gender);
        contentValues.put(TABLE_FRIEND_AGE, age);
        contentValues.put(TABLE_FRIEND_ADDRESS, address);

        database.update(TABLE_FRIEND, contentValues, "friendID = ?", new String[]{
                Integer.toString(id)
        });

        return true;
    }

    public boolean updateTask
            (Integer id, String tName, String tLocation, String tStatus){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_TASK_NAME, tName);
        contentValues.put(TABLE_TASK_LOCATION, tLocation);
        contentValues.put(TABLE_TASK_STATUS, tStatus);

        database.update(TABLE_TASK, contentValues, "taskID = ?", new String[]{
                Integer.toString(id)
        });

        return true;
    }

    public boolean updateEvent
            (Integer id, String eName, String eDate, String eLocation){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_EVENT_NAME, eName);
        contentValues.put(TABLE_EVENT_DATE_TIME, eDate);
        contentValues.put(TABLE_EVENT_LOCATION, eLocation);

        database.update(TABLE_EVENT, contentValues, "eventID = ?", new String[]{
                Integer.toString(id)
        });

        return true;
    }

    public Integer deleteFriend (Integer id){

        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete("tblFriend",
                "friendID = ?",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteTask (Integer id){

        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete("tblTask",
                "taskID = ?",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteEvent (Integer id){

        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete("tblEvent",
                "eventID = ?",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllFriends(){

        ArrayList<String> arrayFriends;
        arrayFriends = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        try (Cursor res = database.rawQuery("select * from tblFriend", null)) {
            res.moveToFirst();

            while (!res.isAfterLast()) {

                arrayFriends.add(res.getString(res.getColumnIndex(TABLE_FRIEND_ID)) + "," +
                        res.getString(res.getColumnIndex(TABLE_FRIEND_FN)) + " " +
                        res.getString(res.getColumnIndex(TABLE_FRIEND_LN)));

                res.moveToNext();
            }
        }

        return arrayFriends;
    }


    public ArrayList<String> filterTasks(String filter){

        ArrayList<String> arrayTasks = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        String[] status = {filter};

        if (filter.equals("All")){

            try (Cursor res = database.rawQuery("select * from tblTask", null)) {
                res.moveToFirst();

                while (!res.isAfterLast()) {

                    arrayTasks.add(res.getString(res.getColumnIndex(TABLE_TASK_ID)) + "," +
                            res.getString(res.getColumnIndex(TABLE_TASK_NAME)) + " - " +
                            res.getString(res.getColumnIndex(TABLE_TASK_STATUS)));

                    res.moveToNext();
                }
            }
        }
        else {
            try (Cursor res = database.rawQuery("select * from tblTask WHERE taskStatus =?", status)) {
                res.moveToFirst();

                while (!res.isAfterLast()) {

                    arrayTasks.add(res.getString(res.getColumnIndex(TABLE_TASK_ID)) + "," +
                            res.getString(res.getColumnIndex(TABLE_TASK_NAME)) + " - " +
                            res.getString(res.getColumnIndex(TABLE_TASK_STATUS)));

                    res.moveToNext();
                }
            }
        }

        return arrayTasks;
    }

    public ArrayList<String> filterEvents(String filter){

        SimpleDateFormat myDateFormat;

        ArrayList<String> arrayEvents = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Calendar myCalender = Calendar.getInstance();

        myDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);

        String todayDate = myDateFormat.format(myCalender.getTime());

        String[] date = {todayDate};

        if (filter.equals("All")){

            try (Cursor res = database.rawQuery("select * from tblEvent", null)) {
                res.moveToFirst();

                while (!res.isAfterLast()) {

                    arrayEvents.add(res.getString(res.getColumnIndex(TABLE_EVENT_ID)) + "," +
                            res.getString(res.getColumnIndex(TABLE_EVENT_NAME)) + " - " +
                            res.getString(res.getColumnIndex(TABLE_EVENT_DATE_TIME)));

                    res.moveToNext();
                }
            }
        }
        if (filter.equals("Future")) {
            try (Cursor res = database.rawQuery("select * from tblEvent WHERE eventDateTime >=?", date)) {
                res.moveToFirst();

                while (!res.isAfterLast()) {

                    arrayEvents.add(res.getString(res.getColumnIndex(TABLE_EVENT_ID)) + "," +
                            res.getString(res.getColumnIndex(TABLE_EVENT_NAME)) + " - " +
                            res.getString(res.getColumnIndex(TABLE_EVENT_DATE_TIME)));

                    res.moveToNext();
                }
            }
        }

        if (filter.equals("Past")) {
            try (Cursor res = database.rawQuery("select * from tblEvent WHERE eventDateTime <?", date)) {
                res.moveToFirst();

                while (!res.isAfterLast()) {

                    arrayEvents.add(res.getString(res.getColumnIndex(TABLE_EVENT_ID)) + "," +
                            res.getString(res.getColumnIndex(TABLE_EVENT_NAME)) + " - " +
                            res.getString(res.getColumnIndex(TABLE_EVENT_DATE_TIME)));

                    res.moveToNext();
                }
            }
        }

        return arrayEvents;
    }

    public void saveImage(Bitmap bitmap) {

        SQLiteDatabase database = this.getWritableDatabase();

        byte[] data = getBitmapAsByteArray(bitmap);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_PIC_IMAGE, data);

        database.insert(TABLE_PIC, null, contentValues);

    }

    public Bitmap[] getAllImages(){

        int numRows = getNumberOfRows("tblPic");
        Bitmap[] allImages = new Bitmap[numRows];

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor res = database.rawQuery("select * from tblPic", null);
        res.moveToFirst();

        for (int i = 0; i < numRows; i++){
            byte[] images = res.getBlob(1);
            allImages[i] = BitmapFactory.decodeByteArray(images, 0, images.length);
            res.moveToNext();
        }

        res.close();

        return allImages;
    }

    public int[] getImagesIds(){

        int numRows = getNumberOfRows("tblPic");
        int[] allImagesIds = new int[numRows];

        SQLiteDatabase database = this.getReadableDatabase();

        try (Cursor res = database.rawQuery("select * from tblPic", null)) {
            res.moveToFirst();

            for (int i = 0; i < numRows; i++) {
                allImagesIds[i] = res.getInt(res.getColumnIndex(TABLE_PIC_ID));
                res.moveToNext();
            }
        }

        return allImagesIds;
    }

    public Integer deleteImage (Integer id){

        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete("tblPic",
                "picID = ?",
                new String[] { Integer.toString(id) });
    }


    //Converting byte[] to an image file
    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);

        return outputStream.toByteArray();
    }

}
