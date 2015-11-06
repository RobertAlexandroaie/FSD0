/**
 * 
 */
package fsd.lab.ring.behaviour;

/**
 * @author Robert
 *
 */
public interface MPISerialisbale {
	byte[] serialise();
	Object deserialise();
}
