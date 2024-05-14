import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

/**
 * @author Marius
 * Simulate the Wave equation. There are two different ways to do it in this class:
 * 			- 	Using points from the parameter, creating cubic hermite splines between
 * 				them, making an interpolation between the points.
 * 			- 	Using a fixed mathematical polynomial.
 * 
 * This class creates a Graph object which contains all the information about the
 * solution of the Wave equation. This Graph object can then be passed onto the Drawer
 * which will draw up a frame of the Graph. Iterate the Graph to get different states.
 * 
 * Assumptions that need to be satisfied:
 * 			- u(0,t) = u(L,t) = 0.
 * 			- u(x,0) = f(x), with f(x) having f(0) = f(L) = 0.
 * 			- du/dt at (x,0) = 0 for all x.
 */
public class WaveDampingSimulation implements Simulation {
	
	/**
	 * Points used to create an cubic hermite interpolation.
	 */
	List<Vertex> m_points;
	
	/**
	 * The width of the rectangle when calculating the integral of the mathematical function.
	 * Can be thought of as the accuracy of the integral approximation.
	 */
	float m_deltaX;
	
	/**
	 * Total number of terms to use from the Fourier series.
	 */
	int m_fourierN;
	
	/**
	 * The dampening factor to dampen the function over time.
	 */
	float m_alpha;
	
	/**
	 * The velocity of propagation of waves along t he sting.
	 */
	float m_beta;
	
	/**
	 * Domain goes from x in [0, m_L].
	 */
	float m_L;
		
	public WaveDampingSimulation(List<Vertex> points, float deltaX, int fourierN, float alpha, float beta, float L) {
		m_points = points;
		m_fourierN = fourierN;
		m_L = L;
		m_deltaX = deltaX;
		m_alpha = alpha;
		m_beta = beta;
		/**
		 * TODO:
		 * create an if statement checking if points is sorted
		 */
	}
	
	/**
	 * This method uses the points given as a parameter to create a spline between the points.
	 * The spline is thus an approximation of a graph being described by the points. 
	 */
	@Override
	public Graph setupSimulation() {
		if (m_points.isEmpty())
			throw new IllegalArgumentException("The list containing points is empty!");
			
		//creating cubic hermite splines between the points
		List<Polynomial> polynomials = new ArrayList<>();
		for(int i = 0; i < m_points.size()-1; i++) {
			//m_points.size()-1 to skip the last vertex because every spline has been created
			Vertex point;
			Vertex pointPlus;
			Vertex pointPlusPlus;
			double derivative; 		
			double derivativePlus;
			/**
			 * Check if this is the last spline that is going to be created.
			 * Due to being the last spline, the derivativePlus is a free variable.
			 */
			if (i == m_points.size()-2) {
				point		 	= m_points.get(i);
				pointPlus 		= m_points.get(i+1);
				derivative 		= (pointPlus.getY() - point.getY()) / (pointPlus.getX() - point.getX());
				derivativePlus	= derivative;
			}
			else {
				point		 	= m_points.get(i);
				pointPlus 		= m_points.get(i+1);
				pointPlusPlus 	= m_points.get(i+2);
				derivative 		= (pointPlus.getY() - point.getY()) / (pointPlus.getX() - point.getX());
				derivativePlus	= (pointPlusPlus.getY() - pointPlus.getY()) / (pointPlusPlus.getX() - pointPlus.getX());
			}
			/**
			 * Restriction order: (explains data setup):
			 * 		-	Matching with first point, f(x_i) = y_i
			 * 		-	Matching with second point, f(x_(i+1)) = y_(i+1)
			 * 		-	Matching derivative with first point f'(x_i) = derivative
			 * 		-	Matching derivativePlus with second point f'(x_(i+1)) = derivativePlus
			 */
			//extract values for readability
			double x 			= point.getX(),			y		  = point.getY();
			double xPlus 		= pointPlus.getX(),		yPlus	  = pointPlus.getY();
			//creating matrix
			double[][] matrixData = new double[][] {
				{ 1, x    , Math.pow(x, 2),     Math.pow(x, 3)     	   },
				{ 1, xPlus, Math.pow(xPlus, 2), Math.pow(xPlus, 3)     },
				{ 0, 1    , 2 * x			  , 3 * Math.pow(x, 2)     },
				{ 0, 1    , 2 * xPlus         , 3 * Math.pow(xPlus, 2) }
			};			
			SimpleMatrix m = new SimpleMatrix(matrixData);
			//matrix created			
			//creating vector
			double[][] vectorData = new double[][] {
				{ y				 },
				{ yPlus			 },
				{ derivative	 },
				{ derivativePlus }
			};
			SimpleMatrix v = new SimpleMatrix(vectorData);
			//vector created
			SimpleMatrix inverse = m.invert();
			SimpleMatrix result = inverse.mult(v);
			//creating polynomial
			double[] pCoefficients = new double[4];
			for(int j = 0; j < result.getNumElements(); j++)
				pCoefficients[j] = result.get(j); 

			Polynomial p = new Polynomial(pCoefficients, (float)x, (float)xPlus);
			polynomials.add(p);
		}
		
		List<Double> fourierCoefficients = new ArrayList<>();
		//finding Fourier coefficients
		for (int n = 1; n < m_fourierN+1; n++) {			
			double c_n = MathM.calculateNthCoefficient(n, m_deltaX, polynomials, m_L);
			fourierCoefficients.add(c_n);
		}
		//coefficients found			
		//creating new graph
		Graph graph = new GraphWaveDamping(fourierCoefficients, m_alpha, m_beta, m_deltaX, m_L);

		return graph;
	}
}