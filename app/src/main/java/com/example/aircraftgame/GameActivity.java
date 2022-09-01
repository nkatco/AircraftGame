package com.example.aircraftgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView airplaneImageView, bulletImageView, imageView3, leftImageView, rightImageView;
    private TextView scoreTextView, timerTextView;
    private boolean cd = true;
    private boolean inGame = true;
    private boolean hit;
    private int count = 0;
    private int rand;
    private int set = 0;
    private int time = 15;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initialize();

        timer();
    }


    public void initialize() {
        airplaneImageView = findViewById(R.id.airplaneImageView);
        leftImageView = findViewById(R.id.leftImageView);
        rightImageView = findViewById(R.id.rightImageView);
        timerTextView = findViewById(R.id.timerTextView);
        bulletImageView = findViewById(R.id.bulletImageView);
        imageView3 = findViewById(R.id.imageView3);
        scoreTextView = findViewById(R.id.scoreTextView);

        airplaneImageView.setOnClickListener(this);
        leftImageView.setOnClickListener(this);
        rightImageView.setOnClickListener(this);
    }
    public void timer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(time > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerTextView.setText("Time: " + time);
                        }
                    });
                    time--;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                inGame = false;
            }
        }); thread.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.airplaneImageView:
                if (cd) {
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bullet();
                        }
                    }); thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.leftImageView:
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (airplaneImageView.getLeft() > 5) {
                            airplaneImageView.setLeft(airplaneImageView.getLeft() - 70);
                        }
                    }
                }); thread2.start();
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rightImageView:
                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (airplaneImageView.getLeft() < getDisplay().getWidth() - 400) {
                            airplaneImageView.setLeft(airplaneImageView.getLeft() + 70);
                        }
                    }
                }); thread3.start();
                try {
                    thread3.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void balls() {
                while (inGame) {
                    imageView3.setLeft(getDisplay().getWidth() - 400);
                    hit = false;
                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (imageView3.getLeft() > -800 && !hit) {
                                imageView3.setLeft(imageView3.getLeft() - 20);
                                imageView3.setImageResource(R.drawable.duck3);
                                try {
                                    Thread.sleep(20L - set);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                imageView3.setImageResource(R.drawable.duck4);
                            }
                        }
                    }); thread2.start();
                    try {
                        thread2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread thread3 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(imageView3.getLeft() < getDisplay().getWidth() - 400 && !hit) {
                                imageView3.setLeft(imageView3.getLeft() + 20);
                                imageView3.setImageResource(R.drawable.duck2);
                                try {
                                    Thread.sleep(20L - set);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                imageView3.setImageResource(R.drawable.duck1);
                            }
                        }
                    }); thread3.start();
                    try {
                        thread3.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void bullet() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                cd = false;
                bulletImageView.setLeft(airplaneImageView.getLeft() + 135);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bulletImageView.setVisibility(View.VISIBLE);
                    }
                });
                while(bulletImageView.getTop() > 100 && inGame) {
                    bulletImageView.setTop(bulletImageView.getTop() - 10);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (bulletImageView.getLeft() >= imageView3.getLeft() && bulletImageView.getLeft() <= imageView3.getLeft() + 600) {
                    count++;
                    set++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scoreTextView.setText("Score: " + count);
                        }
                    });
                    hit = true;
                }
                bulletImageView.setVisibility(View.INVISIBLE);
                bulletImageView.setTop(airplaneImageView.getTop());
                cd = true;
            }
        }).start();
    }
}