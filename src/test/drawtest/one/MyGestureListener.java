package test.drawtest.one;

import test.drawtest.one.MyView.DrawThread;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by jingzaie on 14-7-9.
 */
public class MyGestureListener extends SimpleOnGestureListener {
	private MyView myView;

	public MyGestureListener(MyView myView) {
		this.myView = myView;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		DrawThread dr = myView.new DrawThread(4, distanceX);
		dr.start();
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		DrawThread dr = myView.new DrawThread(3, velocityX / 200);
		dr.start();
		return false;
	}

}