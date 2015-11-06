package fsd.lab.ring;
/**
 * 
 */

/**
 * @author Robert
 *
 */
public class RingElement<T> {
	private int id;
	private T value;
	private RingElement<T> left;
	private RingElement<T> right;
	private boolean asleep;

	public RingElement(int id, T value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the left
	 */
	public RingElement<T> getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(RingElement<T> left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public RingElement<T> getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(RingElement<T> right) {
		this.right = right;
	}

	/**
	 * @return the asleep
	 */
	public boolean isAsleep() {
		return asleep;
	}

	/**
	 * @param asleep
	 *            the asleep to set
	 */
	public void setAsleep(boolean asleep) {
		this.asleep = asleep;
	}

}
