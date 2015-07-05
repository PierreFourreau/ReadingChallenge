package com.fourreau.readingchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fourreau.readingchallenge.R;
import com.gc.materialdesign.views.ButtonRectangle;

public class AboutActivity extends AppCompatActivity {

    private ButtonRectangle buttonOpenSourceProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        buttonOpenSourceProjects = (ButtonRectangle) findViewById(R.id.see_projects_open_source_button);
        buttonOpenSourceProjects.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, LicensesActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
