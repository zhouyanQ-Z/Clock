package com.swufe.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SecondTimerActivity extends AppCompatActivity {
    private Button btnStart,btnPause,btnResume,btnReset,btnLap;
    private TextView tvHour,tvMin,tvSec,tvMsec;
    private ListView lvTimeList;//显示增加的秒表记录
    private ArrayAdapter<String> adapter;//记录秒表添加次数

    private int totalMSecs = 0; //记录时间总数
    private Timer timer =new Timer();
    private TimerTask timerTask = null;
    private TimerTask showTimerTask = null;
    private static final int MSG_WHAT_SHOW_TIME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_activity);

        tvHour = (TextView) findViewById(R.id.timeHour);
        tvHour.setText("00");
        tvMin = (TextView) findViewById(R.id.timeMin);
        tvMin.setText("00");
        tvSec = (TextView) findViewById(R.id.timeSec);
        tvSec.setText("00");
        tvMsec = (TextView) findViewById(R.id.timeMSec);
        tvMsec.setText("0");

        btnStart = (Button) findViewById(R.id.btnSWStart);
        btnPause = (Button) findViewById(R.id.btnSWPause);
        btnResume = (Button) findViewById(R.id.btnSWResume);
        btnReset = (Button) findViewById(R.id.btnSWReset);
        btnLap = (Button) findViewById(R.id.btnSWLap);

        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnLap.setVisibility(View.GONE);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                totalMSecs = 0;
                adapter.clear();
                btnReset.setVisibility(View.GONE);
                btnLap.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter.insert(String.format("%02d:%02d:%02d.%02d", totalMSecs/100/60/60,totalMSecs/100/60%60,totalMSecs/100%60,totalMSecs%100), 0);
            }
        });

        lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);
        adapter = new ArrayAdapter<String>(SecondTimerActivity.this, android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);

        //长按监听事件
        lvTimeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //
                return true;
            }

        });

        showTimerTask=new TimerTask() {
            @Override
            public void run() {
                handle.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }

        };
        timer.schedule(showTimerTask, 200, 200);
    }
    private void startTimer() {
        if (timerTask == null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    totalMSecs++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }

    }

    private void stopTimer(){
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private Handler handle = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(String.format("%02d",totalMSecs/100/60/60));
                    tvMin.setText(String.format("%02d",totalMSecs/100/60%60));
                    tvSec.setText(String.format("%02d",totalMSecs/100%60));
                    tvMsec.setText(String.format("%02d",totalMSecs%100));
                    break;

                default:
                    break;
            }
        };
    };

    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}