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
	private boolean asleep;
	private int repliesReceived;

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
	 * @param id
	 *            the id to set
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
	 * @param value
	 *            the value to set
	 */
	public synchronized void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the asleep
	 */
	public synchronized boolean isAsleep() {
		return asleep;
	}

	/**
	 * @param asleep
	 *            the asleep to set
	 */
	public synchronized void setAsleep(boolean asleep) {
		this.asleep = asleep;
	}

	/**
	 * @return the repliesReceived
	 */
	public synchronized int getRepliesReceived() {
		return repliesReceived;
	}

	/**
	 * @param receivedReplies
	 *            the repliesReceived to set
	 */
	public synchronized void setRepliesReceived(int receivedReplies) {
		this.repliesReceived = receivedReplies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RingElement<T> other = (RingElement<T>) obj;
		if (id != other.id)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
