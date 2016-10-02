package com.myhackathonproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListDetectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.titleColor));
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String itemsStr = intent.getStringExtra("DETECTED_ITEMS");
        Log.i("itemsStr: ", itemsStr);
        String[] itemsArr = itemsStr.split(";");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, itemsArr);

        ListView listView = (ListView) findViewById(R.id.items_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String KEY = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent(ListDetectionsActivity.this, DetailsActivity.class);
                intent.putExtra("SEARCH_KEY", KEY);
                startActivity(intent);
            }
        });
    }
}
