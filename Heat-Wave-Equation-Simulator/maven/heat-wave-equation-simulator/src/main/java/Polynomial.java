public class Polynomial {

	/**
	 * Contains coefficients of the polynomial. Index 0 corresponding to x of degree 0,
	 * 1 corresponding to x^1, 2 to x^2 and so on.
	 */
	double[] m_coefficients;
	int m_size;
	float m_lowerDomain;
	float m_upperDomain;

	public Polynomial(double[] coefficients, float lowerDomain, float upperDomain) {
		m_coefficients = coefficients;
		m_size = coefficients.length;
		m_lowerDomain = lowerDomain;
		m_upperDomain = upperDomain;
	}
	
	public int getSize() 					{ return m_size; }
	public float getLowerDomain()			{ return m_lowerDomain; }
	public float getUpperDomain() 			{ return m_upperDomain; }
	public double getNthCoefficient(int n) 	{ return m_coefficients[n]; }

	@Override
	public String toString() {
		if (m_size == 0)
			throw new IllegalArgumentException("This polynomial is empty!");
		
		String name = String.valueOf(m_coefficients[0]);
		for (int i = 1; i < m_size; i++) {
			String term = " + " + String.valueOf(m_coefficients[i]) + "x^" + String.valueOf(i);
			name += term;
		}
		return name;
	}	
}