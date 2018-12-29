package com.maple.gaijin.engjapdictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_layout);

        overridePendingTransition(R.anim.go_in, R.anim.go_out);

        Intent intent = getIntent();
        String title = intent.getStringExtra("DESCRIPTION_TITLE");
        String description = intent.getStringExtra("DESCRIPTION");

        TextView descriptionTitle = findViewById(R.id.searchedTitle);
        TextView descriptionView = findViewById(R.id.description);
        descriptionTitle.setText(title);
        descriptionView.setText(description);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        this.finish();
    }
;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
