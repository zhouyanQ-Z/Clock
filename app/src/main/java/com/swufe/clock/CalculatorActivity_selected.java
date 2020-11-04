package com.swufe.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity_selected extends Activity implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter,
        NumberPicker.OnScrollListener {
    private NumberPicker hourPicker, minutePicker, secondPicker;
    private TextView tvStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab4_activity);


        hourPicker = (NumberPicker) findViewById(R.id.hourpicker);
        minutePicker = (NumberPicker) findViewById(R.id.minutepicker);
        secondPicker = (NumberPicker) findViewById(R.id.secondpicker);
        tvStart = (TextView) findViewById(R.id.start_text);

        init();

        tvStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String hour = String.valueOf(hourPicker.getValue());// 时
                String minute = String.valueOf(minutePicker.getValue());// 秒
                String second = String.valueOf(secondPicker.getValue());// 分
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                if (second.length() == 1) {
                    second = "0" + second;
                }
//                Toast.makeText(getContext(), "选择的时间是：" + hour + ":" + minute + ":" + second, Toast.LENGTH_LONG)
//                        .show();
//                result = hour + ":" + minute + ":" + second;
                Intent intent = new Intent(CalculatorActivity_selected.this,CalculatorActivity_play.class);
                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);
                intent.putExtra("second",second);
                startActivity(intent);
            }
        });


    }


    private void init() {
        hourPicker.setFormatter(this);
        hourPicker.setOnValueChangedListener(this);
        hourPicker.setOnScrollListener(this);
        hourPicker.setMaxValue(23);//最大值
        hourPicker.setMinValue(0);//最小值
        hourPicker.setValue(0);

        minutePicker.setFormatter(this);
        minutePicker.setOnValueChangedListener(this);
        minutePicker.setOnScrollListener(this);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        minutePicker.setValue(0);

        secondPicker.setFormatter(this);
        secondPicker.setOnValueChangedListener(this);
        secondPicker.setOnScrollListener(this);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setValue(0);

    }

    //value的值格式，当值小于10，前面加一个0
    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    //滑动监听事件
    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (scrollState) {
            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:// 后续滑动，停不下来
//                Toast.makeText(this, "后续滑动(飞呀飞，根本停下来)", Toast.LENGTH_LONG)
//                        .show();
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:// 不滑动
//                Toast.makeText(this, "不滑动", Toast.LENGTH_LONG).show();
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滑动中
//                Toast.makeText(this, "滑动中", Toast.LENGTH_LONG)
//                        .show();
                break;
        }

    }

    //值改变监听 oldVal：原来值 ，newVal：改变值
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//        Toast.makeText(
//                this,
//                "原来的值 " + oldVal + "--新值: "
//                        + newVal, Toast.LENGTH_SHORT).show();

    }
}