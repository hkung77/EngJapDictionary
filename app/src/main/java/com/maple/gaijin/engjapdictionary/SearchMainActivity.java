package com.maple.gaijin.engjapdictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchMainActivity extends AppCompatActivity {

    String description;
    String searchedTitleJA;
    String jaDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
    }

    // Handles when search is pressed
    // Will make API call to retrieve definition for searched word
    public void onSearchPress(View view) {
        TextView textInput = findViewById(R.id.searchText);
        String text = textInput.getText().toString();

        // Ensure we are not making API calls for empty text
        if (text.length() > 0) {
            // Make API call to get definition of searched word.
            DictionaryAPI request = new DictionaryAPI();
            request.get(text.toLowerCase(), null, new JsonHttpResponseHandler() {
                // Success response handler
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    // Log.d("API Response", "---------------- this is response : " + response.toString());
                    try {
                        // Get translation object
                        JSONObject serverResp = new JSONObject(response.toString());
                        JSONArray tucArray = serverResp.getJSONArray("tuc");

                        searchedTitleJA = getJapTitle(tucArray);
                        jaDescription = getJapaneseDescription(tucArray);
                        description = getEnglishDescription(tucArray);
                        navigateDescriptionScreen();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String getEnglishDescription(JSONArray tucArray) {
        if (tucArray.length() > 0) {
            try {

//                JSONObject tuc = (JSONObject) tucArray.get(0);
//
//                // Log.d("translation data", "---------------- this is response : " + tuc.toString());
//                JSONObject meaning = (JSONObject) tuc.getJSONArray("meanings").get(0);

                // TODO:
                // Show all possible meanings instead of just the first meaning
                JSONObject meaning = (JSONObject) ((JSONArray) filterMeanings(tucArray, "en")).get(0);
                // Log.d("definition meaning", "---------------- this is response : " + meaning.toString());
                return meaning.getString("text");
            } catch (JSONException e) {
                return "Cannot find definition";
            }
        }
        return "Cannot find definition";
    }

    private String getJapaneseDescription(JSONArray tucArray) {
        if (tucArray.length() > 0) {
            try {

//                JSONObject tuc = (JSONObject) tucArray.get(0);
//
//                // Log.d("translation data", "---------------- this is response : " + tuc.toString());
//                JSONObject meaning = (JSONObject) tuc.getJSONArray("meanings").get(0);

                // TODO:
                // Show all possible meanings instead of just the first meaning
                JSONObject meaning = (JSONObject) ((JSONArray) filterMeanings(tucArray, "ja")).get(0);
                // Log.d("definition meaning", "---------------- this is response : " + meaning.toString());
                return meaning.getString("text");
            } catch (JSONException e) {
                return "Cannot find definition";
            }
        }
        return "Cannot find definition";
    }

    // Possible values for lang are en and ja
    private JSONArray filterMeanings(JSONArray tucArray, String lang) {
        JSONArray filteredMeanings = new JSONArray();
        JSONArray meanings = new JSONArray();

        // Obtain all possible meaning values;
        for(int tucIndex = 0; tucIndex < tucArray.length(); tucIndex++) {
            try {
                JSONObject translation = (JSONObject) tucArray.get(tucIndex);
                meanings.put(translation.getJSONArray("meanings"));
            } catch (JSONException e) {
                // Do nothing
//                return filteredMeanings;
            }
        }

        // Filter meanings for each language code
        for(int meaningIndex = 0; meaningIndex < meanings.length(); meaningIndex++) {
            try {
                JSONObject meaning = (JSONObject) ((JSONArray) meanings.get(meaningIndex)).get(0);

                if (meaning.getString("language").equals(lang)) {
                    filteredMeanings.put(meaning);
                }

            } catch (JSONException e) {
                // Do nothing

//                return filteredMeanings;
            }
        }

        return filteredMeanings;
    }

    private String getJapTitle(JSONArray tucArray) {
        if (tucArray.length() > 0) {
            try {
                JSONObject tuc = (JSONObject) tucArray.get(0);

                // Log.d("translation data", "---------------- this is response : " + tuc.toString());
                JSONObject phrase = tuc.getJSONObject("phrase");
                // Log.d("definition meaning", "---------------- this is response : " + meaning.toString());
                if (phrase.getString("language").equals("ja")) {
                    return phrase.getString("text");
                }
                return "";

            } catch (JSONException e) {
                return "";
            }
        }
        return "";
    }

    public void navigateDescriptionScreen() {

        TextView textInput = findViewById(R.id.searchText);
        String text = textInput.getText().toString();

        // Navigate to next screen
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("DESCRIPTION_TITLE", text);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("JA_TITLE", searchedTitleJA);
        intent.putExtra("JA_DESCRIPTION", jaDescription);
        startActivity(intent);
    }
}
