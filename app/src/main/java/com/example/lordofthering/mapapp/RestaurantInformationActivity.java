package com.example.lordofthering.mapapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantInformationActivity extends AppCompatActivity {
    TextView address, numbers, ops, timework, http;
    String geo;
    Toolbar toolbar;
    CollapsingToolbarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
        init();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rest, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getGeo()));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void init()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_rest);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        address = (TextView) findViewById(R.id.address);
        numbers = (TextView) findViewById(R.id.number);
        ops = (TextView) findViewById(R.id.ops);
        timework = (TextView) findViewById(R.id.time);
        http = (TextView) findViewById(R.id.http);
        ImageView image = (ImageView) findViewById(R.id.imageRestView);
        image.setImageDrawable(MainActivity.draw);
        appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Context context = this;
        appBarLayout.setContentScrimColor(ContextCompat.getColor(context,R.color.primary_dark));
        if (MainActivity.getC().moveToPosition(MainActivity.getN() - 1)) {
            String s = MainActivity.getC().getString(1);
            int p ;
            int r ;
            if (s != null && s.indexOf("«") > -1) {
                p = s.indexOf("«");
                r = s.indexOf("»");
                String str = s.substring(p + 1, r);
                appBarLayout.setTitle(str);
            } else getSupportActionBar().setTitle(MainActivity.getC().getString(1));

            address.setText("Адреса: " + MainActivity.getC().getString(2));
            if (numbers != null) {
                numbers.setText("Номер телефону: \n" + MainActivity.getC().getString(3));
                int i;
                if ((i = numbers.getText().toString().indexOf(",")) > -1) {
                    String txt = numbers.getText().toString().toString();
                    numbers.setText(txt.substring(0, i + 1) + "\n" + txt.substring(i + 1, txt.length()));
                }
                Linkify.addLinks(numbers, Linkify.ALL);
            }
            ops.setText("Опис: " + MainActivity.getC().getString(4));
            timework.setText(MainActivity.getC().getString(5));
            if (http != null) {
                http.setText("Офіційний сайт: \n" + MainActivity.getC().getString(6));
                Linkify.addLinks(http, Linkify.ALL);
            }

            setGeo(MainActivity.getC().getString(7));
            Toast.makeText(RestaurantInformationActivity.this, MainActivity.getC().getString(1), Toast.LENGTH_SHORT).show();
        }
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

}
