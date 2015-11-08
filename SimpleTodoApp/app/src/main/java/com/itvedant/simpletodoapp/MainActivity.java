package com.itvedant.simpletodoapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ListActivity {
    private TodoDBHelper todoDBHelper;
    private SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();
    }

    private void updateUI() {
        todoDBHelper = new TodoDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = todoDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TodoDBHelper.TABLE,
                new String[]{TodoDBHelper.Columns._ID, TodoDBHelper.Columns.TODO},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.todo_item,
                cursor,
                new String[] { TodoDBHelper.Columns.TODO},
                new int[] { R.id.todoTextView},
                0
        );
        this.setListAdapter(listAdapter);
    }

    public void deleteTodo(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.todoTextView);
        String todo = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TodoDBHelper.TABLE,
                TodoDBHelper.Columns.TODO,
                todo);

        todoDBHelper = new TodoDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = todoDBHelper.getWritableDatabase();
        sqlDB.execSQL(sql);

        updateUI();
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
        switch (item.getItemId()) {
            case R.id.add_todo:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a todo");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = inputField.getText().toString();

                        TodoDBHelper helper = new TodoDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TodoDBHelper.Columns.TODO, task);

                        db.insertWithOnConflict(TodoDBHelper.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);
                    }
                });

                builder.setNegativeButton("Cancel", null);

                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
