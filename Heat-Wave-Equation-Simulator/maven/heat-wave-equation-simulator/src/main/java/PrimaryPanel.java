import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

/**
 * @author Marius
 * 
 * This is the primary panel where everything is drawn. The Drawer class creates a panel (this panel)
 * and draws everything that is on this panel.
 *
 */
public class PrimaryPanel extends JPanel {
	
	/**
	 * Compiler Warning if this is not present.
	 */
	private static final long serialVersionUID = 1L;

	Graph m_graph;
	
	/**
	 * Origin of the canvas that shows up on the screen.
	 */
	final int m_ORIGINX, m_ORIGINY;
	
	/**
	 * Scales everything up (axis, number line, graph, etc.) or down depending on the value.
	 */
	final int m_SCALE;
	
	/**
	 * Length of the axis in pixels. Both for x and y axis.
	 */
	final int m_COORDINATESYSTEMLENGTH = 900;
	
	/**
	 * Amount of dots on the number line in both axis.
	 */
	final int m_NUMBERLINESIZE = 10;
	
	public PrimaryPanel(Graph graph, int ORIGINX, int ORIGINY, int SCALE) {
		m_graph = graph;
		m_ORIGINX = ORIGINX;
		m_ORIGINY = ORIGINY;
		m_SCALE = SCALE;
	}
	
	/**
	 * When drawing, note that the coordinate system is in "screen mode" / "matrix mode".
	 * Thus, make sure to use minus when increasing and plus when decreasing when dealing
	 * with the y-axis.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		//creating Coordinate system
		Graphics2D gg = (Graphics2D) g;
		gg.setStroke(new BasicStroke(3)); //width size of lines
		g.setColor(Color.BLACK);
		g.drawLine(m_ORIGINX, m_ORIGINY, m_ORIGINX, m_ORIGINY - m_COORDINATESYSTEMLENGTH);
		g.drawLine(m_ORIGINX, m_ORIGINY, m_ORIGINX + m_COORDINATESYSTEMLENGTH, m_ORIGINY);
		//Coordinate System complete
		
		
		//Drawing GraphHeat
		g.setColor(Color.blue);
		List<Vertex> vertices = m_graph.getVerticies();
		for (int i = 0; i < vertices.size()-1; i++) {
			Vertex v = vertices.get(i);
			Vertex w = vertices.get(i+1);
			//Draw a line from current vertex to the next one in the list
			g.drawLine(m_ORIGINX + (int) (v.getX() * m_SCALE), m_ORIGINY - (int) (v.getY() * m_SCALE), m_ORIGINX + (int) (w.getX() * m_SCALE), m_ORIGINY - (int) (w.getY() * m_SCALE));
		}
		//GraphHeat complete  
	
		//creating number line
		g.setColor(Color.red);
		for (int i = 0; i < m_NUMBERLINESIZE; i++)
			g.drawOval((int) (m_ORIGINX + i*m_SCALE), m_ORIGINY, 5, 5);
		
		for (int i = 0; i < m_NUMBERLINESIZE; i++)
			g.drawOval(m_ORIGINX, (int) (m_ORIGINY - i*m_SCALE), 5, 5);
		//number line complete
	}
}