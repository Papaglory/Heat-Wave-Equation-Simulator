import java.util.List;

/**
 * @author Marius
 * Contains the mathematics to solve the heat and wave equation, including the 
 * wave damping equation. This class finds the coefficients. It is shared between
 * the heat and wave equation because it is the exact same procedure to find the 
 * coefficients given the assumptions found in the log.
 */
public class MathM {
	
	/**
	 * Formula for coefficient c_n is:
	 * c_n = 2/L * integral from 0 to L [f(x) * sin(n*pi*x/L) dx]
	 * 
	 * Uses a for loop to integrate all the polynomials (splines) in the polys list.
	 * 
	 * @param n 		The nth coefficient to calculate in the Fourier series.
	 * @param deltaX 	Accuracy of the integral approximation. The width of the rectangle.
	 * @param polys		The polynomials to integrate.
	 * @param L			The length, domain of p is [0, L].
	 * @return 			Returns the value of coefficient c_n belonging to the Fourier series.
	 */
	public static double calculateNthCoefficient(int n, double deltaX, List<Polynomial> polys, float L) {
		double integralValue = 0d;
		for (int i = 0; i < polys.size(); i++) {
			Polynomial p = polys.get(i);
			integralValue += integrate(p, n, deltaX, L);
		}
		
		integralValue = (integralValue * 2) / L;
		return integralValue;
	}
	
	/**
	 * Integrate the expression: p*sin(n*pi*x/L) over the domain of p.
	 * @param p 		Polynomial.
	 * @param n 		The nth coefficient to calculate in the Fourier series.
	 * @param deltaX	Accuracy of the integral approximation. The width of the rectangle.
	 * @return 			the integral of the polynomial multiplied with sin(n*pi*x/L).
	 */
	private static double integrate(Polynomial p, int n, double deltaX, float L) {
		if (p.getSize() == 0)
			throw new IllegalArgumentException("ERROR: Size of array is 0 (empty)");
		
		double integralValue = 0d;
		double xPos = p.getLowerDomain();
		while(xPos < p.getUpperDomain()) {
			//Extract the total height of the function value given the x value: xPos			
			double height = 0d;		
			for(int i = 0; i < p.getSize(); i++)
				height += p.getNthCoefficient(i) * Math.pow(xPos, i) * Math.sin((n*Math.PI*xPos) / L);
			//Multiply the height with the width (rectangle)
			integralValue += height;
			xPos += deltaX;
		}
		
		//multiplying sum of terms by deltaX is the same as multiplying all the terms individually
		integralValue *= deltaX;

		/**
		 * Everything has now been summarized except the very last rectangle due to xPos exceeding
		 * p.getUpperDomain(). A tiny rectangle is missing and is thus being added in the code under.
		 * Need to find a new deltaX equaling the last bit remaining.
		 * Have chosen the xPos as the previous x position plus half of the new delta x. 
		 */
		double prevXPos 	= xPos - deltaX;
		double newDeltaX 	= p.getUpperDomain() - (prevXPos);
		double newXPos 		= prevXPos + (newDeltaX / 2);
		
		double height = 0d;
		for(int i = 0; i < p.getSize(); i++)
			height += p.getNthCoefficient(i) * Math.pow(newXPos, i) * Math.sin((n*Math.PI*newXPos) / L);
		//Multiply the height with the new width (rectangle)
		integralValue += height * newDeltaX;		

		
		if (integralValue == 0)
			throw new IllegalArgumentException("Integral is 0, consider lowering deltaX");
		
		return integralValue;
	}
}