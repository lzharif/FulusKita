package com.lzharif.fuluskita.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.model.MoneyTransaction;

public class MoneyTransactionViewHolder extends RecyclerView.ViewHolder {
    private TextView tvTransactionValue;
    private TextView tvTransactionDesc;
    private TextView tvTransactionUser;
    private TextView tvTransactionDate;
    private ImageView ivTransactionIcon;

    public MoneyTransactionViewHolder(View itemView) {
        super(itemView);
        tvTransactionDesc = itemView.findViewById(R.id.transaction_desc);
        tvTransactionUser = itemView.findViewById(R.id.transaction_user);
        tvTransactionDate = itemView.findViewById(R.id.transaction_date);
        tvTransactionValue = itemView.findViewById(R.id.transaction_value);
        ivTransactionIcon = itemView.findViewById(R.id.transaction_icon);
    }

    public void bindToTransaction(MoneyTransaction mT) {
        int category = mT.category;
        String title = itemView.getResources().getStringArray(R.array.category_transaction_array)[category];
        String desc = mT.desc;
        String date = mT.convertLongToDate(mT.date);
        String username = mT.username;
        String sign = "";
        if (category == 1)
            sign = "+";
        else
            sign = "-";
        String value = itemView.getContext().getString(R.string.value_holder, sign, "Rp. ", mT.value);

        if (desc.length() != 0)
            tvTransactionDesc.setText(desc);
        else
            tvTransactionDesc.setText(title);
        tvTransactionUser.setText(username);
        tvTransactionDate.setText(date);
        tvTransactionValue.setText(value);
    }
}
