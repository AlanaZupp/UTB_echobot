package com.example.autandroidapp;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatbotRecycler extends RecyclerView.ViewHolder
{
    TextView leftText, rightText;

    public ChatbotRecycler(View itemView)
    {
        super(itemView);
        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);
    }
}
