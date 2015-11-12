/**
 * 
 */
package fsd.lab.main;

import fsd.lab.model.message.HirschbergSinclairMessage;
import fsd.lab.model.message.MessageType;
import fsd.lab.model.ring.EmptyRingException;
import fsd.lab.model.ring.RingElement;
import fsd.lab.model.ring.RingTopology;
import mpi.MPI;

/**
 * @author Robert
 *
 */
public class Main {

	/**
	 * 
	 */
	private static final int MAX_MESSAGE_SIZE = 200;
	/**
	 * 
	 */
	private static final int PROCESSES_COUNT = 5;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RingTopology<Long> ringTopology = new RingTopology<>();

		for (int i = 0; i < PROCESSES_COUNT; i++) {
			ringTopology.addRingElementWithValue(Math.round(Math.random() * 100));
		}

		MPI.Init(args);

		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();

		int tag = 0;

		try {
			RingElement<Long> currentElement = ringTopology.getElement(rank);
			RingElement<Long> leftNeighbour = ringTopology.getLeftNeighbour(currentElement);
			RingElement<Long> rightNeighbour = ringTopology.getRightNeighbour(currentElement);
			int leftId = leftNeighbour.getId();
			int rightId = rightNeighbour.getId();
			if (currentElement.isAsleep()) {
				currentElement.setAsleep(false);
				int phase = 0;
				int distance = 1;
				HirschbergSinclairMessage message = new HirschbergSinclairMessage(MessageType.PROBE, rank, phase,
						distance);
				byte[] messageBytes = message.serialise();
				MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, leftId, tag);
				MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, rightId, tag);
			} else {
				handleNeighbourMessages(tag, leftId, rightId);
			}
		} catch (EmptyRingException e) {
			e.printStackTrace();
		}

		if (rank != 0) {
			MPI.COMM_WORLD.Send(null, 0, 0, MPI.BYTE, 0, 0);
		} else {
			MPI.COMM_WORLD.Recv(null, 0, 0, MPI.BYTE, 1, 0);
		}

		MPI.Finalize();
	}

	/**
	 * @param tag
	 * @param leftId
	 * @param rightId
	 */
	private static void handleNeighbourMessages(int tag, int leftId, int rightId) {
		byte[] messageBytesFromLeft = new byte[MAX_MESSAGE_SIZE];
		MPI.COMM_WORLD.Recv(messageBytesFromLeft, 0, MAX_MESSAGE_SIZE, MPI.BYTE, leftId, tag);
		HirschbergSinclairMessage messageFromLeft = HirschbergSinclairMessage.getInstance(messageBytesFromLeft);

		byte[] messageBytesFromRight = new byte[MAX_MESSAGE_SIZE];
		MPI.COMM_WORLD.Recv(messageBytesFromRight, 0, MAX_MESSAGE_SIZE, MPI.BYTE, rightId, tag);
		HirschbergSinclairMessage messageFromRight = HirschbergSinclairMessage.getInstance(messageBytesFromRight);

		handleMessage(messageFromLeft);
		handleMessage(messageFromRight);
	}

	/**
	 * @param message
	 */
	private static void handleMessage(HirschbergSinclairMessage message) {
		MessageType messageType = message.getMessageType();
		switch (messageType) {
		case PROBE:
			break;
		case REPLY:
			break;
		case LEADER:
			break;
		}
	}
}
