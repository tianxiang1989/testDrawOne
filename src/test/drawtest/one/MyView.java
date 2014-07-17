package test.drawtest.one;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jingzaie on 14-6-25.
 */
public class MyView extends View {
	private Paint paint = new Paint();
	/**是否可以拖动的标识：0可以;1不可以*/
	public int flagStop = 0;
	/**需要拖动的Rect*/
	public ChartRect chartRect = new ChartRect();
	/**手势识别类*/
	private GestureDetector mGestureDetector;
	/**左侧弹簧固定板位置*/
	public float leftSpringLocation;
	/**右侧弹簧固定板位置*/
	public float rightSpringLocation;
	/**弹动系数*/
	private double sping = 0.01;
	/**摩擦力系数 */
	private double friction = 0.95;
	/**弹簧的长度*/
	private float springLength = 100;
	/**外边框*/
	private RectF outsideRect;
	/**外边框的外边距*/
	private float margin = 10;
	/**标识是否需要初始化 */
	private boolean isFirst = true; //

	/*重写View的三个构造方法*/
	public MyView(Context context) {
		super(context);
		init();
	}

	/*重写View的三个构造方法*/
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/*重写View的三个构造方法*/
	public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**初始化*/
	public void init() {
		// 初始化画笔
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE); // 设置画笔为空心(只画边缘)
		paint.setAntiAlias(true); // 去锯齿 (画圆的时候可以看出来了效果)
		paint.setStrokeWidth(4); // 设置线宽

	}

	@Override
	protected void onDraw(android.graphics.Canvas canvas) {
		super.onDraw(canvas);
		if (isFirst) { // 初始化参数
			// 弹簧位置
			leftSpringLocation = margin + springLength;
			rightSpringLocation = getWidth() - margin - springLength;
			// 设置拖动图像的宽高
			chartRect.setHeight(40);
			chartRect.setWidth(1200);
			// 设置拖动图像的左右边距
			chartRect.setLeft(margin + springLength);
			chartRect.setRight(chartRect.getLeft() + chartRect.getWidth());
			mGestureDetector = new GestureDetector(getContext(),
					new MyGestureListener(this));
			isFirst = false;
		}
		/*开始画图*/
		// 画外边框
		outsideRect = new RectF(margin, margin, getWidth() - margin, 400);
		canvas.drawRect(outsideRect, paint);

		// 左弹簧固定点
		canvas.drawLine(leftSpringLocation, 400 - chartRect.getHeight() - 20,
				leftSpringLocation, 400, paint);
		// 右弹簧固定点
		canvas.drawLine(rightSpringLocation, 400 - chartRect.getHeight() - 20,
				rightSpringLocation, 400, paint);
		// 左弹簧线
		canvas.drawLine(outsideRect.left,
				outsideRect.height() - (chartRect.getHeight() / 2),
				chartRect.getLeft(),
				outsideRect.height() - (chartRect.getHeight() / 2), paint);
		// 右弹簧线
		canvas.drawLine(outsideRect.right,
				outsideRect.height() - (chartRect.getHeight() / 2),
				chartRect.getLeft() + chartRect.getWidth(),
				outsideRect.height() - (chartRect.getHeight() / 2), paint);
		int cur = canvas.save();
		canvas.translate(chartRect.getLeft(), 0);

		// 画需要拖动的RectF外边缘
		RectF rt0 = new RectF(0, outsideRect.height() - chartRect.getHeight(),
				chartRect.getWidth(), outsideRect.height());
		canvas.drawRect(rt0, paint);
		// 画可拖动视图的竖分割线
		int num = (int) chartRect.getWidth() / 100;
		for (int i = 1; i < num; i++) {
			float x = i * 100;
			canvas.drawLine(x, 400 - chartRect.getHeight() - 10, x, 400 - 10,
					paint);
		}
		canvas.restoreToCount(cur);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return mGestureDetector.onTouchEvent(event);
		case MotionEvent.ACTION_MOVE:
			return mGestureDetector.onTouchEvent(event);
		case MotionEvent.ACTION_UP:
			// // 判断可拖动的结束位置和弹簧固定点的左右关系，判断是否需要进行弹簧的回滚
			if (chartRect.getLeft() > leftSpringLocation) {
				// 可拖动的组件左侧在左弹簧的固定板右边
				// vx = 0;
				DrawThread dr = new DrawThread(0, 0); // 向左弹回
				dr.start();
			} else if ((chartRect.getLeft() + chartRect.getWidth()) < rightSpringLocation) {
				// 可拖动的组件右侧在右弹簧的固定板左边
				// vx = 0;
				DrawThread dr = new DrawThread(1, 0); // 向右弹回
				dr.start();
			} else {
				// 否则不进行弹簧回滚，进行弹簧的滑动
				return mGestureDetector.onTouchEvent(event);
			}
			break;
		default:
			break;
		}
		return true;
	}

	public class DrawThread extends Thread {
		/**回弹的刷新速率*/
		private long speed = 17;
		// 标识：0是向左弹回;1向右弹回;2fling用的弹回;3onScroll用的弹回
		private int rollingTurn;
		/**速度*/
		private float vx;
		/**弹簧净变化的距离*/
		private float dx;
		/**加速度*/
		private float ax = 0;

		/**
		 * 构造方法
		 * @param flag 标识：0是向左弹回;1向右弹回;2fling用的弹回;3onScroll用的弹回
		 * @param vx
		 */
		public DrawThread(int flag, float vx) {
			rollingTurn = flag;
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
			if (flagStop == 1) {
				return;
			}
			if (rollingTurn == 0) {// 向左弹回
				toLeft();
			} else if (rollingTurn == 1) {// 向右弹回
				toRight();
			} else if (rollingTurn == 3) {// fling中用的
				while (true) {
					if (Math.abs(vx) > 10) {
						vx *= (float) 0.95;
						chartRect.setLeft(chartRect.getLeft() + vx);
						// chartRect.setLeft(chartRect.getLeft());
						chartRect.setRight(chartRect.getLeft()
								+ chartRect.getWidth());
						MyView.this.postInvalidate();
						try {
							Thread.sleep(17);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {

						break;
					}
				}
				if (chartRect.getLeft() > leftSpringLocation) {
					toLeft();
				} else if ((chartRect.getLeft() + chartRect.getWidth()) < rightSpringLocation) {
					toRight();
				}
			} else if (rollingTurn == 4) {// onScroll中用的 0.5[自定义的一个阻力]
				chartRect.setLeft(chartRect.getLeft() + (-1 * vx) * (float) 0.5);
				chartRect.setRight(chartRect.getLeft() + chartRect.getWidth());
				MyView.this.postInvalidate();
			}
		}

		/**
		 * 向右弹回
		 */
		private void toRight() {
			while (true) {
				if (flagStop == 1) {
					break;
				}
				dx = rightSpringLocation
						- (chartRect.getLeft() + chartRect.getWidth());
				ax = dx * (float) sping;
				vx += ax;
				vx *= (float) friction;
				if (dx <= 0) {
					// 可拖动控件的右侧不超过右弹簧的固定点
					chartRect.setRight(getWidth() - margin - springLength);
				} else {
					chartRect.setRight(chartRect.getRight() + vx);
				}
				chartRect.setLeft(chartRect.getRight() - chartRect.getWidth());
				MyView.this.postInvalidate();// 刷新界面
				if (dx <= 0)
					break;
				try {
					// 刷新间隔
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

		/**
		 * 向左弹回
		 */
		private void toLeft() {
			while (true) {
				if (flagStop == 1) {
					break;
				}
				// 左弹簧固定点左侧到可拖动控件的左侧的距离
				dx = leftSpringLocation - chartRect.getLeft();
				ax = dx * (float) sping; // 加速度等于距离乘以sping的值
				vx += ax;// 把加速度累加在速度上
				vx *= friction;
				if (dx >= 0) {
					// vx=0;
					// dx=0;
					// 可拖动控件的左侧不超过左弹簧的固定点
					chartRect.setLeft(leftSpringLocation);
				} else {
					chartRect.setLeft(chartRect.getLeft() + vx); // 把速度加在位置上
				}
				chartRect.setRight(chartRect.getLeft() + chartRect.getWidth());
				MyView.this.postInvalidate(); // 刷新界面
				if (dx >= 0)
					break;
				try {
					// 刷新间隔
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}
}