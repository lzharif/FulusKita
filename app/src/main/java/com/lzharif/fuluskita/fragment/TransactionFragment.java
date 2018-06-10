package com.lzharif.fuluskita.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.activity.AddEditTransactionActivity;
import com.lzharif.fuluskita.helper.Constant;
import com.lzharif.fuluskita.helper.Utils;
import com.lzharif.fuluskita.model.MoneyTransaction;
import com.lzharif.fuluskita.model.Summary;
import com.lzharif.fuluskita.viewholder.MoneyTransactionViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {
    private TextView tvSummary;
    private RecyclerView rvTransaction;
    private FloatingActionButton fabAddTransaction;
    private DatabaseReference dbTransaction = Utils.getDatabase().getReference();
    private DatabaseReference dbSummary;
    private DatabaseReference dbTransactionData;
    private FirebaseRecyclerAdapter<MoneyTransaction, MoneyTransactionViewHolder> mAdapter;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbSummary = dbTransaction.child(Constant.DIR_SUMMARY);
        dbTransactionData = dbTransaction.child(Constant.DIR_TRANSACTION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View transactionFragment = inflater.inflate(R.layout.fragment_home, container, false);
        tvSummary = transactionFragment.findViewById(R.id.textView_summary_value);
        rvTransaction = transactionFragment.findViewById(R.id.recyclerview_transaction_list);
        fabAddTransaction = transactionFragment.findViewById(R.id.fab_add_transaction);

        return transactionFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Manage recyclerview manager
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setStackFromEnd(true);
        mManager.setReverseLayout(true);
        rvTransaction.setLayoutManager(mManager);
        rvTransaction.setItemAnimator(new DefaultItemAnimator());

        dbSummary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Summary sm = dataSnapshot.getValue(Summary.class);
                    tvSummary.setText(getString(R.string.value_holder, "", "Rp. ", sm.currentBalance));
                } else {
                    initializeSummary();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query mQuery = dbTransactionData.orderByChild("date");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MoneyTransaction>()
                .setQuery(mQuery, MoneyTransaction.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<MoneyTransaction, MoneyTransactionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MoneyTransactionViewHolder holder, int position, @NonNull MoneyTransaction model) {
                final DatabaseReference transRef = getRef(position);

                final String id = transRef.getKey();
                final String username = getItem(position).username;
                final String userId = getItem(position).userId;
                final int category = getItem(position).category;
                final String desc = getItem(position).desc;
                final double value = getItem(position).value;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), AddEditTransactionActivity.class);
                        intent.putExtra(Constant.TRANSACTION_ID, id);
                        intent.putExtra(Constant.USERNAME, username);
                        intent.putExtra(Constant.USER_ID, userId);
                        intent.putExtra(Constant.TRANSACTION_PACKAGE_TYPE, "addEdit");
                        startActivity(intent);
                    }
                });

                holder.bindToTransaction(model);
            }

            @NonNull
            @Override
            public MoneyTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new MoneyTransactionViewHolder(inflater.inflate(R.layout.card_transaction, parent, false));
            }
        };

        rvTransaction.setAdapter(mAdapter);

        // Manage FAB
        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEditTransactionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeSummary() {
        Summary summary = new Summary(0.00, 0.00, 0.00);
        Map<String, Object> sValue = summary.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/summary/", sValue);
        dbTransaction.updateChildren(childUpdate);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null)
            mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

}
