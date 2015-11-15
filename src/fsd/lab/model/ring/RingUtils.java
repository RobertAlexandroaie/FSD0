/**
 * 
 */
package fsd.lab.model.ring;

/**
 * @author Robert
 *
 */
public class RingUtils {
	public static RingElementPosition getRelativePosition(RingTopology<?> ring, RingElement<?> referenceElement,
			RingElement<?> neighbour) throws ElementNotInRingException, EmptyRingException {
		if(!(ring.contains(referenceElement)&&ring.contains(neighbour))) {
			throw new ElementNotInRingException();
		}
		return getRelativePosition(ring.size(), referenceElement.getId(), neighbour.getId());
	}

	public static RingElementPosition getRelativePosition(RingTopology<?> ring, int referencePosition,
			int neighbourPosition) throws EmptyRingException {
		return getRelativePosition(ring.size(), referencePosition, neighbourPosition);
	}

	public static RingElementPosition getRelativePosition(int ringSize, int referencePosition, int neighbourPosition) throws EmptyRingException {
		if(ringSize == 0) {
			throw new EmptyRingException();
		}
		RingElementPosition position = RingElementPosition.LEFT;
		int realReferencePosition = getModuloValue(ringSize, referencePosition);
		int realNeighbourPosition = getModuloValue(ringSize, neighbourPosition);
		if (realNeighbourPosition > realReferencePosition) {
			position = RingElementPosition.RIGHT;
		} else if (realNeighbourPosition == realReferencePosition) {
			position = RingElementPosition.SAME;
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
