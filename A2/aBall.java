import java.awt.Color;
import acm.graphics.GOval;

/**
 * aBall class code of the second assignment for ECSE 202 with prof. Ferrie, Fall 2019.
 * The codes commented with # have been taken from the assignment document.
 * @author Namdar Kabolinejad
 *
 */

public class aBall extends Thread{	
	
	/* Simulation parameters (meter coordinates) */
	
	private static final double g = 9.8; 					// MKS gravitational constant 9.8 m/s^2 #
	private static final double Pi = 3.141592654; 			// To convert degrees to radians #
	private static final double TICK = 0.1; 				// Clock tick duration (sec) #
	private static final double ETHR = 0.01; 				// If either Vx or Vy < ETHR STOP #
	private static final double k = 0.0001  ;				// Parameter for calculating Vt #
	private static final int HEIGHT = 600; 					// distance from top of screen to ground plane #
	private static final int WIDTH = 1200; 					// n.b. screen coordinates #
	private static final double SCALE = HEIGHT/100;  		// pixels per meter #
	
	/* Declaring all the variables */
	
	// Related to the ball
	private double Xi;
	private double Yi;
	private double V0;
	private double theta;
	private double bSize;
	private double bLoss;
	private Color bColor;
	private GOval myBall;
	
    // Related to the while loop and bouncing calculations
	private double X;
	private double Y;
	private double Vx;
	private double Vy;
	private double ScrX;
	private double ScrY;
    private double Vt;
    private double V0x;
    private double V0y;
	private double Xlast;
	private double Ylast;
	private double time;
	private double X0;
    private double KEx;
    private double KEy;
    private double KELast;

	/* Methods */
	/** method to change meters to pixels using the SCALE
	 * 
	 * @param the size in meters
	 * @return the size in pixels
	 */
	static double meter2Pixel (double meters) { return meters * SCALE; }
	
	/** method for changing X meter coordinates to pixel coordinates
	 * 
	 * @param Y the Y position of the ball
	 * @param bSize the radius of the ball in meters
	 * @return the Y coordinates of the ball in pixels
	 */
	
	static double pixelY(double Y, double bSize) {return (double) (HEIGHT-(Y+bSize)*SCALE);}
	
	/** method for changing X meter coordinates to pixel coordinates
	 * 
	 * @param X the X position of the ball
	 * @param bSize the radius of the ball in meters
	 * @return the X coordinates of the ball in pixels
	 */
	static double pixelX(double X, double bSize) {return (double) ((X-bSize)*SCALE);}
    
	/**
	* Constructor for specifying the parameters for simulation.
	*
	* @param Xi double The initial X position of the center of the ball
	* @param Yi double The initial Y position of the center of the ball
	* @param Vo double The initial velocity of the ball at launch
	* @param theta double Launch angle (with the horizontal plane)
	* @param bSize double The radius of the ball in simulation units
	* @param bColor Color The initial color of the ball
	* @param bLoss double Fraction [0,1] of the energy lost on each bounce
	*/

	public aBall (double Xi, double Yi, double V0, double theta,
			  double bSize, Color bColor, double bLoss)
	{
		this.Xi = Xi; 															// X coordinate of the initial launch position (meters)
		this.Yi = Yi;															// Y coordinate of the initial launch position (meters)
		this.V0 = V0;															// initial launch velocity (meters/second)
		this.theta = theta;														// initial launch angle (degrees, as measured from the ground plane)
		this.bSize = bSize;														// radius of the ball (meters)
		this.bColor = bColor;													// ball color
		this.bLoss = bLoss;														// energy loss coefficient
		
		myBall = new GOval((this.Xi)-meter2Pixel(bSize), pixelY(this.Yi, this.bSize), meter2Pixel(this.bSize)*2, meter2Pixel(this.bSize)*2);
		myBall.setFilled(true);
		myBall.setFillColor(this.bColor);
	}

	public GOval getBall() 
	{
		 return myBall;
	}

	/* run method */
	public void run()
	{
		Vt = g / (4*Pi*bSize*bSize*k); 												// Terminal velocity #
	    V0x=V0*Math.cos(theta*Pi/180); 												// X component of initial velocity #
	    V0y=V0*Math.sin(theta*Pi/180); 												// Y component of initial velocity #
	   
	    // Initializing KEx, KEy, KELast to skip the first while loop
	    KEx = 1.0;
	    KEy = 1.0;
	    KELast = 9.0;

	    while(((KEx+KEy)>ETHR) && ((KEx+KEy)<=KELast))
	    {
	    	
		    X = V0x*Vt/g*(1-Math.exp(-g*time/Vt)); 										// X position #
		    Y = bSize + Vt/g*(V0y+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; 				// Y position #
		    
		    Vx = (X-Xlast)/TICK; 														// Estimate Vx from difference #
		    Vy = (Y-Ylast)/TICK;														// Estimate Vy from difference #
	    
		    Xlast = X;																	// Initilazing Xlast & Ylast for calculating the Vx & Vy
		    Ylast = Y;
		    
		    if (Vy<0 && Y <= bSize)
		    {   					    	
		    	    KEx = 0.5*Vx*Vx*(1-bLoss); 								// Kinetic energy in X direction after collision #
			    	KEy = 0.5*Vy*Vy*(1-bLoss); 								// Kinetic energy in Y direction after collision #
			    	
			    	if(Vx<0) {V0x = -Math.sqrt(2*KEx);}						// Resulting horizontal velocity if Vx is less than zero to fix the direction of bounces				    
			    	else 	 {V0x = Math.sqrt(2*KEx);} 						// Resulting horizontal velocity #
			    			  V0y = Math.sqrt(2*KEy);						// Resulting vertical velocity #
			    		
			    	/* Reinitializing values */

			    	X0 += X;
			    	Y = bSize;
			    	time = 0;
			    	X = 0;
			    	
			    	/* Break statements */
			    	
			    	if (((KEx+KEy)<=ETHR)&&((KEx+KEy)>=KELast)) break; 		// Break statement to break the loop (the way prof. Ferrie suggested in his assignment document)
			    	
			    	KELast = KEy + KEx;                  					// Calculating KELast for breaking the loop
			    	
			    	if ((KEx <= ETHR) && (KEy <= ETHR)||((V0x <= ETHR) && (V0y <= ETHR))) break; // Extra break statment
		    }

		    ScrX = pixelX(X+X0, bSize);									// Calculating the screen coordinates using the pixelX method
		    ScrY = pixelY(Y, bSize);									// Calculating the screen coordinates using the pixelY method
		    ScrX = ScrX + WIDTH/2;
		    
		    myBall.setLocation(ScrX, ScrY); 							// Moving ball
		    
		    try { 														// pause for 50 milliseconds #
		    	Thread.sleep(50);
		    	} catch (InterruptedException e){
		    	e.printStackTrace();
		    	}
		    
		    time += TICK;           									// doing the time calculations
		}	    
	}
}