package test.drawtest.one;

/**
 * 实体类
 * 2014年7月15日
 */
public class ChartRect {
	/**左边缘位置*/
	private float left;
	/**右边缘位置*/
	private float right;
	/**高度*/
	private float height ;//= 40
	/**宽度*/
	private float width ;//= 1200

	public void setLeft(float left) {
		this.left = left;
	}
	/**获取左边缘位置*/
	public float getLeft() {
		return left;
	}
	
	public void setRight(float right) {
		this.right = right;
	}
	/**获取右边缘位置*/
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

}
