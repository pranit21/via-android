package com.itvedant.listviewdemo1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Pranit on 17-01-2015.
 */
public class SingleListItem extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);

        TextView txtFood = (TextView) findViewById(R.id.food_label);

        Intent i = getIntent();
        String food = i.getStringExtra("food");
        txtFood.setText(food);
    }
}
