package test.drawtest.one;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 入口activity
 * 2014年7月15日
 */
public class MyActivity extends Activity {
	//自定义的视图类
	private MyView mv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		mv = (MyView) findViewById(R.id.view);
		Button Btn1 = (Button) findViewById(R.id.button);// 获取按钮资源
		Btn1.setOnClickListener(new Button.OnClickListener() {// 创建监听
			public void onClick(View v) {
				// 设置不可滚动
				mv.flagStop = 1;
			}
		});
	}

}
