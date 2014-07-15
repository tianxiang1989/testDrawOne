package jingzaie.drawtest;

/**
 * 实体类
 * 2014年7月15日
 */
public class ChartRect {
	private float x; 
	private float y;
	private float left;
	// private float top; //貌似没用到啊
	private float right;
	// private float bottom;//貌似没用到的说
	private float height = 40;
	private float width = 1200;

	public void setLeft(float left) {
		this.left = left;
	}

	public float getLeft() {
		return left;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public float getRight() {
		return right;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
}
