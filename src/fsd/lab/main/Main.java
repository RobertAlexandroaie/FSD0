/**
 * 
 */
package fsd.lab.main;

import fsd.lab.ring.RingTopology;
import mpi.MPI;

/**
 * @author Robert
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MPI.Init(args);

		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		int dest = 0;
		int tag = 333;

		RingTopology<Long> ringTopology = new RingTopology<>();
		
		for(int i=0;i<size;i++) {
			ringTopology.addRingElementWithValue(Math.round(Math.random()*100));
		}
		
		
		
		if (rank != 0) {
			System.out.println("Send data\n");
			MPI.COMM_WORLD.Send(null, 0, 0, MPI.DOUBLE, dest, tag);
		} else {
			System.out.println("Recieve data\n");
			MPI.COMM_WORLD.Recv(null, 0, 0, MPI.DOUBLE, 1, tag);
			System.out.println("Data recieved: ");
		}

		MPI.Finalize();
	}

}
