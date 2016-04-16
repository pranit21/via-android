package com.itvedant.todorestdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pranit on 27-03-2016.
 */
public class TodosAdapter extends BaseAdapter {
    private List<Todo> todoList;
    private Context context;
    private TextView textView;

    public TodosAdapter(Context context, List<Todo> todosList) {
        this.todoList = todosList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.todo_item, parent, false);

            textView = (TextView) convertView.findViewById(R.id.todo);
        }

        Todo todo = todoList.get(position);

        textView.setText(todo.getTodo());

        return convertView;
    }
}
