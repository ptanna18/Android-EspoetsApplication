package com.esports.vishal.esportsscoreapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TwitterHelper {

    private static final String com_twitter_sdk_android_tweetcomposer_UPLOAD_SUCCESS = "com.twitter.sdk.android.tweetcomposer.UPLOAD_SUCCESS";
    private static final String com_twitter_sdk_android_tweetcomposer_UPLOAD_FAILURE = "com.twitter.sdk.android.tweetcomposer.UPLOAD_FAILURE";

    private Activity mActivity;
    private TwitterListerner mTwitterListerner;

    private TwitterResultReceiver mTwitterResultReceiver;
    private TwitterAuthClient mTwitterAuthClient;

    public TwitterHelper(Activity activity) {
        mActivity = activity;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(mActivity.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                mActivity.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        TwitterConfig config = new TwitterConfig.Builder(mActivity)
                .twitterAuthConfig(authConfig).build();
        Twitter.initialize(config);
    }

    public void doAuth(TwitterListerner listerner) {
        mTwitterListerner = listerner;
        loadActiveSession();
    }

    public TwitterAuthClient getmTwitterAuthClient() {
        return mTwitterAuthClient;
    }

    private void loadActiveSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        if (session != null && session.getAuthToken() != null) {
            mTwitterListerner.onLoginSuccess(session);
        } else {
            if (mTwitterAuthClient == null)
                mTwitterAuthClient = new TwitterAuthClient();
            mTwitterAuthClient.authorize(mActivity, new Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    // Success
                    mTwitterListerner.onLoginSuccess(twitterSessionResult.data);
                }

                @Override
                public void failure(TwitterException e) {
                    mTwitterListerner.onLoginFailed(e);
                }
            });
        }
    }

    public void registerReciever() {
        if (mTwitterResultReceiver == null)
            mTwitterResultReceiver = new TwitterResultReceiver();
        mActivity.registerReceiver(mTwitterResultReceiver, new IntentFilter(com_twitter_sdk_android_tweetcomposer_UPLOAD_SUCCESS));
        mActivity.registerReceiver(mTwitterResultReceiver, new IntentFilter(com_twitter_sdk_android_tweetcomposer_UPLOAD_FAILURE));
    }

    public void unRegisterReciever() {
        if (mTwitterResultReceiver != null)
            mActivity.unregisterReceiver(mTwitterResultReceiver);
    }

    public interface TwitterListerner {
        void onLoginSuccess(TwitterSession session);

        void onLoginFailed(TwitterException e);

        void onShareSuccess();

        void onShareFailed();
    }

    public class TwitterResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
                mTwitterListerner.onShareSuccess();
            } else {
                // failure
                mTwitterListerner.onShareFailed();
            }
        }
    }
}
