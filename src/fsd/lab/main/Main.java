/**
 * 
 */
package fsd.lab.main;

import fsd.lab.model.message.HirschbergSinclairMessage;
import fsd.lab.model.message.MessageType;
import fsd.lab.model.ring.EmptyRingException;
import fsd.lab.model.ring.MessageSourceDirection;
import fsd.lab.model.ring.RingElement;
import fsd.lab.model.ring.RingTopology;
import fsd.lab.model.ring.RingUtils;
import mpi.MPI;

/**
 * @author Robert
 *
 */
public class Main {

	/**
	 * 
	 */
	private static final int DEFAULT_TAG = 0;
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
				MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, leftId, DEFAULT_TAG);
				MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, rightId, DEFAULT_TAG);
			} else {
				handleReceivedMessages(rank, leftId, rightId, ringTopology);
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
	 * @param currentProcess
	 * @param leftId
	 * @param rightId
	 * @param ringTopology
	 */
	private static void handleReceivedMessages(int currentProcess, int leftId, int rightId,
			RingTopology<?> ringTopology) {
		byte[] messageBytesFromLeft = new byte[MAX_MESSAGE_SIZE];
		MPI.COMM_WORLD.Recv(messageBytesFromLeft, 0, MAX_MESSAGE_SIZE, MPI.BYTE, leftId, DEFAULT_TAG);
		HirschbergSinclairMessage messageFromLeft = HirschbergSinclairMessage.getInstance(messageBytesFromLeft);

		byte[] messageBytesFromRight = new byte[MAX_MESSAGE_SIZE];
		MPI.COMM_WORLD.Recv(messageBytesFromRight, 0, MAX_MESSAGE_SIZE, MPI.BYTE, rightId, DEFAULT_TAG);
		HirschbergSinclairMessage messageFromRight = HirschbergSinclairMessage.getInstance(messageBytesFromRight);

		handleMessage(currentProcess, messageFromLeft, ringTopology, leftId, rightId);
		handleMessage(currentProcess, messageFromRight, ringTopology, leftId, rightId);
	}

	/**
	 * @param currentProcess
	 * @param message
	 * @param ringTopology
	 * @param rightId
	 * @param leftId
	 */
	private static void handleMessage(int currentProcess, HirschbergSinclairMessage message,
			RingTopology<?> ringTopology, int leftId, int rightId) {
		MessageType messageType = message.getMessageType();
		MessageSourceDirection sourceDirection;
		try {
			sourceDirection = RingUtils.getMessageSourceDirection(ringTopology, message.getSenderId(), currentProcess);
			switch (messageType) {
			case PROBE:
				handleProbeMessage(currentProcess, message, sourceDirection, ringTopology, leftId, rightId);
				break;
			case REPLY:
				handleReplyMessage(currentProcess, message, sourceDirection, ringTopology, leftId, rightId);
				break;
			case LEADER:
				handleLeaderMessage(currentProcess, message, sourceDirection, ringTopology, leftId);
				break;
			}
		} catch (EmptyRingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param currentProcess
	 * @param message
	 * @param sourceDirection
	 * @param ringTopology
	 * @param rightId
	 * @param leftId
	 */
	private static void handleProbeMessage(int currentProcess, HirschbergSinclairMessage message,
			MessageSourceDirection sourceDirection, RingTopology<?> ringTopology, int leftId, int rightId) {
		int senderId = message.getSenderId();
		int distance = message.getDistance();
		int phase = message.getPhase();
		int frontier = 1 << phase;
		if (senderId == currentProcess) {
			HirschbergSinclairMessage leaderMessage = new HirschbergSinclairMessage(MessageType.LEADER, senderId);
			byte[] messageBytes = leaderMessage.serialise();
			MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, leftId, DEFAULT_TAG);
		} else if (senderId > currentProcess) {
			if (distance < frontier) {
				HirschbergSinclairMessage newProbeMessage = new HirschbergSinclairMessage(message.getMessageType(),
						senderId, phase, distance + 1);
				byte[] messageBytes = newProbeMessage.serialise();
				int destination;
				if (sourceDirection.equals(MessageSourceDirection.FROM_LEFT)) {
					destination = rightId;
				} else {
					destination = leftId;
				}
				MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, destination, DEFAULT_TAG);
			} else {
				HirschbergSinclairMessage replyMessage = new HirschbergSinclairMessage(MessageType.REPLY, senderId,
						phase);
				byte[] messageBytes = replyMessage.serialise();
				int destination;
				if (sourceDirection.equals(MessageSourceDirection.FROM_RIGHT)) {
					destination = rightId;
				} else {
					destination = leftId;
				}
				MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, destination, DEFAULT_TAG);
			}
		}
	}

	/**
	 * @param currentProcess
	 * @param message
	 * @param sourceDirection
	 * @param ringTopology
	 * @param leftId
	 * @param rightId
	 */
	@SuppressWarnings("unchecked")
	private static void handleReplyMessage(int currentProcess, HirschbergSinclairMessage message,
			MessageSourceDirection sourceDirection, RingTopology<?> ringTopology, int leftId, int rightId) {
		int senderId = message.getSenderId();
		int phase = message.getPhase();
		if (senderId != currentProcess) {
			byte[] messageBytes = message.serialise();
			int destination;
			if (sourceDirection.equals(MessageSourceDirection.FROM_LEFT)) {
				destination = rightId;
			} else {
				destination = leftId;
			}
			MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, destination, DEFAULT_TAG);
		} else if (senderId == currentProcess) {
			try {
				RingElement<Long> currentRingElement = (RingElement<Long>) ringTopology.getElement(currentProcess);
				int repliesReceived = currentRingElement.getRepliesReceived();
				currentRingElement.setRepliesReceived(repliesReceived++);
				if (repliesReceived > 1) {
					HirschbergSinclairMessage nextPhaseProbeMessage = new HirschbergSinclairMessage(MessageType.PROBE,
							senderId, phase + 1, 1);
					byte[] messageBytes = nextPhaseProbeMessage.serialise();
					MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, leftId, DEFAULT_TAG);
					MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, rightId, DEFAULT_TAG);
				}
			} catch (EmptyRingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param currentProcess
	 * @param message
	 * @param sourceDirection
	 * @param ringTopology
	 * @param leftId
	 */
	private static void handleLeaderMessage(int currentProcess, HirschbergSinclairMessage message,
			MessageSourceDirection sourceDirection, RingTopology<?> ringTopology, int leftId) {
		int senderId = message.getSenderId();
		ringTopology.setLeader(senderId);
		if (senderId != currentProcess) {
			HirschbergSinclairMessage newLeaderMessage = new HirschbergSinclairMessage(MessageType.LEADER, senderId);
			byte[] messageBytes = newLeaderMessage.serialise();
			MPI.COMM_WORLD.Send(messageBytes, 0, messageBytes.length, MPI.BYTE, leftId, DEFAULT_TAG);
		}
	}
}
