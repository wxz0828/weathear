package com.example.a57vz.coolweather;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    private ImageView splashSun;
    private ImageView splashCould1;
    private ImageView splashCould2;
    private ImageView splashCould3;
    private TextView countDown;
    private MyCountDownTimer downTimer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        downTimer= new MyCountDownTimer(4000,1000);
        downTimer.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        },2000);
    }

    private Handler handler = new Handler();

    private void initView(){
        splashSun = (ImageView) findViewById(R.id.splashSun);
        splashCould1 = (ImageView) findViewById(R.id.splashCloud1);
        splashCould2 = (ImageView) findViewById(R.id.splashCloud2);
        splashCould3 = (ImageView) findViewById(R.id.splashCloud3);
        countDown = (TextView) findViewById(R.id.countDown);
        // 需要在布局填充完成后才能获取到View的尺寸
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                playAnim();
                // 需要移除监听，否则会重复触发
                getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 设置动画属性 其中太阳360无限循环 云朵水平平移
     */
    private void playAnim(){
        playSunAnim();
        playCloud_1Anim();
        playCloud_2Anim();
        playCloud_3Anim();
    }

    private void playSunAnim(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashSun,"rotation",0f,360f);
        anim.setRepeatMode(ObjectAnimator.RESTART);//设置动画循环模式。
        anim.setRepeatCount(ObjectAnimator.INFINITE);//设置动画重复次数，-1代表无限
        anim.setInterpolator(new LinearInterpolator());//设置动画插入器，减速
        anim.setDuration(30 * 1000);  //设置动画时间
        anim.start();
    }

    private void playCloud_1Anim(){
        float cloud1TranslationX = splashCould1.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashCould1, "translationX", cloud1TranslationX,-250f,cloud1TranslationX);
        anim.setDuration(8 * 1000);
        anim.start();

    }

    private void playCloud_2Anim(){
        float cloud2TranslationX = splashCould2.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashCould2, "translationX", cloud2TranslationX,-200f, cloud2TranslationX);
        anim.setDuration(7* 1000);
        anim.start();
    }

    private void playCloud_3Anim(){
        float cloud3TranslationX = splashCould3.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashCould3, "translationX",cloud3TranslationX,-300f,cloud3TranslationX);
        anim.setDuration(8 * 1000);
        anim.start();
    }
    private class MyCountDownTimer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */

        /**
         * millisInFuture
         *     表示以毫秒为单位 倒计时的总数
         *
         *     例如 millisInFuture=1000 表示1秒
         * countDownInterval
         *     表示 间隔 多少微秒 调用一次 onTick 方法
         *
         *     例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countDown.setText("倒计时（"+millisUntilFinished/1000+"）");
        }

        @Override
        public void onFinish() {
          countDown.setText("正在跳转.....");
        }
    }
}
