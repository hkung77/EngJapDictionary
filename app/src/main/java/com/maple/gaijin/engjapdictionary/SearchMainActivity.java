package com.maple.gaijin.engjapdictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchMainActivity extends AppCompatActivity {

    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
    }

    public void onSearchPress(View view) {
        TextView textInput = findViewById(R.id.searchText);
        String text = textInput.getText().toString();

        if (text.length() > 0) {
            // Make API call to get definition of searched word.
            DictionaryAPI request = new DictionaryAPI();
            request.get(text, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
//                    Log.d("FUCKER", "---------------- this is response : " + response.toString());
                    try {
                        JSONObject serverResp = new JSONObject(response.toString());
                        JSONArray tucArray = serverResp.getJSONArray("tuc");

                        if (tucArray.length() > 0) {
                            JSONObject tuc = (JSONObject) tucArray.get(0);

                        Log.d("FUCKER tuc", "---------------- this is response : " + tuc.toString());
                            JSONObject meaning = (JSONObject) tuc.getJSONArray("meanings").get(0);
                            Log.d("FUCKER meaning", "---------------- this is response : " + meaning.toString());
                            description = meaning.getString("text");

                            navigateDescriptionScreen();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void navigateDescriptionScreen() {

        TextView textInput = findViewById(R.id.searchText);
        String text = textInput.getText().toString();

        // Navigate to next screen
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("DESCRIPTION_TITLE", text);
        intent.putExtra("DESCRIPTION", description);
        startActivity(intent);
    }
}
