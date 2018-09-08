package mhd.personalorganiser;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Mahdi on 6/09/2015.
 */
public class ToDoList extends Activity {

    DatabaseHandler mydb;

    Button btnDelete;
    Button btnCancel;

    Spinner spinnerFilterTasks;


    ArrayList<String> arrayTasksName = new ArrayList<>();
    ArrayList arrayTasksID = new ArrayList();
    ArrayList arrayTasks;

    ListView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        final AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setPositiveButton("OK", null);

        String[] filter = {"All", "Not Completed", "Completed"};
        final ArrayAdapter<String > filterArrayAdapter;
        filterArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, filter);

        spinnerFilterTasks = findViewById(R.id.spinnerFilter);
        spinnerFilterTasks.setAdapter(filterArrayAdapter);

        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);

        taskList = findViewById(R.id.listViewTasks);

        mydb = new DatabaseHandler(this);

        Bundle mode = getIntent().getExtras();

        final ArrayAdapter arrayAdapter;

        if ( mode != null){
            arrayAdapter =
                    new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, arrayTasksName);
        }
        else {
        arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayTasksName);
        }

        spinnerFilterTasks.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg4) {

                int status = spinnerFilterTasks.getSelectedItemPosition();

                if (status == 0){
                    arrayAdapter.clear();
                    arrayTasksName.clear();
                    arrayTasksID.clear();
                    arrayTasks = mydb.filterTasks("All");
                    arrayAdapter.notifyDataSetChanged();
                }

                if (status == 1){
                    arrayAdapter.clear();
                    arrayTasksName.clear();
                    arrayTasksID.clear();
                    arrayTasks = mydb.filterTasks("Not Completed");
                    arrayAdapter.notifyDataSetChanged();
                }
                if (status == 2){
                    arrayAdapter.clear();
                    arrayTasksName.clear();
                    arrayTasksID.clear();
                    arrayTasks = mydb.filterTasks("Completed");
                    arrayAdapter.notifyDataSetChanged();
                }


                for ( int i = 0; i < arrayTasks.size(); i++ ){
                    String taskRecord = arrayTasks.get(i).toString();
                    String[] taskDetail = taskRecord.split(",");
                    String name = taskDetail[1];
                    String id = taskDetail[0];
                    arrayTasksName.add(name);
                    arrayTasksID.add(id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if ( mode != null ){
            btnCancel.setEnabled(true);
            btnCancel.setVisibility(View.VISIBLE);
            btnDelete.setEnabled(true);
            btnDelete.setVisibility(View.VISIBLE);

            taskList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            taskList.setAdapter(arrayAdapter);
            taskList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        }
        else{
            btnCancel.setEnabled(false);
            btnCancel.setVisibility(View.INVISIBLE);

            btnDelete.setEnabled(false);
            btnDelete.setVisibility(View.INVISIBLE);

            taskList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            taskList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

            taskList.setAdapter(arrayAdapter);
            taskList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    int id = Integer.parseInt(arrayTasksID.get(position).toString());

                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", id);

                    Intent intent = new Intent(getApplicationContext(), DisplayTask.class);

                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.newTask:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(), DisplayTask.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;

            case R.id.menuEditTaskList:
                Bundle edit = new Bundle();
                edit.putInt("mode", 2);
                Intent intentEdit = new Intent(getApplicationContext(), ToDoList.class);
                intentEdit.putExtras(edit);

                startActivity(intentEdit);
                return true;

            case R.id.menuFriends:
                Intent intentFriends = new Intent(getApplicationContext(), Friends.class);
                startActivity(intentFriends);
                return true;

            case R.id.menuEvents:
                Intent intentEvents = new Intent(getApplicationContext(), Events.class);
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

        Intent intent = new Intent(getApplicationContext(),ToDoList.class);
        startActivity(intent);
    }

    public void delete(View view){
        SparseBooleanArray checked = taskList.getCheckedItemPositions();

        for (int i = 0; i < arrayTasksID.size(); i++){
            if (checked.get(i)){
                String task_id = arrayTasksID.get(i).toString();
                int id = Integer.parseInt(task_id);
                mydb.deleteTask(id);
            }
        }
        Intent intent = new Intent(getApplicationContext(), ToDoList.class);
        startActivity(intent);
    }

}
