/**
 * gUtill class for ECSE 202 assignments
 * @author Namdar Kabolinejad
 */


public class gUtil {
	
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int OFFSET = 200;
	private static final int SCALE = HEIGHT/100;
	
	/* Methods */
	/** method to change meters to pixels using the SCALE
	 * 
	 * @param the size in meters
	 * @return the size in pixels
	 */
	static double meter2Pixel (double meters) 
	{
		return meters * SCALE;	
	}
	
	/** method for changing X meter coordinates to pixel coordinates
	 * 
	 * @param Y the Y position of the ball
	 * @param bSize the radius of the ball in meters
	 * @return the Y coordinates of the ball in pixels
	 */
	static double pixelY(double Y)
	{
		return (double) HEIGHT-(Y*SCALE);
	}
	
	/** method for changing X meter coordinates to pixel coordinates
	 * 
	 * @param X the X position of the ball
	 * @param bSize the radius of the ball in meters
	 * @return the X coordinates of the ball in pixels
	 */
	static double pixelX(double X)
	{
		return (double) X*SCALE;
	}
}
