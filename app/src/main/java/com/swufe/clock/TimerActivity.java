package com.swufe.clock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.format.Time;

import android.view.Gravity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class TimerActivity extends AppCompatActivity {
    //private Toolbar toolbar;
    private ImageView img, addTime_iv;
    private TextView taCountry, taDate, taTime;
    private SimpleAdapter simpleAdapter;
    private ListView dataList;
    List<Map<String, Object>> list;
    String timeE;//时区/地域对应的id
    String timeC;//时区/地域名字
    String timeE1;//日期
    String timeC1;//时间
    private static final String KEY_TIMER_LIST = "timerList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2_activity);

//        toolbar = (Toolbar)findViewById(R.id.tab2_tb);
//        setSupportActionBar(toolbar);

        img = (ImageView) findViewById(R.id.img1);
        addTime_iv = (ImageView) findViewById(R.id.addTimer);
        taCountry = (TextView) findViewById(R.id.countryName);
        taDate = (TextView) findViewById(R.id.date_time);
        taTime = (TextView) findViewById(R.id.timePlay);
        dataList = (ListView) findViewById(R.id.time_play);


        list = new ArrayList<Map<String, Object>>();

        //当列表为空时，出现提示信息
        TextView emptyView = new TextView(TimerActivity.this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("还没有时间");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) dataList.getParent()).addView(emptyView);
        dataList.setEmptyView(emptyView);

        addTime_iv.setOnClickListener(new View.OnClickListener() {//选择添加哪个时区的时间
            @Override
            public void onClick(View v) {
                //事件处理代码
                Intent intent = new Intent(TimerActivity.this, TimeListActivity.class);    //1，当前类   2、跳转的类
                startActivityForResult(intent, 100);
            }
        });


    }

    private List<Map<String, Object>> getData() {
        list.clear();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("image", R.drawable.timer);
//        Log.e("jjjj", timeC);
        row.put("text1", timeE + timeC);
        row.put("text2", timeE1);
        row.put("text3", timeC1);
        list.add(row);
        return list;
    }


    //接收从TimeListActivity中传回来的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == 41) {
                    timeE = data.getStringExtra("timeen");
                    timeC = data.getStringExtra("timech");
                    // 指定适配器
                    simpleAdapter = new SimpleAdapter(this, list,
                            R.layout.tab2_activity_layout, new String[]{"image", "text1", "text2", "text3"},
                            new int[]{R.id.img1, R.id.countryName, R.id.date_time, R.id.timePlay});

                    handler.postDelayed(task, 0);//表示程序运行到此处延迟开启这个handler

                    // 绑定
                    dataList.setAdapter(simpleAdapter);
//                    readSaveTimerList();

                    //长按删除
                    dataList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            new AlertDialog.Builder(TimerActivity.this).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    list.remove(position);
                                    simpleAdapter.notifyDataSetChanged();
                                    saveTimerList();
                                    dialog.dismiss();
                                }
                            }).create().show();

                            return true;
                        }
                    });
                }
                break;

            default:
                break;

        }
    }

    //读取数据
//    private void readSaveTimerList() {
//        SharedPreferences sp = getSharedPreferences(TimerActivity.class.getName(),
//                Activity.MODE_PRIVATE);
//        String content = sp.getString(KEY_TIMER_LIST, null);
//
//        if (content != null) {
//            String[] timeStrings = content.split(",");
//            for (String string : timeStrings) {
//               simpleAdapter.
//            }
//        }
//    }
    private void saveTimerList() {
        SharedPreferences.Editor ed;
        ed =  getSharedPreferences(AlarmActivity.class.getName(), Activity.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < simpleAdapter.getCount(); i++) {
            sb.append(simpleAdapter.getItem(i)).append(",");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            ed.putString(KEY_TIMER_LIST, content);
        } else {
            ed.putString(KEY_TIMER_LIST, null);
        }

        ed.commit();//提交
    }

    private void getTime(String id) {
        TimeZone tz = TimeZone.getTimeZone(id);
        Time time = new Time(tz.getID());
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        timeE1 = year + "年" + (month + 1) + "月" + day + "日";
        timeC1 = String.format("%02d:%02d",hour,minute);//时，分

    }

    private final Handler handler = new Handler();

    private final Runnable task = new Runnable() {
        public void run() {

            try {
                list.clear();
                getTime(timeE);
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("image", R.drawable.timer);
                row.put("text1", timeC);//时区/地域
                row.put("text2", timeE1);//日期
                row.put("text3", timeC1);//时间
//
//                if(list!=null||!list.isEmpty()) {
//                    for (int i = 0; i < list.size(); i++) {
//                        Map<String,Object> map = list.get(i);
//                        if(map.get("text1").toString().equals(row.get("text1").toString())){
//                            list.get(i).clear();
//                            list.add(row);
//                        }
//                    }
//                }
                    list.add(row);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                simpleAdapter.notifyDataSetChanged();
                saveTimerList();
            }
            handler.postDelayed(this, 30 * 1000);//每隔半分钟执行一次
        }

    };


}