package com.swufe.clock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;





public class AlarmActivity extends AppCompatActivity {

    private ListView alarmList;
    private FloatingActionButton btnAdd;
    private ArrayAdapter<AlarmData> adapter;
    SharedPreferences sp;
    private static final String KEY_ALARM_LIST = "alarmList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_activity);


        alarmList = (ListView) findViewById(R.id.alarmList);

        //当列表为空时，出现提示信息
        TextView emptyView = new TextView(AlarmActivity.this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("还没有闹钟");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) alarmList.getParent()).addView(emptyView);
        alarmList.setEmptyView(emptyView);


        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按事件
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {//长按事件
                new AlertDialog.Builder(AlarmActivity.this).setTitle("操作选项").setItems(new CharSequence[]{"删除", "添加"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            case 1:
                                addAlarm();
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消", null).show();
                return true;
            }
        });

        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this,AlarmAdd.class);
                startActivityForResult(intent,100);
            }
        });

        adapter = new ArrayAdapter<AlarmData>(this
                , android.R.layout.simple_list_item_1);
        alarmList.setAdapter(adapter);
        readSaveAlarmList();
    }

//选择闹钟
    private void addAlarm() {
        Intent intent = new Intent(AlarmActivity.this,AlarmAdd.class);
        startActivityForResult(intent,100);
    }

    //删除数据
    private void deleteAlarm(int position) {
        adapter.remove(adapter.getItem(position));
        adapter.notifyDataSetChanged();
        saveAlarmList();

    }
//保存数据
    private void saveAlarmList() {
        SharedPreferences.Editor ed;
        ed =  getSharedPreferences(AlarmActivity.class.getName(), Activity.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTimeLable()).append(";");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            ed.putString(KEY_ALARM_LIST, content);
        } else {
            ed.putString(KEY_ALARM_LIST, null);
        }

        ed.commit();//提交

    }
//读取数据
    private void readSaveAlarmList() {
        SharedPreferences sp = getSharedPreferences(AlarmActivity.class.getName(),
                Activity.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);

        if (content != null) {
            String[] timeStrings = content.split(";");
            for (String string : timeStrings) {
                adapter.add(new AlarmData(string));
            }
        }
    }


    //接收从TimeListActivity中传回来的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sp = getSharedPreferences("myalarm",MODE_PRIVATE);
        switch (requestCode){
            case 100:
                if(resultCode==40){
                    AlarmData ad = new AlarmData("闹钟\t("+sp.getString("repeat_value","")+")\t"+sp.getString("time",""));
                    adapter.add(ad);
                    adapter.notifyDataSetChanged();
                    // 添加之后记得保存闹钟列表
                    saveAlarmList();
                }
                break;
            default:
                break;
        }


    }

    private class AlarmData {
        private String timeLable = "";

        public AlarmData(String timelable){
            this.timeLable = timelable;

        }

        public void setTimeLable(String timeLable) {
            this.timeLable = timeLable;
        }

        public String getTimeLable() {
            return timeLable;
        }

        //重载头String()方法
        @Override
        public String toString() {
            return getTimeLable();
        }
    }




}