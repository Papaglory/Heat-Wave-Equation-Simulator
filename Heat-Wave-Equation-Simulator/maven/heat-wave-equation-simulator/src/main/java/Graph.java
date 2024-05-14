import java.util.List;

public interface Graph {

	/**
	 * Sets the timer m_time to time such that getVertices() can be called to get a
	 * description of how Graph looks like given the parameter time. 
	 * 
	 * @param time 		The new time.
	 */
	public void setGraph(double time);
	
	public List<Vertex> getVerticies();
}