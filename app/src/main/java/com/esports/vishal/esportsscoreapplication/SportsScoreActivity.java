package com.esports.vishal.esportsscoreapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esports.vishal.esportsscoreapplication.CSGO.CsGoNetworkUtility;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;

public class SportsScoreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static int SPLASH_TIMEOUT = 3000;
    private TextView textView;
    private ProgressBar loading;
    private SwipeRefreshLayout swipeContainer;
    private ListView listview;
    String[] listItem;
    ArrayAdapter adapter;

    android.app.FragmentTransaction ft;
    ArrayList<String[]> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                fetchURL();
                loading.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            }

        });
        swipeContainer.setEnabled(false);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);




        listview = (ListView) findViewById(R.id.custom_listview);
//        listItem = new String[1];
//        listItem[0] = "Working";
        listItem = new String[]{"fdlkjlsd", "fdslfkjskd"};

        list = new ArrayList<String[]>();


        //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new CustomAdapter(this, list);
        listview.setAdapter(adapter);

        //textView = (TextView) findViewById(R.id.csgo_fetch_data);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        fetchURL();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sports_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class CsGoDataFetchTask extends AsyncTask<URL,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            try {

                return CsGoNetworkUtility.getResponseFromHttpUrl(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);

            String jsonData = value;
            try {
                JSONObject jsonObject = new JSONObject(jsonData);

                JSONArray results = jsonObject.getJSONArray("results");

                listItem = new String[results.length()];

                int len = results.length();

                for(int i = 0; i < results.length(); i++) {
                    Log.d("LOGGED APP", "ITERIA");

                    JSONObject event = results.getJSONObject(i).getJSONObject("sport_event");

                    JSONObject tournament = event.getJSONObject("tournament");
                    String tournamentName = tournament.getString("name");

                    String sportName = tournament.getJSONObject("sport").getString("name");

                    JSONArray Competitors = event.getJSONArray("competitors");

                    String firstCompetitor = Competitors.getJSONObject(0).getString("name");

                    String secondCompetitor = Competitors.getJSONObject(1).getString("name");

                    final int[] comp1_count = {0};
                    final int[] comp2_count = {0};

                    final int comp11_count = 0;

                    String comp1_hashtag = "#" + firstCompetitor.replace(" ", "").toLowerCase();
                    String comp2_hashtag = "#" + secondCompetitor.replace(" ", "").toLowerCase();
                    final String[] vals = {
                            tournamentName,
                            firstCompetitor,
                            secondCompetitor,
                            Integer.toString(comp1_count[0]),
                            Integer.toString(comp2_count[0])
                    };
                    TwitterConfig config = new TwitterConfig.Builder(getApplicationContext())
                            .logger(new DefaultLogger(Log.DEBUG))
                            .twitterAuthConfig(new TwitterAuthConfig("W4aTidLCancJ4yKQcjoQOrMuG", "bfstVokk07CpAqqxVrG0ko0n2hNP7wWh6X4DQyBetvUxIPtXI7"))
                            .debug(true)
                            .build();
                    Twitter.initialize(config);

                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

                    SearchService searchService = twitterApiClient.getSearchService();

                    Call<Search> search = searchService.tweets(comp1_hashtag, null, null, null, null, 100, null, null,
                            null, true);
                    search.enqueue(new Callback<Search>() {
                        @Override
                        public void success(Result<Search> result) {
                            vals[3] = Integer.toString(result.data.tweets.size());
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Log.d("ERROR", e.toString());
                        }
                    });

                    Call<Search> search2 = searchService.tweets(comp2_hashtag, null, null, null, null, 100, null, null,
                            null, true);
                    search2.enqueue(new Callback<Search>() {
                        @Override
                        public void success(Result<Search> result) {
                            vals[4] = Integer.toString(result.data.tweets.size());
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Log.d("ERROR", e.toString());
                        }
                    });

                    list.add(i, vals);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
            // textView.setText(value);
           loading.setVisibility(View.GONE);
        }
    }

    public void fetchURL(){
        CsGoDataFetchTask csGoDataFetchTask = new CsGoDataFetchTask();

        URL url = CsGoNetworkUtility.makeURL();
        csGoDataFetchTask.execute(url);

        listItem = new String[]{"ffd", "fdfsd"};
        adapter.notifyDataSetChanged();
    }


    class CustomAdapter extends ArrayAdapter<String[]> {
        private final Context context;
        private final ArrayList<String[]> values;


        public CustomAdapter(@NonNull Context context, @NonNull ArrayList<String[]> objects) {
            super(context, -1, objects);

            this.context = context;
            this.values = objects;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.tournament_fragment_layout, parent, false);

            TextView tour_name = (TextView) rowView.findViewById(R.id.tour_name);
            TextView comp1 = (TextView) rowView.findViewById(R.id.comp_1);
            TextView comp2 = (TextView) rowView.findViewById(R.id.comp_2);

            TextView comp1_tweetcount = (TextView) rowView.findViewById(R.id.comp1_tweet_count);
            TextView comp2_tweetcount = (TextView) rowView.findViewById(R.id.comp_2_tweet_count);

            tour_name.setText(values.get(position)[0] + " Tournament");
            comp1.setText(values.get(position)[1]);
            comp2.setText(values.get(position)[2]);

            comp1_tweetcount.setText(values.get(position)[3]);
            comp2_tweetcount.setText(values.get(position)[4]);

            int comp1_count = Integer.parseInt(values.get(position)[3]);
            int comp2_count = Integer.parseInt(values.get(position)[4]);

            if(comp1_count != comp2_count) {
                if(comp1_count > comp2_count) {
                    comp1.setTextColor(Color.parseColor("#00b300"));
                    comp1_tweetcount.setTextColor(Color.parseColor("#00b300"));
                } else {
                    comp2.setTextColor(Color.parseColor("#00b300"));
                    comp2_tweetcount.setTextColor(Color.parseColor("#00b300"));
                }
            }


            return rowView;
        }
    }
}
