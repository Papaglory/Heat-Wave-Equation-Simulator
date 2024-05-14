import java.util.ArrayList;
import java.util.List;

/**
 * @author Marius
 * 
 * This Graph holds the coefficients for the Fourier Series. The length of m_coefficients is
 * the amount of terms that is used.
 * 
 * The formula for the Fourier Series / Heat Equation is the following (this is the terms the
 * coefficients correspond to):
 * 		Summation from n = 1 to infinity [c_n * e^(-n^2*PI^2*alpha^2*t/L^2)*sin(n*PI*x/L)].
 * 			- Alpha is the thermal diffusivity constant.
 * 			- c_n is the coefficient belonging to term n.
 * 			- L is the length of the function.
 * 			- x is the position in the x-axis.
 * 			- t is time.
 */
public class GraphHeat implements Graph {
	
	List<Vertex> m_vertices;
	List<Double> m_coefficients;
	double m_time;
	
	/**
	 * The thermal diffusivity constant. Look up Heat equation for tables and more. 
	 */
	float m_alpha;
	
	/**
	 * This is the accuracy to extract the points used for drawing.
	 */
	float m_deltaX;
	
	float m_L;
	
	public GraphHeat(List<Double> coefficients, float alpha, float deltaX, float L) {
		m_coefficients = coefficients;
		m_alpha = alpha;
		m_deltaX = deltaX;
		m_L = L;
		m_time = 0;
		//update for time = 0
		updateVertices();
	}
	
	/**
	 * Sets the timer m_time to time such that getVertices() can be called to get a
	 * description of how GraphHeat looks like given the parameter time. 
	 * 
	 * @param time 		The new time.
	 */
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
				y += m_coefficients.get(n) * Math.pow(Math.E, powerOfE) * Math.sin((n+1) * Math.PI * xPos / m_L);
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