package de.nnordman.beaf.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.nnordman.beaf.DataBaseHelper;
import de.nnordman.beaf.Model.Event;
import de.nnordman.beaf.MyAdapter;
import de.nnordman.beaf.R;
import de.nnordman.beaf.RecyclerItem;


public class events_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<RecyclerItem> listItems;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    ProgressDialog progress;

    DataBaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_, container, false);
        Event eventDetails = new Event();

        db = new DataBaseHelper(this.getActivity());


/*        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference ref = mDatabase.child("9qpVu8MX6NPk6UheJLLqql7QzKj1").child("events/Party");*/

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();

        showAllSQLData();

        return view;
    }

    public void showAllSQLData() {

        Cursor res = db.getAllEventsData();
        if (res.getCount() == 0) {
            //show message
            //Toast.makeText(getActivity(), R.string.no_events_created, Toast.LENGTH_SHORT).show();
        }

        while (res.moveToNext()) {
            listItems.add(new RecyclerItem(res.getString(0), res.getString(1), res.getString(3)));
            adapter = new MyAdapter(listItems, getActivity());
            recyclerView.setAdapter(adapter);
        }
        db.close();
    }


}
