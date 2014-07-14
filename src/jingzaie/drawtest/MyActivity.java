package jingzaie.drawtest;


import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MyActivity extends Activity {

    private MyView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
         mv= (MyView)findViewById(R.id.view);
        Button Btn1 = (Button)findViewById(R.id.button);//获取按钮资源
        Btn1.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
               mv.flagStop=1;
               // DrawImage dr = new DrawImage();
               // new Thread(dr).start();


            }

        });
    }




}
