package com.csusb.cse455.trip.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.csusb.cse455.trip.R;
import com.csusb.cse455.trip.adapter.ItemClickCallback;
import com.csusb.cse455.trip.adapter.MyTripsDataAdapter;
import com.csusb.cse455.trip.data.MockDataSource;
import com.csusb.cse455.trip.model.MyTripItem;

import java.util.ArrayList;

public class MyTripsFragment extends Fragment implements ItemClickCallback {
    // Fragment initialization parameters.
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_LABEL = "EXTRA_LABEL";
    private static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";

    // Recycler view.
    private RecyclerView mRecView;
    // Data adapter.
    private MyTripsDataAdapter mAdapter;
    // Data list.
    private ArrayList mListData;

    // Required empty public constructor.
    public MyTripsFragment() { }

    // Inflates view with the specified layout.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_my_trips, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get data.
        // TODO: Change to real data.
        mListData = (ArrayList) MockDataSource.getMyTripItemsList(30);
        if (mListData == null) {
            mListData = new ArrayList();
        }

        // Initialize adapter.
        mAdapter = new MyTripsDataAdapter(mListData, getContext());

        // Setup Recycler view.
        mRecView = (RecyclerView) view.findViewById(R.id.items_list);
        mRecView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                addItemToList();
            }
        });
    }

    // Creates callback for touch operations.
    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    // Add a new item to list.
    // TODO: Change to real data.
    private void addItemToList() {
        MyTripItem item = MockDataSource.getMyTripItem();
        mListData.add(item);
        mAdapter.notifyItemInserted(mListData.indexOf(item));
    }

    // Moves an item within the list.
    private void moveItem(int oldPos, int newPos) {
        MyTripItem item = (MyTripItem) mListData.get(oldPos);
        mListData.remove(oldPos);
        mListData.add(newPos, item);
        mAdapter.notifyItemMoved(oldPos, newPos);
    }

    // Deletes an item from the list.
    private void deleteItem(int position) {
        mListData.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    // Handles on click event by showing details.
    @Override
    public void onItemClick(int position) {
        // Get item details and create a bundle.
        MyTripItem item = (MyTripItem) mListData.get(position);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_ID, item.getId());
        extras.putString(EXTRA_LABEL, item.getLabel());
        extras.putString(EXTRA_DESCRIPTION, item.getDescription());

        // Create a new details fragment.
        Fragment frag = new MyTripDetailsFragment();

        // Pass bundle.
        frag.setArguments(extras);

        // Create a fragment transaction.
        FragmentTransaction tran = getFragmentManager().beginTransaction();

        // Replace this fragment with the new fragment.  Push old fragment onto the stack.
        tran.replace(this.getId(), frag);
        tran.addToBackStack(null);

        // Commit transaction.
        tran.commit();
    }
}
