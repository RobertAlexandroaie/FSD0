/**
 * 
 */
package fsd.lab.model.message;

import fsd.lab.model.AbstractMPISerializable;

/**
 * @author Robert
 *
 */
public class HirschbergSinclairMessage extends AbstractMPISerializable {
	private MessageType messageType = MessageType.PROBE;
	private int senderId;
	private int phase;
	private int distance;

	/**
	 * 
	 * @param messageType
	 * @param senderId
	 * @param phase
	 * @param distance
	 */
	public HirschbergSinclairMessage(MessageType messageType, int senderId, int phase, int distance) {
		super();
		this.messageType = messageType;
		this.senderId = senderId;
		this.phase = phase;
		this.distance = distance;
	}

	/**
	 * 
	 * @param messageType
	 * @param senderId
	 */
	public HirschbergSinclairMessage(MessageType messageType, int senderId) {
		super();
		this.messageType = messageType;
		this.senderId = senderId;
	}

	/**
	 * @param messageType
	 * @param senderId
	 * @param phase
	 */
	public HirschbergSinclairMessage(MessageType messageType, int senderId, int phase) {
		super();
		this.messageType = messageType;
		this.senderId = senderId;
		this.phase = phase;
	}

	private HirschbergSinclairMessage() {
	}

	public static HirschbergSinclairMessage getInstance(byte[] bytes) {
		return (HirschbergSinclairMessage) new HirschbergSinclairMessage().deserialise(bytes);
	}

	/**
	 * @return the messageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the senderId
	 */
	public int getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId
	 *            the senderId to set
	 */
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return the phase
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(int phase) {
		this.phase = phase;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * 
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see fsd.lab.model.AbstractMPISerializable#getSelf()
	 */
	@Override
	public AbstractMPISerializable getSelf() {
		return this;
	}
}
