package com.pritesh.androidappratingtogooglestore;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import java.util.ArrayList;

public class SampleRecyclerViewActivity extends AppCompatActivity
{

    RecyclerView recList;
    ArrayList<CardViewModel> listitems = new ArrayList<CardViewModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_recycler_view);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        setupListItems();
        if(listitems.size() > 0 & recList != null)
        {
            recList.setAdapter(new MyAdapter(this,listitems));
        }
        recList.setLayoutManager(llm);
    }

    private int getThemePrimaryColor() {
        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int[] attribute = new int[]{R.attr.colorPrimary};
        final TypedArray array = obtainStyledAttributes(typedValue.resourceId, attribute);
        return array.getColor(0, Color.MAGENTA);
    }

    public void setupListItems()
    {
        listitems.clear();
        CardViewModel item1 = new CardViewModel();
        item1.setCardName("Pritesh Patel");
        item1.setImageResourceId(R.drawable.my_pic);
        item1.setIsfav(0);
        item1.setIsturned(0);
        listitems.add(item1);
        CardViewModel item2 = new CardViewModel();
        item2.setCardName("Jigisha Patel");
        item2.setImageResourceId(R.drawable.header_bg);
        item2.setIsfav(0);
        item2.setIsturned(0);
        listitems.add(item2);
        CardViewModel item3 = new CardViewModel();
        item3.setCardName("Cart Item");
        item3.setImageResourceId(R.drawable.header_bg);
        item3.setIsfav(0);
        item3.setIsturned(0);
        listitems.add(item3);
        CardViewModel item4 = new CardViewModel();
        item4.setCardName("Moksh Patel");
        item4.setImageResourceId(R.drawable.my_profile);
        item4.setIsfav(0);
        item4.setIsturned(0);
        listitems.add(item4);
    }
}
