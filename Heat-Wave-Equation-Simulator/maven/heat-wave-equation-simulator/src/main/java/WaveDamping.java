import java.util.List;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author Marius
 * 
 * Description of main():
 * 		- Setup of different parameters concerning the wave equation.
 * 		- Asks the WaveSimulation to create a graph containing the solution.
 * 		- Asks Drawer to create a canvas to draw upon.
 * 		- Runs the simulation loop, increasing time t, giving the graph an animation.
 * 
 * CAUTION:
 * 		- 	If the fourierTerms gets to large compared to deltaX, the integrals in MathM will
 * 			become to inaccurate compared to what is needed to solve for the fourierTerms with
 * 			large n value. 
 *
 */
public class WaveDamping implements Equation {
	
	private float deltaTime; 

	public void setupWaveDampingEquation(List<Vertex> points, float deltaX, int fourierTerms, float alpha, float beta, float L,
	int SCREENWIDTH, int SCREENHEIGHT, int SCALE, int PADDINGX, int PADDINGY) {

		deltaTime = deltaX;

		// Simulation setup
		Simulation simulation = new WaveDampingSimulation(points, deltaX, fourierTerms, alpha, beta, L);
		Graph graph = simulation.setupSimulation();

		// Window setup
		Drawer.createWindow(this, graph, SCREENWIDTH, SCREENHEIGHT, SCALE, PADDINGX, PADDINGY);
	}
	
	/**
	 * Run the simulation, simulating the graph.
	 */
	@Override
	public void runSimulation(Graph graph) {
		double startTime = System.currentTimeMillis();
		final double[] time = {0d}; // Wrap time in an array
		
		/*
		 * This is basically a while loop that lets us do other java swing stuff at the same time. 
		 * Not completely sure how it works tho..
		 */
		Timer timer = new Timer((int) (deltaTime), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time[0] = nextFrame(graph, deltaTime, startTime, time[0]); // Access time through the array
			}
		});
		timer.start();
	}
	
	/**
	 * This method creates a new frame given the current time in the simulation.
	 * 
	 * @param graph			The type of graph to simulate over time.
	 * @param deltaTime		Time between the frames.
	 * @param startTime		Reference to when the simulation started.
	 * @param time			Current time in the simulation.
	 * @return				The new time after the frame has been created.
	 */
	private double nextFrame(Graph graph, double deltaTime, double startTime, double time) {
		if (time < System.currentTimeMillis() - startTime) {
			time = (System.currentTimeMillis() - startTime);
			time += deltaTime;
			// Update the graph with the new time
			graph.setGraph(time / 1000);
	
			Drawer.UpdateFrame((int) (time / 1000));
		}
		return time;
	}
}