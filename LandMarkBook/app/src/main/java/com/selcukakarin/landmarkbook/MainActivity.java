package com.selcukakarin.landmarkbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //static Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=(ListView) findViewById(R.id.listView);
        final ArrayList<String> landMarkNames=new  ArrayList<String>();
        landMarkNames.add("Pisa");
        landMarkNames.add("Colloseum");
        landMarkNames.add("Eiffel");
        landMarkNames.add("London Bridge");

        Bitmap pisa=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.pisa);
        Bitmap colloseum=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.colloseum);
        Bitmap eiffel=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.eiffel);
        Bitmap londonBridge=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.londonbridge);

        final ArrayList<Bitmap> landMarkImages=new ArrayList<>();
        landMarkImages.add(pisa);
        landMarkImages.add(colloseum);
        landMarkImages.add(eiffel);
        landMarkImages.add(londonBridge);


        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,landMarkNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("name",landMarkNames.get(position));

                //selectedImage=landMarkImages.get(position);

                Bitmap bitmap=landMarkImages.get(position);

                Globals globals=Globals.getInstance();
                globals.setData(bitmap);

                startActivity(intent);

            }
        });


    }
}
