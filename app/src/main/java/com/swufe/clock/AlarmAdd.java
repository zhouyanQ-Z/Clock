package com.swufe.clock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.TimePickerView;
import com.swufe.clock.view.SelectRemindCyclePopup;
import com.swufe.clock.view.SelectRemindWayPopup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmAdd extends AppCompatActivity implements View.OnClickListener{
    private TextView date_tv,sure_tv;
    private ImageView back_iv;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl,ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private LinearLayout allLayout;
    private String time;
    private int cycle;
    private int ring;

    private SharedPreferences sharedPreferences;
    SharedPreferences .Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_alarm_add);
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        date_tv = (TextView) findViewById(R.id.date_tv);
        sure_tv = (TextView) findViewById(R.id.set_sure);
        sure_tv.setOnClickListener(this);
        back_iv = (ImageView)findViewById(R.id.back_icon);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        sharedPreferences = getSharedPreferences("myalarm", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date) {//闹钟设置，获取闹钟
                time = getTime(date);
            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {//返回上一个activity
            @Override
            public void onClick(View v) {
                AlarmAdd.this.finish();
            }
        });

    }

    private String getTime(Date date) {//设置时间格式
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.repeat_rl:
                selectRemindCycle();//闹钟响铃格式
                break;
            case R.id.ring_rl:
                selectRingWay();//响铃方式
                break;
            case R.id.set_sure:
                setClock();//确定设置的闹钟
                break;
            default:
                break;
        }

    }

    private void setClock() {
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            if (cycle == 0) {//是每天的闹钟
                com.swufe.clocklibrary.AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), getId(time), 0, "闹钟响了!", ring);
            } if(cycle == -1){//是只响一次的闹钟
                com.swufe.clocklibrary.AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), getId(time), 0, "闹钟响了!", ring);
            }else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    com.swufe.clocklibrary.AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
                            .parseInt(times[1]), i+getId(time), Integer.parseInt(weeks[i]), "闹钟响了!", ring);
                }
            }
            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            String repeatValue = tv_repeat_value.getText().toString();
           // String ringValue = tv_ring_value.getText().toString();
            editor.putString("time",time);
            editor.putString("repeat_value",repeatValue);
//            editor.putString("ring_value",ringValue);
            editor.commit();
            setResult(40,intent);
            finish();
        }


    }
//设置唯一请求码,在设置多个闹钟时不会被覆盖
    private int getId(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat("HH:mm").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();//得到时间戳
        return (int) ( ts/ 1000 / 60);

    }


    private void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("每天");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText("震动");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tv_ring_value.setText("铃声");
                        ring = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }
}
