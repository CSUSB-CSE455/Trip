package com.csusb.cse455.trip.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.csusb.cse455.trip.R;
import com.csusb.cse455.trip.adapter.OnSubscriptionCardClickCallback;
import com.csusb.cse455.trip.adapter.SubscriptionsDataAdapter;
import com.csusb.cse455.trip.data.MockDataSource;
import com.csusb.cse455.trip.model.Subscription;

import java.util.ArrayList;

// Subscriptions fragment, which handles the display of subscription cards.
public class SubscriptionsFragment extends Fragment implements OnSubscriptionCardClickCallback {
    // Fragment initialization parameters.
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_LABEL = "EXTRA_LABEL";
    private static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";

    // Main activity.
    private MainActivity mMainActivity;
    // Data adapter.
    private SubscriptionsDataAdapter mAdapter;
    // Data list.
    private ArrayList<Subscription> mListData;

    //Progress Dialog
    private ProgressDialog newSubscriptionDialog;

    //Async Task
    MyAsyncTask myAsyncTask;

    // Required empty public constructor.
    public SubscriptionsFragment() { }

    // Inflates view with the specified layout.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the main activity.
        mMainActivity = (MainActivity) getActivity();

        // Get data.
        // TODO: Change to real data.
        mListData = (ArrayList<Subscription>) MockDataSource.getSubscriptionsList(30);

        // Initialize adapter.
        if (mListData != null) {
            mAdapter = new SubscriptionsDataAdapter(mListData, getActivity().getBaseContext());
        }

        // Setup Recycler view.
        RecyclerView mRecView = (RecyclerView) view.findViewById(R.id.items_list);
        mRecView.setHasFixedSize(false);
        mRecView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecView.setAdapter(mAdapter);
        mAdapter.setCardClickCallback(this);

        //Instance Progress Dialog
        newSubscriptionDialog = new ProgressDialog(getActivity());

        // Set up item touch helper.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(mRecView);

        // Setup floating action button click listener.
        FloatingActionButton addItem = (FloatingActionButton) view.findViewById(R.id.btn_add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Subscription Dialog for Adding New Subscription
                newSubscriptionDialog.setIndeterminate(true);
                newSubscriptionDialog.setMessage("Please wait...");
                newSubscriptionDialog.setTitle("Adding New Subscription");
                newSubscriptionDialog.show();
                addSubscription();
            }
        });
    }

    // Creates callback for touch operations.
    private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            // Allows rearrangement of cards.
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Do nothing.
            }
        };
    }

    // Opens a new subscription creation view.
    private void addSubscription() {
        Intent intent = new Intent(getActivity(), NewSubscriptionActivity.class);
        getActivity().startActivity(intent);
    }

    // Moves an item within the list.
    private void moveItem(int oldPos, int newPos) {
        Subscription item = mListData.get(oldPos);
        if (item != null) {
            mListData.remove(oldPos);
            mListData.add(newPos, item);
            mAdapter.notifyItemMoved(oldPos, newPos);
        }
    }

    //Dismiss Progress Dialog for Adding New Subscription when return to Fragment
    @Override
    public void onResume() {
        super.onResume();
        if(newSubscriptionDialog!=null){
            newSubscriptionDialog.dismiss();
        }
    }

    // Handles on click event by showing details.
    @Override
    public void onViewActionClick(int position) {
        // Get an item form the given position.
        Subscription item = mListData.get(position);

        //Create new Async Task
        myAsyncTask = new MyAsyncTask();

        // Create a bundle.
        Bundle extras = new Bundle();
        extras.putString(EXTRA_ID, item.getId());
        extras.putString(EXTRA_LABEL, item.getLabel());
        extras.putString(EXTRA_DESCRIPTION, item.getDescription());

        // Create a new details fragment.
        Fragment newFragment = new SubscriptionDetailsFragment();

        // Pass bundle.
        newFragment.setArguments(extras);

        // Load the new fragment.
        mMainActivity.loadFragment(this.getId(), "SUBSCRIPTION_DETAILS_FRAGMENT", newFragment,
                true, "Subscription Details");

        //Execute Async Task
        myAsyncTask.execute();
    }

    //Set up MyAsyncTask for Progress Dialog
    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        boolean running;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            int i = 10;
            while(running){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i-- == 0){
                    running = false;
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
            progressDialog = ProgressDialog.show(getActivity(),"","Please wait...",true,false);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}

