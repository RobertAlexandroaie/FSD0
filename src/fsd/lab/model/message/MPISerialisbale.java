/**
 * 
 */
package fsd.lab.model.message;

/**
 * @author Robert
 *
 */
public interface MPISerialisbale<T> {
	byte[] serialise();
	T deserialise(byte[] bytes);
}
