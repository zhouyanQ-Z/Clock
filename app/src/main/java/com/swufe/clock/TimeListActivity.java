package com.swufe.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class TimeListActivity extends Activity implements AdapterView.OnItemClickListener, TextWatcher {

    //显示时区的列表
    private ListView listView;

    //存放时区信息的HashMap
    private HashMap<String, String> map = new HashMap<String, String>();

    //这个数组只存放时区名，用于列表显示
    private ArrayList<String> list = new ArrayList<String>();

    //搜索输入框
    private EditText editText;
    //列表显示的适配器
    private myadapter name;

    String YMD;
    String tTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.times_list);
        listView = (ListView)findViewById(R.id.world_time_list);
        editText = (EditText)findViewById(R.id.ss);
        editText.addTextChangedListener(this);
//通过这个方法，去xml文件中获得时区信息
        getdata();
//列表显示
        name = new myadapter();
        listView.setAdapter(name);
//列表单击事件监听
        listView.setOnItemClickListener(this);
    }
    //通过时区的id获得当时的时间
    public void getTime(String id)
    {
        TimeZone tz = TimeZone.getTimeZone(id);
//String s = "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT)
//+ " Timezon id :: " + tz.getID();
        Time time = new Time(tz.getID());
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        YMD = year+"年"+(month+1)+"月"+day+"日";
        tTime = hour+":"+minute+":"+sec;
//
//        return  "当前时间为：" + year +
//                "年 " + (month+1) +
//                "月 " + day +
//                "日 " + hour +
//                "时： " + minute +
//                "分： " + sec +
//                "秒";
    }
    //一次次的从xml文件获取信息
    public void getdata()
    {
        try {
//将上次的数据清空，方便重新搜索
            map.clear();
            list.clear();
//获取信息的方法
            Resources res = getResources();
            XmlResourceParser xrp = res.getXml(R.xml.timezones);
//判断是否已经到了文件的末尾
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String name = xrp.getName();
                    if (name.equals("timezone")) {
//关键词搜索，如果匹配，那么添加进去如果不匹配就不添加，如果没输入关键词“”,那么默认搜索全部
                        if(xrp.getAttributeValue(1).indexOf(editText.getText().toString()) != -1)
                        {
//0，标识id，1标识名称
                            map.put(xrp.getAttributeValue(1),
                                    xrp.getAttributeValue(0));
                            list.add(xrp.getAttributeValue(1));
                        }
                    }
                }
//搜索过第一个信息后，接着搜索下一个
                xrp.next();
            }

        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //适配器类
    class myadapter extends BaseAdapter {

        Holder holder;
        @Override
        public int getCount() {
// TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
// TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
// TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup arg2) {
// TODO Auto-generated method stub
            holder = new Holder();
            if(view == null)
            {
                view = LayoutInflater.from(TimeListActivity.this).inflate(R.layout.time_item, null);
                holder.view = (TextView)view.findViewById(R.id.tvCountry);
                view.setTag(holder);
            }
            else
            {
                holder = (Holder) view.getTag();
            }
            holder.view.setText(list.get(pos));
            return view;
        }
        class Holder{
            public TextView view;
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
// TODO Auto-generated method stub
//点击后显示当前时区的时间
        TextView textView = (TextView)view.findViewById(R.id.tvCountry);
        String timeEn=map.get(textView.getText().toString());//时区样式。
        String timeCh = list.get(arg2);//时区
        getTime(map.get(textView.getText().toString()));
        String date = YMD;
        String time = tTime;
        Intent intent=new Intent();
        intent.putExtra("timeen",timeEn);
        intent.putExtra("timech",timeCh);
//        intent.putExtra("timeymd",date);
//        intent.putExtra("timete",time);
        setResult(41,intent);
        finish();


    }

    @Override
    public void afterTextChanged(Editable arg0) {
// TODO Auto-generated method stub
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
// TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
// TODO Auto-generated method stub
//当输入框改变时，重新获取数据并通知列表更新
        getdata();
        name.notifyDataSetChanged();
    }

}