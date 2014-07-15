package jingzaie.drawtest;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by jingzaie on 14-7-9.
 */
public class MyGestureListener extends SimpleOnGestureListener {
	private MyView myView;

	public int flagStop = 0;
	public ChartRect chartRect = new ChartRect();

	private float dx = 0;
	private float ax = 0;

	private float targetleft; // 目标x轴左距离
	private float targetright;// 目标x轴右距离
	private double sping = 0.01; // 弹动系数
	private double sping1 = 0.4;
	private double friction = 0.97; // 摩擦力系数
	private float springLength = 100;// 弹簧的长度
	// private RectF rt = null;
	private float margin = 10;

	public MyGestureListener(MyView myView) {
		this.myView = myView;
		this.chartRect = myView.chartRect;
		this.targetleft = myView.targetleft;
		this.targetright = myView.targetright;
	}

	@Override
	public boolean onDown(MotionEvent e) {

		flagStop = 1;

		return true;

	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,

	float distanceX, float distanceY) {

		if (chartRect.getLeft() > targetleft
				|| chartRect.getRight() < targetright) {
			chartRect
					.setX(chartRect.getX() + (-1 * distanceX) * (float) sping1);
		} else {
			chartRect.setX(chartRect.getX() + (-1 * distanceX));
		}
		chartRect.setLeft(chartRect.getX());
		chartRect.setRight(chartRect.getX() + chartRect.getWidth());
		myView.postInvalidate();

		// DrawImage dr = new DrawImage(1);
		// dr.start();

		return true;

	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,

	float velocityY) {
		flagStop = 0;

		DrawImage dr = new DrawImage(velocityX / 200);
		dr.start();

		return false;

	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {

		return false;

	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {

		return false;

	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {

		return false;

	}

	public class DrawImage extends Thread {
		// private double speed = 0.5;
		private float vx;

		public DrawImage(float vx) {
			if (Math.abs(vx) > 30) {
				if (vx > 0) {
					this.vx = 30;
				} else {
					this.vx = -30;
				}
			} else {
				this.vx = vx;
			}

		}

		@Override
		public void run() {

			while (true) {
				if (Math.abs(vx) > 10) {
					vx *= (float) 0.95;
					chartRect.setX(chartRect.getX() + vx);
					chartRect.setLeft(chartRect.getX());
					chartRect.setRight(chartRect.getX() + chartRect.getWidth());
					myView.postInvalidate();
					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {

					break;

				}
			}
			vx = 0;
			if (chartRect.getX() > targetleft) {
				tandong(0);
			} else if ((chartRect.getX() + chartRect.getWidth()) < targetright) {
				tandong(1);

			}
		}

		private void tandong(int flag1) {

			if (flag1 == 0) {
				while (true) {
					if (flagStop == 1) {
						break;
					}
					dx = targetleft - chartRect.getX();
					ax = dx * (float) sping;
					vx += ax;
					vx *= (float) friction;
					if (dx >= 0) {
						dx = 0;
						vx = 0;
						chartRect.setX(targetleft);
					} else {
						chartRect.setX(chartRect.getX() + vx);
					}

					chartRect.setLeft(chartRect.getX());
					chartRect.setRight(chartRect.getX() + chartRect.getWidth());
					myView.postInvalidate();
					if (dx == 0)
						break;
					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			} else {
				while (true) {
					if (flagStop == 1) {
						break;
					}
					dx = targetright
							- (chartRect.getX() + chartRect.getWidth());
					ax = dx * (float) sping;
					vx += ax;
					vx *= (float) friction;
					if (dx <= 0) {
						dx = 0;
						chartRect.setX(0 - (chartRect.getWidth() - (myView
								.getWidth() - springLength - margin)));
					} else {
						chartRect.setX(chartRect.getX() + vx);
					}
					chartRect.setLeft(chartRect.getX());
					chartRect.setRight(chartRect.getX() + chartRect.getWidth());
					myView.postInvalidate();
					if (dx == 0)
						break;
					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}
}