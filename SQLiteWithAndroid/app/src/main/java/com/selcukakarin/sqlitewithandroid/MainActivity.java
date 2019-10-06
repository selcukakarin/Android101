package com.selcukakarin.sqlitewithandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Musicians", MODE_PRIVATE, null);
            myDatabase.execSQL("create table if not exists musicians(name varchar,age int(2))");

            //myDatabase.execSQL("insert into musicians(name,age) values('James','50')");
            //myDatabase.execSQL("insert into musicians(name,age)values('Lars',55)");
            //myDatabase.execSQL("insert into musicians(name,age)values('Kirk',60)");
            //myDatabase.execSQL("insert into musicians(name,age)values('Rob',65)");

            //myDatabase.execSQL("delete from musicians where name like 'J%'");

            myDatabase.execSQL("update musicians set age=56 where name='Lars'");

            Cursor cursor=myDatabase.rawQuery("select * from musicians",null);
            //Cursor cursor=myDatabase.rawQuery("Select * from musicians where age<59 and name='Lars' and name like '%a%'",null);

            int nameIx=cursor.getColumnIndex("name");
            int ageIx=cursor.getColumnIndex("age");

            cursor.moveToFirst();

            while(cursor!=null){

                System.out.println("Name : "+cursor.getString(nameIx));
                System.out.println("Age : "+cursor.getInt(ageIx));
                cursor.moveToNext();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
