import java.util.ArrayList;
import java.util.List;

/**
 * @author Marius
 * 
 * This Graph holds the coefficients for the Fourier Series. The length of m_coefficients is
 * the amount of terms that is used.
 * 
 * The formula for the Fourier Series / Wave Damping Equation is the following (this is the
 * terms the coefficients correspond to):
 * 		Summation from n = 1 to infinity [c_n * sin(n*PI*x/L) * cos(n*PI*beta*t/L) * e^(-n*2*PI^2*alpha^2*t/L^2].
 * 			- Alpha is the dampening effect.
 * 			- Beta is the velocity of propagation of waves along t he sting.
 * 			- c_n is the coefficient belonging to term n.
 * 			- L is the length of the function / string.
 * 			- x is the position in the x-axis.
 * 			- t is time.
 * 
 */
public class GraphWaveDamping implements Graph {
	
	List<Vertex> m_vertices;
	List<Double> m_coefficients;
	double m_time;
	
	/**
	 * The thermal diffusivity constant. Look up Heat equation for tables and more.
	 * It is the dampening.
	 */
	float m_alpha;
	
	/**
	 * The velocity of propagation of waves along t he sting. 
	 */
	float m_beta;
	
	/**
	 * This is the accuracy to extract the points used for drawing.
	 */
	float m_deltaX;
	
	float m_L;
	
	public GraphWaveDamping(List<Double> coefficients, float alpha, float beta, float deltaX, float L) {
		m_coefficients = coefficients;
		m_alpha = alpha;
		m_beta = beta;
		m_deltaX = deltaX;
		m_L = L;
		m_time = 0;
		//update for time = 0
		updateVertices();
	}
	
	@Override
	public void setGraph(double time) {
		if (time < 0) throw new IllegalArgumentException("time is negative");		
		m_time = time;
		updateVertices();
	}
	
	/**
	 * Updates vertices for the given time: m_time. Using m_deltaX for accuracy. 
	 */
	private void updateVertices() {
		List<Vertex> temp = new ArrayList<>();
		double xPos = 0;
		//creating vertices until the whole domain has been taken
		while (xPos < m_L) {
			Vertex v = new Vertex();
			v.setX(xPos);
			//finding y value
			double y = 0;
			for (int n = 0; n < m_coefficients.size(); n++) {
				double powerOfE = -Math.pow((n+1), 2) * Math.pow(Math.PI, 2) * Math.pow(m_alpha, 2) * m_time / Math.pow(m_L, 2);
				y += m_coefficients.get((n)) * Math.sin((n+1) * Math.PI * xPos / m_L) * Math.cos((n+1) * Math.PI * m_beta * m_time / m_L) * Math.pow(Math.E, powerOfE);
			}
			v.setY(y);
			temp.add(v);
			xPos += m_deltaX;
		}
		//add the last vertex, due to heat equation assumption, this vertex lies at (m_L, 0)
		Vertex w = new Vertex();
		w.setX(m_L);
		w.setY(0);
		temp.add(w);
		
		//overwrite and save
		m_vertices = temp;
	}	
	
	@Override
	public List<Vertex> getVerticies() { return m_vertices; }
}