package com.example.android.internet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.internet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = findViewById(R.id.et_search_box);

        mUrlDisplayTextView = findViewById(R.id.tv_url_display);

        mSearchResultsTextView = findViewById(R.id.tv_github_search_results_json);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
    }

    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        new GithubQueryTask().execute(githubSearchUrl);
    }

    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // First, hide the currently visible data
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // COMPLETED (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubSearchResults) {
            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                // COMPLETED (17) Call showJsonDataView if we have valid, non-null results
                showJsonDataView();
                mSearchResultsTextView.setText(githubSearchResults);
            } else {
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItem = item.getItemId();
        if (clickedItem == R.id.action_search){

            makeGithubSearchQuery();
        }
        return super.onOptionsItemSelected(item);
    }
}
