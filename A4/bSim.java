/**
 * bSim class code of the 4th assignment for ECSE 202 with prof. Ferrie, Fall 2019.
 * The codes commented with # have been taken from the assignment document.
 * @author Namdar Kabolinejad
 * 
 */

import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.font.TextLayout;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import acm.graphics.*;
import acm.gui.DoubleField;
import acm.gui.IntField;
import acm.gui.TableLayout;

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
	private static final int panelWidth = 120;				
	private static final int HEIGHT = 650;					// distance from top of screen to ground plane
	private static final int OFFSET = 200;					// Offset size of screen
	private static final double SCALE = HEIGHT/100;         // pixels per meter

	
	/* Declaring instance variables */
	private double iSize;
	private Color iColor;
	private double iLoss;
	private double iV;
	private double iTheta;
	
	private IntField NumBalls;
	private DoubleField MinSize;
	private DoubleField MaxSize;
	private DoubleField VelMin;
	private DoubleField VelMax;
	private DoubleField LossMin;
	private DoubleField LossMax;
	private DoubleField ThetaMin;
	private DoubleField ThetaMax;
	
	private JButton run;
	private JButton stack;
	private JButton clear;
	private JButton stop;
	private JButton quit;
	
	private JCheckBox trace;
	private bSim traceLink = null;
	
	private boolean simEnable = false;
	
	RandomGenerator rgen = RandomGenerator.getInstance();				// getting instance of the RandomGenerator class
	bTree myTree = new bTree();
	
	
	public void run()
	{	
		rgen.setSeed((long) 424242);
		simEnable = false;
		
		// resizing and adding ground line
		this.resize(WIDTH+panelWidth, HEIGHT + OFFSET);
		add(new GLine(0,HEIGHT,WIDTH,HEIGHT));
		
		// adding the intFeild and labels to left JPanel
		JPanel MainPanel = new JPanel();
		MainPanel.setLayout(new TableLayout(11,2));
		
		MainPanel.add(new JLabel("General Parameters:"), "width = 120");
		MainPanel.add(new JLabel(" "));
		MainPanel.add(new JLabel(" "), "RowSpan = 2");
		
		MainPanel.add(new JLabel("NUMBALLS:"), "width = 90");
		NumBalls = new IntField(60);
		MainPanel.add(NumBalls, "width = 40");
		
		MainPanel.add(new JLabel("MIN SIZE:"), "width = 90");
		MinSize = new DoubleField(1.0);
		MainPanel.add(MinSize, "width = 40");
		
		MainPanel.add(new JLabel("MAX SIZE:"), "width = 40");
		MaxSize = new DoubleField(7.0);
		MainPanel.add(MaxSize, "width = 40");
		
		MainPanel.add(new JLabel("LOSS MIN"), "width = 40");
		LossMin = new DoubleField(0.2);
		MainPanel.add(LossMin, "width = 40");
		
		MainPanel.add(new JLabel("LOSS MAX:"), "width = 90");
		LossMax = new DoubleField(0.6);
		MainPanel.add(LossMax, "width = 40");
		
		MainPanel.add(new JLabel("MIN VEL:"), "width = 90");
		VelMin = new DoubleField(50.0);
		MainPanel.add(VelMin, "width = 40");
		
		MainPanel.add(new JLabel("MAX VEL:"), "width = 90");
		VelMax = new DoubleField(80.0);
		MainPanel.add(VelMax, "width = 40");
		
		MainPanel.add(new JLabel("TH MIN:"), "width = 90");
		ThetaMin = new DoubleField(80.0);
		MainPanel.add(ThetaMin, "width = 40");
		
		MainPanel.add(new JLabel("TH MAX:"), "width = 90");
		ThetaMax = new DoubleField(100.0);
		MainPanel.add(ThetaMax, "width = 40");
		
		add(MainPanel, EAST);
		
		// Trace Check Box
		trace = new JCheckBox("Trace");
		trace.addActionListener(this);
		add(trace, SOUTH);
		
		
		// Combo Box
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new TableLayout(1,5));
		
		run = new JButton("Run");
		northPanel.add(run);
		
		stop = new JButton("Stop");
		northPanel.add(stop);
		
		stack = new JButton("Stack");
		northPanel.add(stack);
		
		clear = new JButton("Clear");
		northPanel.add(clear);
		
		quit = new JButton("Quit");
		northPanel.add(quit);
		
		add(northPanel, NORTH);
		
		addActionListeners();
		
		while(true) {
			pause(200);
			if (simEnable) { // Run once, then stop
				doSim();
				simEnable=false;
				}
		}
	}

	/**
	 * doSim method
	 * 
	 * generates balls
	 */
	
	private void doSim() {
		for(int i = 0; i < (int) NumBalls.getValue(); i++) {
		    iSize = rgen.nextDouble(MinSize.getValue(),MaxSize.getValue());
		    iColor = rgen.nextColor();
		    iLoss = rgen.nextDouble(LossMin.getValue(),LossMax.getValue());
		    iV = rgen.nextDouble(VelMin.getValue(),VelMax.getValue());  
		    iTheta = rgen.nextDouble(ThetaMin.getValue(),ThetaMax.getValue());
		    
			aBall iball = new aBall(WIDTH/(2*SCALE), iSize, iV, iTheta, iSize ,iColor ,iLoss, traceLink);		// creating balls and passing them
			add(iball.getBall());
			myTree.addNode(iball);																	// making bTree
			iball.start();
			}
	}
	
	/**
	 * doStack method
	 * 
	 * Traverses the bTree and then stacks the balls
	 */
	private void doStack()
	{
		myTree.inorder();
		myTree.stackBall();
	}
	
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand(); 
			if(cmd.equals("Run"))									// run sim if selected
			{
				aBall.TICK = 0.01;									// TICK back to 0.01
				System.out.println("Starting Simulation");
				simEnable = true;
			}
			
			if(cmd.equals("Stop"))									// to stop set TICK to 0
			{
				System.out.println("Stopped Sim");
				aBall.TICK = 0;
			}
			
			if(cmd.equals("Clear"))									// using the clearBall method from bTree class to clear all balls
			{
				System.out.println("Cleared Balls");
				myTree.clearBalls(this);
				removeAll();										// to remove the old trace points
				myTree = new bTree();								// new bTree so we can stack them later
				add(new GLine(0,HEIGHT,WIDTH,HEIGHT));				// adding ground line
			}
			
			if(cmd.equals("Stack"))									// using doStack method to stack all balls
			{
				System.out.println("All Stacked");
				doStack();
			}
			
			if(cmd.equals("Quit"))									// exiting program
			{
				System.exit(0);
			}
			
			if(trace.isSelected())									// if selected link is not null do it will put trace points
			{
				System.out.println("Trace selected");
				traceLink = this;
			}
			else {													// if it is not slected link = null, no trace points
				System.out.println("Stoped tracing"); 
				traceLink = null;
			}
		}
	}