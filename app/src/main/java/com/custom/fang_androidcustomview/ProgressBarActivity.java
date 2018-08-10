package com.custom.fang_androidcustomview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.custom.fang_androidcustomview.widget.CircleProgressBarView;
import com.custom.fang_androidcustomview.widget.HorizontalProgressBar;

public class ProgressBarActivity extends AppCompatActivity {

    private CircleProgressBarView circleProgressBarView;

    private HorizontalProgressBar horizontalProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

        circleProgressBarView = findViewById(R.id.circleProgressBarView);
        circleProgressBarView.setProgressWithAnimation(60);
        circleProgressBarView.startProgressAnimation();

        horizontalProgressBar = findViewById(R.id.horizontalProgressBar);
        horizontalProgressBar.setProgressWithAnimation(100).setProgressListener(new HorizontalProgressBar.ProgressListener() {
            @Override
            public void currentProgressListener(float currentProgress) {

            }
        }).startProgressAnimation();
    }
}
