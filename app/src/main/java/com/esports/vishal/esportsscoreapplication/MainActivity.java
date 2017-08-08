package com.esports.vishal.esportsscoreapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.esports.vishal.esportsscoreapplication.CSGO.CsGoScoreActivity;
import com.squareup.picasso.Picasso;

import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_SEARCH_HASHTAG = "key_search_hashtag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardView = (CardView) findViewById(R.id.cardview_cs);
        CardView cardViewLol = (CardView) findViewById(R.id.cardview_lol) ;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CsGoScoreActivity.class);
                startActivity(intent);
            }
        });

        cardViewLol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","Cardview lol called");
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.imageview_cs);
        ImageView imageViewlol = (ImageView) findViewById(R.id.imageview_lol);
        Picasso.with(imageView.getContext()).load(R.drawable.csgo).resize(dp2px(220),0).into(imageView);
        Picasso.with(imageViewlol.getContext()).load(R.drawable.lol).resize(dp2px(220),0).into(imageViewlol);
    }

    public void onGameButtonClick(View view) {
        handleTwitterLoginAndLoadTweets((String) view.getTag());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTwitterShareHelper != null && mTwitterShareHelper.getmTwitterAuthClient() != null)
            mTwitterShareHelper.getmTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This method is used to handle the sharing of image on Twitter application
     *
     * @param uri
     */
    TwitterHelper mTwitterShareHelper;

    private void handleTwitterLoginAndLoadTweets(final String hashtag) {
        if (mTwitterShareHelper == null)
            mTwitterShareHelper = new TwitterHelper(this);
        mTwitterShareHelper.doAuth(new TwitterHelper.TwitterListerner() {
            @Override
            public void onLoginSuccess(TwitterSession session) {
                // Login success
                Toast.makeText(getApplicationContext(), "Twitter login success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TweetsActivity.class);
                intent.putExtra(KEY_SEARCH_HASHTAG, hashtag);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(TwitterException e) {

            }

            @Override
            public void onShareSuccess() {

            }

            @Override
            public void onShareFailed() {

            }

        });
    }

            public int dp2px(int dp) {
        WindowManager wm = (WindowManager) this.getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }
}