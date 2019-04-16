
package com.example.autandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chatbotActivity(View view)
    {
        Intent intent = new Intent(this, chatbot_button.class);
        startActivity(intent);
    }

    public void coursesActivity(View view)
    {
        Intent intent = new Intent(this, courses_button.class);
        startActivity(intent);
    }

    public void helpActivity(View view)
    {
        Intent intent = new Intent(this, help_button.class);
        startActivity(intent);
    }
    public void servicesActivity(View view)
    {
        Intent intent = new Intent(this, services_button.class);
        startActivity(intent);
    }

}
