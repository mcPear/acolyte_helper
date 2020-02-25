package com.example.mass;

import android.os.Bundle;
import com.example.mass.ui.main.ViewPagerConfigurator;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPagerConfigurator.setup(this);
    }


}