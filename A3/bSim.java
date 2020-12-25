/**
 * bSim class code of the third assignment for ECSE 202 with prof. Ferrie, Fall 2019.
 * The codes commented with # have been taken from the assignment document.
 * @author Namdar Kabolinejad
 * 
 */

import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.Color;
import acm.graphics.*;

public class bSim extends GraphicsProgram{
	
	public static void main(String[] args) {
		new bSim().start(args);
	}
	
	static double getHEIGHT()
	{
		return HEIGHT;
	}

	/* All named consents # */
	
	private static final int WIDTH = 1200; 					// n.b. screen coordinates
	private static final int HEIGHT = 600;					// distance from top of screen to ground plane
	private static final int OFFSET = 200;					// Offset size of screen
	private static final double SCALE = HEIGHT/100;         // pixels per meter
	private static final int NUMBALLS = 60; 				// # balls to simulate 
	private static final double MINSIZE = 1.0; 				// Minimum ball radius (meters)
	private static final double MAXSIZE = 7.0;	 			// Maximum ball radius (meters)
	private static final double EMIN = 0.2; 				// Minimum loss coefficient
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

	RandomGenerator rgen = RandomGenerator.getInstance();				// getting instance of the RandomGenerator class
	bTree myTree = new bTree();
	
	public void init()
	{
		addMouseListeners();
	}
	
	public void run() {
		
		rgen.setSeed((long) 424242);								// setting the rgen seed
		
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
            
			aBall iball = new aBall(WIDTH/(2*SCALE), iSize, iV, iTheta, iSize ,iColor ,iLoss);		// creating balls and passing them
			add(iball.getBall());
			myTree.addNode(iball);																	// making bTree
			iball.start();
		}
		
		while (myTree.isRunning()) {}; 				// while balls are bouncing do nothing
		
		// if all the balls stop running (is Running == false) --> add label + mouse event
		// GLabel
		GLabel label = new GLabel("Click to sort balls");
		label.setColor(Color.blue);
		label.setFont("Arial");
		add(label, getWidth()-150, getHeight()-160);
		
		waitForClick();							// mouse event
		myTree.stackBall();						// stack balls according to size
		}
	}