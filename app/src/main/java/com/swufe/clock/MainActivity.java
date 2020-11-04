package com.swufe.clock;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ActivityGroup {
    private Context mContex = this;
    private TabHost tabHost;
    private String TAB1 = "闹钟";
    private String TAB2 = "时钟";
    private String TAB3 = "秒表";
    private String TAB4 = "计时器";
    private List<LinearLayout> itemList;
    private List<ImageView> imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();//启动
        tabHost.setup(this.getLocalActivityManager());
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getMenuItem(R.drawable.tab1_ispressed, TAB1)).setContent(new Intent(this, AlarmActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getMenuItem(R.drawable.tab2_ispressed, TAB2)).setContent(new Intent(this, TimerActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(getMenuItem(R.drawable.tab3_ispressed, TAB3)).setContent(new Intent(this, SecondTimerActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(getMenuItem(R.drawable.tab4_ispressed, TAB4)).setContent(new Intent(this, CalculatorActivity_selected.class)));

//        //标签切换事件处理，setOnTabChangedListener
//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            // tabId是newTabSpec第一个参数设置的tab页名，并不是layout里面的标识符id
//            public void onTabChanged(String tabId) {
//                if (tabId.equals("tab1")) {   //第一个标签
//                    Toast.makeText(MainActivity.this, "点击标签页一", Toast.LENGTH_SHORT).show();
//                }
//                if (tabId.equals("tab2")) {   //第二个标签
//                    Toast.makeText(MainActivity.this, "点击标签页二", Toast.LENGTH_SHORT).show();
////                    Intent intent=new Intent(MainActivity.this,TimerActivity.class);
////                    startActivity(intent);
//                }
//                if (tabId.equals("tab3")) {   //第三个标签
//                    Toast.makeText(MainActivity.this, "点击标签页三", Toast.LENGTH_SHORT).show();
//                }
//                if (tabId.equals("tab4")) {   //第四个标签
//                    Toast.makeText(MainActivity.this, "点击标签页四", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

//
    public View getMenuItem(int imgID, String textID) {
        View linearLayout = (View) LayoutInflater.from(mContex).inflate(R.layout.tab_item, null);
        ImageView imgView = (ImageView) linearLayout.findViewById(R.id.icon);
        imgView.setBackgroundResource(imgID);
        TextView textView = (TextView) linearLayout.findViewById(R.id.name);
        textView.setText(textID);
        //itemList.add(linearLayout);
        return linearLayout;
    }


}