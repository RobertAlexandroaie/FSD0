/**
 * 
 */
package fsd.lab.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import fsd.lab.model.message.MPISerialisbale;

/**
 * @author Robert
 *
 */
public abstract class AbstractMPISerializable implements MPISerialisbale<AbstractMPISerializable> {

	public abstract AbstractMPISerializable getSelf();

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsd.lab.model.behaviour.MPISerialisbale#serialise()
	 */
	@Override
	public byte[] serialise() {
		byte[] bytes = {};
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutput objectOutput = null;
		try {
			objectOutput = new ObjectOutputStream(baos);
			objectOutput.writeObject(getSelf());
			bytes = baos.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				objectOutput.close();
				baos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return bytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsd.lab.model.behaviour.MPISerialisbale#deserialise(byte[])
	 */
	@Override
	public AbstractMPISerializable deserialise(byte[] bytes) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInput objectInput = null;
		AbstractMPISerializable deserialised = null;
		try {
			objectInput = new ObjectInputStream(bais);
			Object obj = objectInput.readObject();
			deserialised = (AbstractMPISerializable) obj;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return deserialised;
	}
}
