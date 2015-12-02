package com.raymond.demo.andfixdemo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_second);
        Toast.makeText(this,"hello error",Toast.LENGTH_LONG).show();

    }
    public void onEventMainThread(String event){
        Toast.makeText(this,"Event received:"+event,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
