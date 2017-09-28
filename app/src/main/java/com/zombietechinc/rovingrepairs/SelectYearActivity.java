package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectYearActivity extends AppCompatActivity {

    ListView yearlv;
    String make;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_year);
        Intent intent = getIntent();
        make = intent.getStringExtra("make");

        yearlv = (ListView)findViewById(R.id.year_list);

        ArrayList<Integer> years = new ArrayList<>();

        int[] year = {1980,1981,1982,1983,1984,1985,1986,1987,1988,1989,1990,1991,1992,1993,1994,1995,1996,1997,1998,1999,2000,2001,2002,
                        2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017};

        for (int i = 0; i < year.length; i++){
            years.add(year[i]);
        };
        SortIntegerArrayList sortArrayList = new SortIntegerArrayList(years);
        years = sortArrayList.sortDescending();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, years);
        yearlv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        final ArrayList<Integer> finalYears = years;
        yearlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int year = finalYears.get(i);
                Log.d("Vehicle Year: ", String.valueOf(year));
                Intent intent1 = new Intent(SelectYearActivity.this, SelectModelActivity.class);
                String yearString = String.valueOf(year);
                intent1.putExtra("year", yearString);
                intent1.putExtra("make", make);
                startActivity(intent1);
            }
        });

    }
}
