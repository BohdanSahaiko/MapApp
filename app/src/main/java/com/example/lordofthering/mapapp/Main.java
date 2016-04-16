package com.example.lordofthering.mapapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lordofthering.mapapp.Adapter.MyAdapter;
import com.example.lordofthering.mapapp.Adapter.PojoForList;
import com.example.lordofthering.mapapp.DataBase.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static int n;
    public static Cursor c = null;
    public DatabaseHelper myDbHelper;
    ListView listView;
    private ArrayAdapter adapter;
    static TypedArray images;
    static ArrayList<PojoForList> maps;
    Toolbar toolbar;
    static Drawable draw;
    Intent intent;
    static ArrayList<String> geomanser;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectDB();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) Main.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Main.this.getComponentName()));
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(this, Rest.class);
        setN(position + 2);
        draw = images.getDrawable(position);
        startActivity(intent);
    }

    public ArrayList<PojoForList> initData() {

        for(int i = 1 ;i < 56 ;i++) {
            getC().moveToPosition(i);
            geomanser.add(getC().getString(7) + "}" + getC().getString(1) + "№" + getC().getString(2));
            PojoForList objectItem1 = new PojoForList(i,
                    getC().getString(1), getC().getString(2),
                    images.getDrawable(i - 1));
            maps.add(objectItem1);
        }
        return maps;
    }

    private void connectDB()
    {
        myDbHelper = new DatabaseHelper(Main.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error(getString(R.string.DatabaseError));
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        setC(myDbHelper.query("Enterprice", null, null, null, null, null, null));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private void init()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        maps = new ArrayList<>();
        //toolbar.setLogo(R.drawable.ic_dining);
        geomanser = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        Resources res = getResources();
        images = res.obtainTypedArray(R.array.images);
        adapter = new MyAdapter(initData(), this);
        listView.setAdapter(adapter);
    }
    static public int getN() {return n;}
    public void setN(int n) {this.n = n;}
    static public Cursor getC() {
        return c;
    }
    public void setC(Cursor c) {
        this.c = c;
    }
    public static ArrayList<String> getGeomanser() {
        return geomanser;
    }
    public static void setGeomanser(ArrayList<String> geomanser) {
        Main.geomanser = geomanser;
    }
}
