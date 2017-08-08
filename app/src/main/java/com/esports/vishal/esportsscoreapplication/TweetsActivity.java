package com.esports.vishal.esportsscoreapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TweetsActivity extends AppCompatActivity {

    String hashtag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hashtag = getIntent().getStringExtra(MainActivity.KEY_SEARCH_HASHTAG);
        if (hashtag == null) {
            hashtag = "#csgo";
        }

        setContentView(R.layout.activity_tweets);
    }

    public String getHashtag() {
        return hashtag;
    }
}
