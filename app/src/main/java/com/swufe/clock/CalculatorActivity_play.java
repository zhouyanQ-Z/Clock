package com.swufe.clock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CalculatorActivity_play extends AppCompatActivity {
    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;

    private int allTimeCount = 0;//记录时间总数
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Button btnStart, btnPause, btnResume, btnReset;
    private EditText etHour, etMin, etSec;
    private TextView cancleText;

    private Vibrator mVibrator;//震动
    private MediaPlayer mediaplayer;//提示音
    private PowerManager.WakeLock mWakelock;//PowerManager.WakeLock 也称作唤醒锁, 是一种保持 CPU 运转防止设备休眠的方式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab4_play);
        //确保在锁屏状态下弹出Dialog
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnResume = (Button) findViewById(R.id.btnResume);
        btnReset = (Button) findViewById(R.id.btnReset);
        cancleText = (TextView) findViewById(R.id.cacl_text);


        cancleText.setOnClickListener(new View.OnClickListener() {//取消，返回
            @Override
            public void onClick(View v) {//返回上一个activity
                CalculatorActivity_play.this.finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();
                btnPause.setVisibility(View.VISIBLE);
                btnResume.setVisibility(View.GONE);
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                etHour.setText("00");
                etMin.setText("00");
                etSec.setText("00");

                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });

        etHour = (EditText) findViewById(R.id.etHour);
        etMin = (EditText) findViewById(R.id.etMin);
        etSec = (EditText) findViewById(R.id.etSec);

        String hour, minute, second;
        Intent intent = getIntent();
        hour = intent.getStringExtra("hour");
        etHour.setText(hour);
        etHour.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                /*
                 * 这个方法是在Text改变过程中触发调用的， 它的意思就是说在原有的文本s中，
                 * 从start开始的count个字符替换长度为before的旧文本，
                 * 注意这里没有将要之类的字眼，也就是说一句执行了替换动作。
                 */
                if (!TextUtils.isEmpty(s)) {

                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        minute = intent.getStringExtra("minute");
        etMin.setText(minute);
        etMin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {

                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        second = intent.getStringExtra("second");
        etSec.setText(second);
        etSec.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {

                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        btnStart.setVisibility(View.VISIBLE);
       // btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
    }

//检查时间格式是否可以开始计时
    private void checkToEnableBtnStart() {
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer
                .parseInt(etHour.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etMin.getText()) && Integer
                .parseInt(etMin.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etSec.getText()) && Integer
                .parseInt(etSec.getText().toString()) > 0));
    }

    private void startTimer() {
        if (timerTask == null) {
            allTimeCount = Integer.parseInt(etHour.getText().toString()) * 60
                    * 60 + Integer.parseInt(etMin.getText().toString()) * 60
                    + Integer.parseInt(etSec.getText().toString());
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    allTimeCount--;
                    handle.sendEmptyMessage(MSG_WHAT_TIME_TICK);
                    if (allTimeCount <= 0) {
                        handle.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private Handler handle = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:
                    int hour = allTimeCount / 60 / 60;
                    int min = (allTimeCount / 60) % 60;
                    int sec = allTimeCount % 60;

                    etHour.setText(String.format("%02d",hour));
                    etMin.setText(String.format("%02d",min));
                    etSec.setText(String.format("%02d",sec));
                    break;

                case MSG_WHAT_TIME_IS_UP:
                    startMedia();
                    startVibrator();
                    new AlertDialog.Builder(CalculatorActivity_play.this)
                            .setTitle("计时")
                            .setMessage("计时结束!")
                            .setNegativeButton("停止", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mediaplayer.stop();
                                    mVibrator.cancel();
                                    CalculatorActivity_play.this.finish();//返回
                                }
                            }).show();

                    btnReset.setVisibility(View.GONE);
                    btnResume.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    //震动
    private void startVibrator() {

        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         *
         */
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 500, 1000, 500, 1000 }; // 停止 开启 停止 开启
        mVibrator.vibrate(pattern, 0);

    }

    //音乐播放
    private void startMedia() {
        try {
            mediaplayer = MediaPlayer.create(this, com.swufe.clocklibrary.R.raw.in_call_alarm);
            mediaplayer.setLooping(true);
            mediaplayer.start();
//            mediaplayer.setDataSource(this,
//                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)); //铃声类型为默认闹钟铃声
//            mediaplayer.prepare();
//            mediaplayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 唤醒屏幕
        acquireWakeLock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }

    /**
     * 唤醒屏幕
     */
    private void acquireWakeLock() {
        if (mWakelock == null) {
            //获取电源管理器对象
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);//PowerManager控制设备的电源状态
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
                    .getCanonicalName());

            //点亮屏幕
            mWakelock.acquire();
        }
    }

    /**
     * 释放锁屏
     */
    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }


}
