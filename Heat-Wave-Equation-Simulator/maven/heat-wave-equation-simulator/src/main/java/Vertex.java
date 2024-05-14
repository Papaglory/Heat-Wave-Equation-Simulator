/**
 * @author Marius
 * A vertex is a tuple (x,y) containing double values.
 */
public class Vertex {
	
	double m_x;
	double m_y;
	
	public Vertex( double x, double y) 	{ m_x = x; m_y = y; }
	public Vertex() 					{ m_x = 0; m_y = 0; }
	
	public double getX() 				{ return m_x; }
	public double getY() 				{ return m_y; }
	
	public void  setX(double x) 		{ m_x = x; }
	public void  setY(double y) 		{ m_y = y; }
}