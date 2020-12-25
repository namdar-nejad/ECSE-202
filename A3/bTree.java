/**
 * bTree class code of the third assignment for ECSE 202 with prof. Ferrie, Fall 2019.
 * The codes commented with # have been taken from the assignment document.
 * @author Namdar Kabolinejad
 * 
 */

public class bTree{
	// Variables
	private static double DELTASIZE = 0.1;					// To make a new stack of balls
	boolean running = true;						// If false --> all balls have stopped bouncing 
	bNode root = null;										// root note for binary tree
	
	// Variables for stacking balls
	double x = 0;						// x location
	double y = 0;						// y location
	double prevSize = 0;				// bSize of previous ball
	
	// bNode class #
	class bNode {
		aBall iBall;
		bNode left;
		bNode right;
	}
	
	/**
	 * makeNode method #
	 * Creates a single instance of a bNode
	 * left and right nodes are null
	 * 
	 * @param	aBall iBall
	 * @return  bNode
	 */
		bNode makeNode(aBall iBall) {
			bNode node = new bNode();							// create new object
			node.iBall = iBall;									// initialize data field
			node.left = null;									// set both successors
			node.right = null;									// to null
			return node;										// return handle to new object
		}
	
	// addNode method #
	public void addNode(aBall iBall) {
		bNode current;
		if (root == null) {							// If the tree is empty
			root = makeNode(iBall);
		}
		// If tree not empty, descend to the leaf node according to the input data.
		else {
			current = root;
			while (true) {
				if (iBall.getSize() < current.iBall.getSize()) {
					// New bSize < bSize of node, branch left
					if (current.left == null) {				// leaf node
						current.left = makeNode(iBall);		// attach new node here
						break;
					}
					else {									// otherwise
						current = current.left;				// keep traversing
					}
				}
				else {
					// New bSize >= bSize of node, branch right
					if (current.right == null) {			// leaf node	
						current.right = makeNode(iBall);	// attach
						break;
					}
					else {									// otherwise 
						current = current.right;			// keep traversing
					}
				}
			}
		}
	}
	
	/* methods for checking if all balls have stopped bouncing */
	
/**
 * inorder wraper method - warper class for traverse-inorder #
 */
	public void inorder() {									// hides recursion from user
		traverse_inorder(root);
	}
	
/**
 * traverse_inorder method #
 * goes through nodes left-root-right
 * 
 * @param bNode root
 * @return boolean running
 */
	public boolean traverse_inorder(bNode root) {
		running = false;
		
		if (root.left != null){traverse_inorder(root.left);}	else {}	
		
		if (root.iBall.getEnergy() == true)						// getEnergy is a getter for hasEnergy boolean in aBall class
		{														// hasEnergy will be false if the ball's energy is less than the ETHR
			running = true;										// If ball has energy then running is true
		}	else {}
		
		if (root.right != null){traverse_inorder(root.right);}	else {}
		
		return (running);
	}
	
	// warper class for traverse_inorder
	public boolean isRunning()						// traverses through travarse_inorder method to see if all balls have stopped bouncing
	{						
		traverse_inorder(root);
		return (running);
	}

	/* methods for sorting and stacking balls */
	// warper class for  sortBall
	public void stackBall()
	{
		sortBall(root);								// traverses through sortBall method - below -->
	}
	
	/**
	 * sortBall method
	 * goes through nodes with left-root-right traversal
	 * sorts balls according to size
	 * 
	 * @param bNode root
	 * @return graphic display of balls
	 */
	private void sortBall(bNode root) {
		if (root.left != null) {
			sortBall(root.left);
		}
		if (root != null)
		{
			if (prevSize == 0)									// if it's the most left in the bTree - smallest ball
			{
				x =0;
				root.iBall.myBall.setLocation(x, bSim.getHEIGHT());
				prevSize = root.iBall.getSize();
			} else { }
				
			if ((root.iBall.getSize()-prevSize)>DELTASIZE) {	// if deference in bSize is more than DELTASIZE --> make new stack
				
				x += gUtil.meter2Pixel((prevSize)*2);
				y = gUtil.meter2Pixel((root.iBall.getSize())*2);
				root.iBall.myBall.setLocation(x, bSim.getHEIGHT()-y);
				prevSize = root.iBall.getSize();
				}
			
			else{												// if less that DELTASIZE --> put on the same stack
				y += gUtil.meter2Pixel(prevSize)*2;
				root.iBall.myBall.setLocation(x, bSim.getHEIGHT()-y);
				prevSize = root.iBall.getSize();
				}
		}
		
		if (root.right!= null) {
			sortBall(root.right);
			}
		}
	}
