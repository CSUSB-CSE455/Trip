package com.csusb.cse455.trip.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csusb.cse455.trip.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubscriptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscriptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends Fragment {


    public SubscriptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

}
