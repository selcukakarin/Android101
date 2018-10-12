package com.selcukakarin.basicofmethodandclasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);System.out.println(giveMeMyResult(5,10));
        makeSimpson();
    }

    public void writeTheText(){
        System.out.println("Hello World");
    }

    public int giveMeMyResult(int x,int y){
        return x+y;
    }

    public void makeMusician(){
        Musician james=new Musician("james hetfield","guitar",50);
    }

    public void makeSimpson(){
        Simpsons homer=new Simpsons("Homer Simpson",50,"Nuclear Reactor Chief");
        System.out.println(homer.getName());
    }
}
