package com.esports.vishal.esportsscoreapplication;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * SearchTimelineFragment demonstrates a TimelineListAdapter with a SearchTimeline.
 */
public class SearchTimelineFragment extends ListFragment {

    public static SearchTimelineFragment newInstance() {
        return new SearchTimelineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(((TweetsActivity) getActivity()).getHashtag())
                .maxItemsPerRequest(50)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(),
                searchTimeline);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweetui_timeline, container, false);
    }
}
