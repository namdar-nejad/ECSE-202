import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.Color;
import acm.graphics.*;

/**
 * bSim class code of the second assignment for ECSE 202 with prof. Ferrie, Fall 2019.
 * The codes commented with # have been taken from the assignment document.
 * @author Namdar Kabolinejad
 * 
 */

public class bSim extends GraphicsProgram{

	/* All named consents # */
	
	private static final int WIDTH = 1200; 					// n.b. screen coordinates
	private static final int HEIGHT = 600;					// distance from top of screen to ground plane
	private static final int OFFSET = 200;					// Offset size of screen
	private static final int NUMBALLS = 100; 				// # balls to simulate 
	private static final double MINSIZE = 1.0; 				// Minimum ball radius (meters)
	private static final double MAXSIZE = 10.0; 			// Maximum ball radius (meters)
	private static final double EMIN = 0.1; 				// Minimum loss coefficient
	private static final double EMAX = 0.6; 				// Maximum loss coefficient
	private static final double V0MIN = 40.0; 				// Minimum velocity (meters/sec)
	private static final double V0MAX = 50.0; 				// Maximum velocity (meters/sec)
	private static final double ThetaMIN = 80.0;			// Minimum launch angle (degrees)
	private static final double ThetaMAX = 100.0; 			// Maximum launch angle (degrees)
	
	/* Declaring instance variables */
	private double iSize;
	private Color iColor;
	private double iLoss;
	private double iV;
	private double iTheta;
	
	private RandomGenerator rgen = RandomGenerator.getInstance();	// getting instance of the RandomGenerator class
	
	public void run() {
		rgen.setSeed((long) 0);										// setting the rgen seed
		
		/* Setting up the display */
		
		this.resize(WIDTH, HEIGHT+OFFSET);							// resizing window
		add(new GLine(0,HEIGHT,WIDTH,HEIGHT));						// adding ground
		
		/* Loop for generating the balls */

		for(int i = 0; i < NUMBALLS; i++) {
            iSize = rgen.nextDouble(MINSIZE,MAXSIZE);
            iColor = rgen.nextColor();
            iLoss = rgen.nextDouble(EMIN,EMAX);
            iV = rgen.nextDouble(V0MIN,V0MAX);  
            iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);
            
			aBall iball = new aBall(WIDTH/2, iSize, iV, iTheta, iSize ,iColor ,iLoss);		// creating balls and passing them
			add(iball.getBall());
			iball.start();
		}
	}
}