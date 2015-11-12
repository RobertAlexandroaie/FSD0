package fsd.lab.model.ring;
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
	public synchronized int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public synchronized void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public synchronized T getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public synchronized void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the left
	 */
	public synchronized RingElement<T> getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public synchronized void setLeft(RingElement<T> left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public synchronized RingElement<T> getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public synchronized void setRight(RingElement<T> right) {
		this.right = right;
	}

	/**
	 * @return the asleep
	 */
	public synchronized boolean isAsleep() {
		return asleep;
	}

	/**
	 * @param asleep the asleep to set
	 */
	public synchronized void setAsleep(boolean asleep) {
		this.asleep = asleep;
	}

}
