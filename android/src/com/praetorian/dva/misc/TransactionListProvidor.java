package com.praetorian.dva.misc;

import java.util.ArrayList;

import android.content.Context;

public class TransactionListProvidor
{
    private Context context;
    private ArrayList<Transaction> transactions;

    public TransactionListProvidor(Context context)
    {
        this.context = context;
        transactions = new ArrayList<Transaction>();

        for (int i = 0; i < 100; i++)
        {
            transactions.add(new Transaction());
        }
    }

    public Transaction getTransaction(int index)
    {
        return transactions.get(index);
    }

    public int size()
    {
        return transactions.size();
    }

}
