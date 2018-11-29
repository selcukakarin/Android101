package com.example.selcuk.travelbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> names=new ArrayList<String>();
    static ArrayList<LatLng> locations=new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.add_place){
            //intent
            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=findViewById(R.id.listView);

        try{
            //SQLite database'imizden  program içerisinde kullanamabilmek için verilerimizi çekiyoruz
            MapsActivity.database=this.openOrCreateDatabase("Places",MODE_PRIVATE,null);
            Cursor cursor=MapsActivity.database.rawQuery("select * from places",null);
            int nameIx=cursor.getColumnIndex("name");
            int latitudeIx=cursor.getColumnIndex("latitude");
            int longitudeIx=cursor.getColumnIndex("longitude");

            //veritabanındaki satırları taramak için cursor kullanıyoruz ve arrayListlerimize veritabanındaki bilgileri atıyoruz
            while(cursor.moveToNext()){
                String nameFromDatabase=cursor.getString(nameIx);
                String latitudeFromDatebase=cursor.getString(latitudeIx);
                String longitudeFromDatabase=cursor.getString(longitudeIx);

                names.add(nameFromDatabase);

                Double l1=Double.parseDouble(latitudeFromDatebase);
                Double l2=Double.parseDouble(longitudeFromDatabase);

                LatLng locationFromDatabase=new LatLng(l1,l2);

                locations.add(locationFromDatabase);
            }

            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        //veritabanımızdaki verileri listview'ımızda gösteriyoruz
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(arrayAdapter);

        //listView'da bir adrese tıklanırsa info ve position bilgilerini MapsActivity'ye gönderiyoruz
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


    }
}
