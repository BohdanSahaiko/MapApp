package com.example.lordofthering.mapapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Rest extends AppCompatActivity {
    TextView address, numbers, ops, timework, http;
    String geo;
    Toolbar toolbar;
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent;
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getGeo()));
            startActivity(intent);
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void init()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        address = (TextView) findViewById(R.id.address);
        numbers = (TextView) findViewById(R.id.number);
        ops = (TextView) findViewById(R.id.ops);
        timework = (TextView) findViewById(R.id.time);
        http = (TextView) findViewById(R.id.http);
        ImageView image = (ImageView) findViewById(R.id.imageRestView);
        image.setImageDrawable(Main.draw);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(R.drawable.ic_place_black_18dp);
                Snackbar.make(view, R.string.like, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action, null).show();
            }
        });
        if (Main.getC().moveToPosition(Main.getN() - 1)) {
            String s = Main.getC().getString(1);
            int p ;
            int r ;
            if (s != null && s.indexOf("«") > -1) {
                p = s.indexOf("«");
                r = s.indexOf("»");
                String str = s.substring(p + 1, r);
                getSupportActionBar().setTitle(str);
            } else getSupportActionBar().setTitle(Main.getC().getString(1));

            address.setText("Адреса: " + Main.getC().getString(2));
            if (numbers != null) {
                numbers.setText("Номер телефону: \n" + Main.getC().getString(3));
                int i;
                if ((i = numbers.getText().toString().indexOf(",")) > -1) {
                    String txt = numbers.getText().toString().toString();
                    numbers.setText(txt.substring(0, i + 1) + "\n" + txt.substring(i + 1, txt.length()));
                }
                Linkify.addLinks(numbers, Linkify.ALL);
            }
            ops.setText("Опис: " + Main.getC().getString(4));
            timework.setText(Main.getC().getString(5));
            if (http != null) {
                http.setText("Офіційний сайт: \n" + Main.getC().getString(6));
                Linkify.addLinks(http, Linkify.ALL);
            }
            setGeo(Main.getC().getString(7));
            Toast.makeText(Rest.this, Main.getC().getString(1), Toast.LENGTH_SHORT).show();
        }
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

}
