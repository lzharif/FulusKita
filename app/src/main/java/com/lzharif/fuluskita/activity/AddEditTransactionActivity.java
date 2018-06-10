package com.lzharif.fuluskita.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Constant;
import com.lzharif.fuluskita.helper.Utils;
import com.lzharif.fuluskita.model.MoneyTransaction;
import com.lzharif.fuluskita.model.Summary;
import com.lzharif.fuluskita.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// TODO add another type of transaction in business
// TODO create activity (or popup?) to choose type of transaction
public class AddEditTransactionActivity extends AppCompatActivity {
    private static final String TAG = "AddEditTransactionActivity";
    private Toolbar toolbarAddEdit;
    private LinearLayout layoutValue;
    private LinearLayout layoutDate;
    private LinearLayout layoutCategory;
    private LinearLayout layoutDesc;
    private TextView tvValue;
    private TextView tvDate;
    private TextView tvCategory;
    private EditText etDesc;
    private Button btnSubmit;

    private String currentDate;
    private String dateSelected;
    private SimpleDateFormat dateFormat;

    private DatabaseReference dbTransaction = Utils.getDatabase().getReference();
    private DatabaseReference dbAddEdit;
    private DatabaseReference dbUser;

    private int category;
    private int typeTransaction;
    private String categorySign = "-";
    private String description = "";
    private double value;
    private double prevValue;
    private String id;
    private String username;
    private String userId;
    private MoneyTransaction mT;
    private String MODE_ACTIVITY;
    private int currentCategory;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putDouble(Constant.VALUE, value);
        savedInstanceState.putString(Constant.DATE, dateSelected);
        savedInstanceState.putInt(Constant.CATEGORY, category);
        savedInstanceState.putString(Constant.DESC, description);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_transaction);
        toolbarAddEdit = findViewById(R.id.toolbar_add);
        layoutValue = findViewById(R.id.edit_value_transaction);
        layoutDate = findViewById(R.id.edit_date_transaction);
        layoutCategory = findViewById(R.id.edit_category_transaction);
        layoutDesc = findViewById(R.id.edit_desc_transaction);
        tvValue = findViewById(R.id.value_transaction);
        tvDate = findViewById(R.id.date_transaction);
        tvCategory = findViewById(R.id.category_transaction);
        etDesc = findViewById(R.id.desc_transaction);
        btnSubmit = findViewById(R.id.submit_transaction);

        setSupportActionBar(toolbarAddEdit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = dateFormat.format(new Date());
        dateSelected = currentDate;
        value = 0.00;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MODE_ACTIVITY = "edit";
            getSupportActionBar().setTitle(getString(R.string.edit_transaction_toolbar));
            id = bundle.getString(Constant.TRANSACTION_ID);
            username = bundle.getString(Constant.USERNAME);
            userId = bundle.getString(Constant.USER_ID);
            dbAddEdit = dbTransaction.child(Constant.DIR_TRANSACTION).child(id);
            fetchDataTransaction();

        } else {
            MODE_ACTIVITY = "add";
            getSupportActionBar().setTitle(getString(R.string.add_transaction_toolbar));
            tvValue.setText(getString(R.string.value_holder, categorySign, "Rp. ", 0.00));
            tvDate.setText(getString(R.string.today));
            tvCategory.setText(getString(R.string.choose_category));
        }

        layoutValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        layoutDate.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            int startDate = calendar.get(Calendar.DATE);

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditTransactionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.DATE, dayOfMonth);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.YEAR, year);
                                dateSelected = dateFormat.format(calendar.getTime());

                                if (dateSelected.equalsIgnoreCase(currentDate))
                                    tvDate.setText(getString(R.string.today));
                                else
                                    tvDate.setText(dateSelected);
                            }
                        }, startYear, startMonth, startDate);
                datePickerDialog.show();
            }
        });

        layoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEditTransactionActivity.this);
                builder.setTitle(R.string.choose_category)
                        .setItems(R.array.category_transaction_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                category = i;
                                // TODO make a variety of type itself
                                if (category == 0) {
                                    typeTransaction = 0;
                                    categorySign = "-";
                                    tvCategory.setText(getResources().getStringArray(R.array.category_transaction_array)[0]);
                                } else {
                                    typeTransaction = 20;
                                    categorySign = "+";
                                    tvCategory.setText(getResources().getStringArray(R.array.category_transaction_array)[1]);
                                }
                            }
                        });
                builder.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                value = data.getDoubleExtra(Constant.VALUE, value);
                tvValue.setText(getString(R.string.value_holder, categorySign, "Rp. ", value));
            }
        }
    }

    private void submitData() {
        final String transactionId;
        description = etDesc.getText().toString();
        if (MODE_ACTIVITY.equals("add")) {
            userId = getUid();
            transactionId = FirebaseDatabase.getInstance().getReference()
                    .child(Constant.DIR_TRANSACTION).push().getKey();

            dbTransaction.child(Constant.DIR_USER).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) {
                        Toast.makeText(AddEditTransactionActivity.this,
                                getString(R.string.error_failed_get_user), Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<String, Object> time = new HashMap<>();
                        time.put("timestamp", ServerValue.TIMESTAMP);

                        String username = user.username;

                        mT = new MoneyTransaction();
                        long dateLong = mT.convertDateToLong(dateSelected);

                        MoneyTransaction moneyT = new MoneyTransaction(transactionId, value, userId,
                                username, description, dateLong, category, typeTransaction, time);

                        Map<String, Object> moneyTValue = moneyT.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        // Save it to transaction folder
                        childUpdates.put("/transaction/" + transactionId, moneyTValue);

                        // Save it to user transaction folder
                        childUpdates.put("/transaction-user/" + userId + "/" + transactionId, moneyTValue);
                        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
                        AddCounterSummary();

                        // Close the Activity, back to MainActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        // If editing, then it's changing a lot
        else {
            transactionId = id;
            HashMap<String, Object> time = new HashMap<>();
            time.put("timestamp", ServerValue.TIMESTAMP);

            mT = new MoneyTransaction();
            long dateLong = mT.convertDateToLong(dateSelected);

            MoneyTransaction moneyT = new MoneyTransaction(transactionId, value, userId,
                    username, description, dateLong, category, typeTransaction, time);

            Map<String, Object> moneyTValue = moneyT.toMap();
            Map<String, Object> childUpdates = new HashMap<>();

            // Save it to transaction folder
            childUpdates.put("/transaction/" + transactionId, moneyTValue);

            // Save it to user transaction folder
            childUpdates.put("/transaction-user/" + userId + "/" + transactionId, moneyTValue);
            FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
            AddCounterSummary();

            // Close the Activity, back to MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddCounterSummary() {
        // valueEdit used to track the difference between values after editing
        Double valueEdit = 0.0;
        if (MODE_ACTIVITY.equals("add")) {
            valueEdit = value;
        } else {
            if (value == prevValue)
                valueEdit = value;
            else
                valueEdit = value - prevValue;
        }
        DatabaseReference dataSummary = FirebaseDatabase.getInstance().getReference().child(Constant.DIR_SUMMARY);
        final Double finalValueEdit = valueEdit;
        dataSummary.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Summary s = mutableData.getValue(Summary.class);
                if (s == null)
                    return Transaction.success(mutableData);
                else {
                    // If there is change of category transaction in edit mode
                    if (MODE_ACTIVITY.equals("edit") && currentCategory != category) {
                        // If the previous category is expense, then it's changed to income
                        if (currentCategory == 0) {
                            s.currentCredit = s.currentCredit - finalValueEdit;
                            s.currentDebit = s.currentDebit + finalValueEdit;
                            s.currentBalance = s.currentBalance + finalValueEdit;
                        }
                        // If the previous category is income, then it's changed to expense
                        else {
                            s.currentCredit = s.currentCredit + finalValueEdit;
                            s.currentDebit = s.currentDebit - finalValueEdit;
                            s.currentBalance = s.currentBalance - finalValueEdit;
                        }
                    }
                    // If it's adding a new transaction or editing the current one without changing the category
                    else {
                        // Expense
                        if (category == 0) {
                            s.currentCredit = s.currentCredit + finalValueEdit;
                            s.currentDebit = s.currentDebit - finalValueEdit;
                            s.currentBalance = s.currentBalance - finalValueEdit;
                        }
                        // Income
                        else {
                            s.currentCredit = s.currentCredit - finalValueEdit;
                            s.currentDebit = s.currentDebit + finalValueEdit;
                            s.currentBalance = s.currentBalance + finalValueEdit;
                        }
                    }
                    mutableData.setValue(s);
                }
                return Transaction.success(mutableData);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "manageSummary:onComplete:" + databaseError);
            }
        });
    }

    private void fetchDataTransaction() {
        dbAddEdit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mT = dataSnapshot.getValue(MoneyTransaction.class);
                if (mT != null) {
                    dateSelected = mT.convertLongToDate(mT.date);
                    category = mT.category;
                    currentCategory = category;
                    if (category == 0)
                        categorySign = "-";
                    else
                        categorySign = "+";

                    description = mT.desc;
                    value = mT.value;
                    if (MODE_ACTIVITY.equals("edit"))
                        prevValue = mT.value;

                    tvValue.setText(getString(R.string.value_holder, categorySign, "Rp. ", value));
                    if (dateSelected.equalsIgnoreCase(currentDate))
                        tvDate.setText(getString(R.string.today));
                    else
                        tvDate.setText(dateSelected);
                    tvCategory.setText(getResources()
                            .getStringArray(R.array.category_transaction_array)[category]);
                    etDesc.setText(description);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
