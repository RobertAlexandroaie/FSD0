/**
 * 
 */
package fsd.lab.model.ring;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Robert
 *
 */
public class RingTopology<T> extends CopyOnWriteArraySet<RingElement<T>> {

	private int leader = -1;

	/**
	 * @return the leader
	 */
	public synchronized int getLeader() {
		return leader;
	}

	/**
	 * @param leader
	 *            the leader to set
	 */
	public synchronized void setLeader(int leader) {
		this.leader = leader;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param value
	 * @return
	 */
	public boolean addRingElementWithValue(T value) {
		return add(new RingElement<T>(size(), value));
	}

	/**
	 * 
	 * @param collection
	 * @return
	 */
	public boolean addAllRingElementsWithValues(Collection<? extends T> collection) {
		boolean isOk = true;
		for (T element : collection) {
			isOk = addRingElementWithValue(element);
			if (!isOk) {
				break;
			}
		}
		return isOk;
	}

	/**
	 * 
	 * @param current
	 * @return
	 * @throws EmptyRingException
	 */
	public RingElement<T> getRightNeighbour(RingElement<T> current) throws EmptyRingException {
		int id = current.getId();
		int size = size();
		int neighbourIndex = (id + size + 1) % size;
		return getElement(neighbourIndex);
	}

	/**
	 * 
	 * @param current
	 * @return
	 * @throws EmptyRingException
	 */
	public RingElement<T> getLeftNeighbour(RingElement<T> current) throws EmptyRingException {
		int id = current.getId();
		int size = size();
		int neighbourIndex = (id + size - 1) % size;
		return getElement(neighbourIndex);
	}

	@SuppressWarnings("unchecked")
	public RingElement<T> getElement(int index) throws EmptyRingException {
		RingElement<T> element = null;
		if (isEmpty()) {
			throw new EmptyRingException();
		} else {
			try {
				element = (RingElement<T>) toArray()[index];
			} catch (ClassCastException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return element;
	}
}
