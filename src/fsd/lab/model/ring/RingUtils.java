/**
 * 
 */
package fsd.lab.model.ring;

/**
 * @author Robert
 *
 */
public class RingUtils {
	public static MessageSourceDirection getMessageSourceDirection(RingTopology<?> ring, RingElement<?> referenceElement,
			RingElement<?> neighbour) throws ElementNotInRingException, EmptyRingException {
		if(!(ring.contains(referenceElement)&&ring.contains(neighbour))) {
			throw new ElementNotInRingException();
		}
		return getMessageSourceDirection(ring.size(), referenceElement.getId(), neighbour.getId());
	}

	public static MessageSourceDirection getMessageSourceDirection(RingTopology<?> ring, int referencePosition,
			int neighbourPosition) throws EmptyRingException {
		return getMessageSourceDirection(ring.size(), referencePosition, neighbourPosition);
	}

	public static MessageSourceDirection getMessageSourceDirection(int ringSize, int referencePosition, int neighbourPosition) throws EmptyRingException {
		if(ringSize == 0) {
			throw new EmptyRingException();
		}
		MessageSourceDirection position = MessageSourceDirection.FROM_LEFT;
		int realReferencePosition = getModuloValue(ringSize, referencePosition);
		int realNeighbourPosition = getModuloValue(ringSize, neighbourPosition);
		if (realNeighbourPosition < realReferencePosition) {
			position = MessageSourceDirection.FROM_RIGHT;
		} else if (realNeighbourPosition == realReferencePosition) {
			position = MessageSourceDirection.SAME;
		}
		return position;
	}

	public static int getModuloValue(int base, int value) {
		while (value < 0) {
			value += base;
		}
		while (value >= base) {
			value %= base;
		}
		return value;
	}
}
