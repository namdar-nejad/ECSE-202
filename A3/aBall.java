/**
 * aBall class code of the third assignment for ECSE 202 with prof. Ferrie, Fall 2019.
 * The codes commented with # have been taken from the assignment documents posted on MyCourses.
 * @author Namdar Kabolinejad
 */

import java.awt.Color;
import acm.graphics.GOval;

public class aBall extends Thread{	
	
	
	/* Bouncing time speeded up for time efficiency - you may change time intervals on line 142 */
	
	
	/* Simulation parameters (meter coordinates) */
	
	private static final double g = 9.8; 					// MKS gravitational constant 9.8 m/s^2 #
	private static final double Pi = 3.141592654; 			// To convert degrees to radians #
	private static final double TICK = 0.01; 				// Clock tick duration (sec) #
	private static final double TICKmS = TICK*1000;			// Clock tick duration (milliseconds) #
	private static final double ETHR = 0.1; 				// If either Vx or Vy < ETHR STOP #
	private static final double k = 0.0001  ;				// Parameter for calculating Vt #
	private static final int HEIGHT = 600; 					// distance from top of screen to ground plane #
	private static final int WIDTH = 1200; 					// n.b. screen coordinates #
	private static final double SCALE = HEIGHT/100;  		// pixels per meter #
	
	/* Declaring all the instance variables */
	
	// Related to the ball
	private boolean hasEnergy;   //<--- has energy boolean
	private double Xi;
	private double Yi;
	private double V0;
	private double theta;
	private double bLoss;
	private Color bColor;
	GOval myBall;
	private double bSize;
	
	/* getters for myBall */
	boolean getEnergy() {			// getter for hasEnergy
		return hasEnergy;
	}
	double getSize()				// getter for bSize
	{
		return bSize;
	}
	public GOval getBall() 			// getter for myBall
	{
		 return myBall;
	}
	
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
		
		myBall = new GOval(gUtil.pixelX(this.Xi-this.bSize), gUtil.pixelY(this.Yi+this.bSize), gUtil.meter2Pixel(this.bSize*2), gUtil.meter2Pixel(this.bSize)*2);
		myBall.setFilled(true);
		myBall.setFillColor(this.bColor);
	}
	
	/* run method */
	public void run()
	{
		double Vt = g / (4*Pi*bSize*bSize*k); 												// Terminal velocity #
	    double time = 0;
		double KEx = ETHR, KEy = ETHR;
	    
		double X, X0, Xlast, Vx, Y, Vy, Ylast, KELast;										// Declaring variables #
		double signV0x = 1;																	// #
	    
		double V0x=V0*Math.cos(theta*Pi/180); 												// X component of initial velocity #
	    double V0y=V0*Math.sin(theta*Pi/180); 												// Y component of initial velocity #

	    if (V0x < 0) signV0x = -1;
	    
	    X0 = Xi;
	    Y = Yi;
	    Ylast = Y;
	    Xlast = X0;
	    KELast = 0.5*V0*V0;
	    hasEnergy = true;
	    
	    while(hasEnergy)
	    {
	    	
		    X = V0x*Vt/g*(1-Math.exp(-g*time/Vt)); 										// X position #
		    Y = bSize + Vt/g*(V0y+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; 				// Y position #
		    
		    Vx = (X-Xlast)/TICK; 														// Estimate Vx from difference #
		    Vy = (Y-Ylast)/TICK;														// Estimate Vy from difference #
	    
		    Xlast = X;																	// Initilazing Xlast & Ylast for calculating the Vx & Vy
		    Ylast = Y;
		    
		    if ((Vy<0)&&(Y<=bSize))
		    {   					    	
		    	    KEx = 0.5*Vx*Vx*(1-bLoss); 											// Kinetic energy in X direction after collision #
			    	KEy = 0.5*Vy*Vy*(1-bLoss); 											// Kinetic energy in Y direction after collision #
			    	
			    	V0x = Math.sqrt(2*KEx)*signV0x;
			    	V0y = Math.sqrt(2*KEy);
			    				    	/* Break statements */
			    	
			    	if ((KEx+KEy)<ETHR || (KEx+KEy)>=KELast) hasEnergy = false; 			// Break statement to break the loop (the way prof. Ferrie suggested in his assignment document)
			    	
			    	else KELast = KEx + KEy;                  								// Calculating KELast for breaking the loop

			    	time = 0;
			    	Y = bSize;
			    	X0 += X;
			    	X = 0;
			    	Xlast = X;
			    	Ylast = Y;
		    }

		    double ScrX = gUtil.pixelX(X+X0-bSize);									// Calculating the screen coordinates using the pixelX method
		    double ScrY = gUtil.pixelY(Y+bSize);									// Calculating the screen coordinates using the pixelY method
		    
		    myBall.setLocation(ScrX, ScrY); 										// Moving ball
		    
		    try { 																	// pause for 50 milliseconds #
		    	Thread.sleep((long) (TICKmS/50));									// for actual time change 50 to 2
		    	} catch (InterruptedException e){
		    	e.printStackTrace();
		    	}
		    
		    time += TICK;           												// doing the time calculations
		}	    
	}
}