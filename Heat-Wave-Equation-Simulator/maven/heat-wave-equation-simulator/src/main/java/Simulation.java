
public interface Simulation {

	/**
	 * Given the parameters, this method returns a Graph object containing the information
	 * about the simulation in a graph style. Thus it can be later used by the drawer to
	 * draw the graph given a time t.
	 * 
	 * @return	A Graph that contains the information about the simulation such that it is
	 * 			runnable (through time).
	 */
	public Graph setupSimulation();
}