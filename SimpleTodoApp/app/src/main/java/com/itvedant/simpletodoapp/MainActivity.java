package com.itvedant.simpletodoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etEnterTodo;
    private ListView todoList;
    private ArrayList<String> todos;
    private Button btnAdd;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEnterTodo = (EditText) findViewById(R.id.etEnterTodo);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        todoList = (ListView) findViewById(R.id.todoList);
        todos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todos);
        todoList.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = etEnterTodo.getText().toString();
                todos.add(todo);
                adapter.notifyDataSetChanged();
            }
        });

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todos.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}
