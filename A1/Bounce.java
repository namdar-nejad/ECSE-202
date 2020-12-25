import acm.program.*;
import java.awt.Color;
import acm.graphics.*;

/** ECSE 202 Assignment 1, Bouncing Ball Simulator
 * @author Namdar Kabolinejad
 */

public class Bounce extends GraphicsProgram
{
	
	/* Screen display parameters (screen coordinates) */
	
	private static final int WIDTH = 600; 					// defines the width of the screen in pixels
	private static final int HEIGHT = 600; 					// distance from top of screen to ground plane
	private static final int OFFSET = 200; 					// distance from bottom of screen to ground plane
	
	/* Simulation parameters (meter coordinates) */
	
	private static final double g = 9.8; 					// MKS gravitational constant 9.8 m/s^2
	private static final double Pi = 3.141592654; 			// To convert degrees to radians
	private static final double TICK = 0.1; 				// Clock tick duration (sec)
	private static final double ETHR = 0.01; 				// If either Vx or Vy < ETHR STOP
	private static final double XMAX = 100.0; 				// Maximum value of X
	private static final double PD = 1; 					// Trace point diameter
	private static final double SCALE = HEIGHT/XMAX; 		// Pixels/meter
	private static final double k = 0.0016;					// Parameter for calculating Vt
	private static final double GROUND = 3;					// The thickness of the ground
	private static double Xinit = 10; 						// Initial ball location (X)
	
	/* Instance Variables */
	private static final boolean TEST = true; 				// print if test true
	private static double Time;								// for calculating the time
	
	/* Methods */
	
	/**  method for calculating initial 
	 * 
	 * @param bSize the radius of the ball in meters
	 * @return initial Y
	 */
	
	static double Yinit (double bSize) { return HEIGHT-(2*meter2Pixel(bSize));}		  	
	
	/** method to change meters to pixels using the SCALE
	 * 
	 * @param meters size in meters
	 * @return size in pixels
	 */
	static double meter2Pixel (double sMeters) { return sMeters * SCALE; }				

	/** method for changing X meter coordinates to pixel coordinates
	 * 
	 * @param X the X position of the ball
	 * @param bSize the radius of the ball in meters
	 * @return X coordinates of the ball in pixels
	 */
	static double pixelX(double X, double bSize) {return (int) ((X-bSize)*SCALE);}
	
	/** method for changing X meter coordinates to pixel coordinates
	 * 
	 * @param Y the Y position of the ball
	 * @param bSize the radius of the ball in meters
	 * @return Y coordinates of the ball in pixels
	 */
	
	static double pixelY(double Y, double bSize) {return (int) (HEIGHT-(Y+bSize)*SCALE);}
	
	public void run()
	{
		/* Setting up the display */
		
		this.resize(WIDTH, HEIGHT+OFFSET);											// resizing window
		
		GRect ground = new GRect(0 , HEIGHT, WIDTH, GROUND);						// adding ground line
		ground.setFilled(true);
		ground.setFillColor(Color.BLACK);
		add(ground);
		
		/* Assigning parameters */
		
	    double V0 = readDouble("Enter initial velocity in m/s [0,100]: ");         				//Initial Velocity in m/s
	    double theta = readDouble("Enter angle of launch in degrees [0,90]: ");    				//Angle of Launch in degrees
	    double loss = readDouble("Enter energy loss perameter [0,1]: ");           				//Energy Loss parameter
	    double bSize = readDouble("Enter radius of ball in m [0.1,5.0]: ");        				//Radius of ball in 
	    println("");
	    
	    /* Adding ball */
	    
		GOval ball = new GOval(Xinit, Yinit(bSize), 2*meter2Pixel(bSize), 2*meter2Pixel(bSize));			// meter2Pixel is a method that returns pixels
		ball.setFilled(true);																				
		ball.setFillColor(Color.DARK_GRAY);
		add(ball);
		
		pause(100);
		
	    /* Initializing variables */		
		
	    double Vt = g / (4*Pi*bSize*bSize*k); 												// Terminal velocity
	    double V0x=V0*Math.cos(theta*Pi/180); 												// X component of initial velocity
	    double V0y=V0*Math.sin(theta*Pi/180); 												// Y component of initial velocity
	    
		double Xlast = 0;
		double Ylast = 0;
		double time = 0;
		double X0 = 0;
		Time = 0;

		pause(1000);
		
		// Stating the bouncing loop
		
	    while(true)
	    {
		    double X = V0x*Vt/g*(1-Math.exp(-g*time/Vt)); 										// X position
		    double Y = bSize + Vt/g*(V0y+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; 					// Y position
		    
		    double Vx = (X-Xlast)/TICK; 														// Estimate Vx from difference
		    double Vy = (Y-Ylast)/TICK;															// Estimate Vy from difference
	    
		    Xlast = X;
		    Ylast = Y;
		    X = Xlast;
		    
		    if (Vy<0 && Y <= bSize)
		    {   					    	
		    	    double KEx = 0.5*Vx*Vx*(1-loss); 								// Kinetic energy in X direction after collision
			    	double KEy = 0.5*Vy*Vy*(1-loss); 								// Kinetic energy in Y direction after collision
			    	
			    	V0x = Math.sqrt(2*KEx); 										// Resulting horizontal velocity
			    	V0y = Math.sqrt(2*KEy);											// Resulting vertical velocity
			    	
			    	/* Reinitializing values */
			    	
			    	time = 0;
			    	X0 += X;
			    	Y = bSize;
			    	X = 0;
			    	
			    	if ((KEx <= ETHR) || (KEy <= ETHR)) break;
		    }
		    
		    double ScrX = pixelX(X+X0, bSize);													// Calculating the screen coordinates using the pixelX method
		    double ScrY = pixelY(Y, bSize);														// Calculating the screen coordinates using the pixelY method
		    ScrX = ScrX + Xinit;
		    
		    ball.setLocation(ScrX + meter2Pixel(bSize),ScrY); 									// Moving ball
		    
		    time += TICK;           // doing the time calculations
		    Time += TICK;
		    pause(100);
		    
		    /* Adding trace point */
		    
		    GOval trace = new GOval(ScrX+meter2Pixel(bSize)*2,ScrY+meter2Pixel(bSize),PD,PD);
		    trace.setFilled(true);
		    trace.setColor(Color.RED);
		    add(trace);
		    
		    /* Printing values */
		    
		    if (TEST)
		    	System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",
		    	Time,X0+X,Y,Vx,Vy);
		}
	}
}