package com.click.circularprogressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.click.progress_library.view.CircularProgressBar;
import com.click.progress_library.view.OnFinishListener;
import com.click.progress_library.view.ResUtil;

public class MainActivity extends AppCompatActivity {

    private CircularProgressBar CircularProgressBar1, CircularProgressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ResUtil.init(this);

        CircularProgressBar1 = (CircularProgressBar) findViewById(R.id.CircularProgressBar1);
        CircularProgressBar2 = (CircularProgressBar) findViewById(R.id.CircularProgressBar2);

        CircularProgressBar1.setDuration(10 * 1000, new OnFinishListener() {
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
            }
        });


        CircularProgressBar2.setDuration(10 * 1500, new OnFinishListener() {
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
