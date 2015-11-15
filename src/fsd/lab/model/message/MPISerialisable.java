/**
 * 
 */
package fsd.lab.model.message;

/**
 * @author Robert
 *
 */
public interface MPISerialisable<T> {
	byte[] serialise();
	T deserialise(byte[] bytes);
}
