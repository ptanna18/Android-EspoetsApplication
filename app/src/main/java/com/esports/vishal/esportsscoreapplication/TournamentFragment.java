package com.esports.vishal.esportsscoreapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TournamentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tournament_fragment_layout, container, false);

        TextView tourName = (TextView) rootView.findViewById(R.id.tour_name);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
