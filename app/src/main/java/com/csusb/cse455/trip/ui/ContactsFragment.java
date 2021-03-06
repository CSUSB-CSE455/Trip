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
import com.csusb.cse455.trip.adapter.ContactsDataAdapter;
import com.csusb.cse455.trip.adapter.OnContactItemClickCallback;
import com.csusb.cse455.trip.data.MockDataSource;
import com.csusb.cse455.trip.model.Contact;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Contacts fragment, which handles the display of Contact items.
public class ContactsFragment extends Fragment implements OnContactItemClickCallback {
    // Fragment initialization parameters.
    private static final String EXTRA_FIRST_NAME = "EXTRA_FIRST_NAME";
    private static final String EXTRA_LAST_NAME = "EXTRA_LAST_NAME";
    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    // Main activity.
    private MainActivity mMainActivity;
    // Data adapter.
    private ContactsDataAdapter mAdapter;
    // Data list.
    private ArrayList<Contact> mListData;
    //Async Task
    MyAsyncTask myAsyncTask;

    // Required empty public constructor.
    public ContactsFragment() { }

    // Inflates view with the specified layout.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the main activity.
        mMainActivity = (MainActivity) getActivity();

        // Get data.
        // TODO: Change to real data.
        mListData = (ArrayList<Contact>) MockDataSource.getContactsList(30);
        Collections.sort(mListData, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                int res = c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
                if (res != 0) {
                    return res;
                }
                return c1.getLastName().compareToIgnoreCase(c2.getLastName());
            }
        });

        // Initialize adapter.
        if (mListData != null) {
            mAdapter = new ContactsDataAdapter(mListData, getActivity().getBaseContext());
        }

        // Setup Recycler view.
        RecyclerView mRecView = (RecyclerView) view.findViewById(R.id.items_list);
        mRecView.setHasFixedSize(false);
        mRecView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecView.setAdapter(mAdapter);
        mAdapter.setItemClickCallback(this);

        // Set up item touch helper.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(mRecView);

        // Setup floating action button click listener.
        FloatingActionButton addItem = (FloatingActionButton) view.findViewById(R.id.btn_add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContact();
            }
        });
    }

    // Creates callback for touch operations.
    private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            // Allows rearrangement of items.
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

    // Opens a new contact addition view.
    private void addNewContact() {
        Intent intent = new Intent(getActivity(), NewContactActivity.class);
        getActivity().startActivity(intent);
        //show Progress Dialog
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    // Moves an item within the list.
    private void moveItem(int oldPos, int newPos) {
        Contact item = mListData.get(oldPos);
        if (item != null) {
            mListData.remove(oldPos);
            mListData.add(newPos, item);
            mAdapter.notifyItemMoved(oldPos, newPos);
        }
    }

    // Handles on click event by showing details.
    @Override
    public void onViewActionClick(int position) {
        // Get an item form the given position.
        Contact item = mListData.get(position);

        // Create a bundle.
        Bundle extras = new Bundle();
        extras.putString(EXTRA_FIRST_NAME, item.getFirstName());
        extras.putString(EXTRA_LAST_NAME, item.getLastName());
        extras.putString(EXTRA_EMAIL, item.getEmail());

        // Create a new details fragment.
        Fragment newFragment = new ContactDetailsFragment();

        // Pass bundle.
        newFragment.setArguments(extras);

        // Load the new fragment.
        mMainActivity.loadFragment(this.getId(), "CONTACT_DETAILS_FRAGMENT", newFragment,
                true, "Contact Details");
        //show Progress Dialog
        myAsyncTask = new MyAsyncTask();
        //execute Async Task for Progress Dialog
        myAsyncTask.execute();
    }

    //Async Task for Progress Bar
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
