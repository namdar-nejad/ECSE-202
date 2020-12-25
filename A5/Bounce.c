/*
 ============================================================================
 Name        : Bounce.c
 Author      : Namdar Kabolinejad
 Description : Bounce.c class for the 5th assignment of ECSE 202 with Prof. farrie, fall 2019.
 Note		 : some parts of the code have been taken from the solution provided by prof. farrie
 ============================================================================
 */
	// library imports
	#include <stdio.h>
	#include <stdlib.h>
	#include <math.h>

	// constant variables (used const instead of #describe because it's more useful and efficient)
	const int HEIGHT = 600;
	const int WIDTH = 600;
	const int OFFSET = 200;

	const double g = 9.8;
	const double k = 0.0016;
	const double Pi = 3.1416;
	const double Xinit = 5.0;
	const double TICK = 0.1;
	const double ETHR = 0.01;
	const double XMAX = 100.0;
	const double YMAX = 100.0;

	int main(void)
	{
		// declaring variables
		double V0, theta, loss, bSize;			// variables assigned by user
		double ScrX, ScrY, SCALE;				// screen variables
		double KEx, KEy, V0x, V0y, Vt, time;	// computational variables
		double X,X0,Xlast,Vx,Y,Vy,Ylast;		// computational variables

		// defining true and false
		int false = 0;
		int true = !false;

		// getting values from user
		printf("Enter the initial velocity of the ball in meters/second [0,100]: ");
		scanf("%lf",&V0);

		printf("Enter the launch angle in degrees [0,90]: ");
		scanf("%lf",&theta);

		printf("Enter energy loss parameter [0,1]: ");
		scanf("%lf",&loss);

		printf ("Enter the radius of the ball in meters [0.1,5.0]: ");
		scanf("%lf",&bSize);

		/* COMPUTATIONS */
		SCALE = HEIGHT/XMAX;
		ScrX = (Xinit-bSize)*SCALE;     // Convert simulation to screen coordinates
		ScrY = HEIGHT-(2*bSize)*SCALE;  // Convert simulation to screen coordinates

		KEy = ETHR;						// setting kinetic energy to ETHR
		KEx = ETHR;
		V0x = V0*cos(theta*Pi/180);		// velocity in x direction
		V0y = V0*sin(theta*Pi/180);		// velocity in y direction
		Vt = g / (4*Pi*bSize*bSize*k);  // termial velocity

		// assigning values
		time = 0;
		X0 = Xinit;
		Y = bSize;
		Ylast = Y;
		Xlast = X0;

		while(true){

			X = V0x*Vt/g*(1-exp(-g*time/Vt)); 										// X position
			Y = bSize + Vt/g*(V0y+Vt)*(1-exp(-g*time/Vt))-Vt*time; 					// Y position

			Vx = (X-Xlast)/TICK; 													// Estimate Vx from difference
			Vy = (Y-Ylast)/TICK;													// Estimate Vy from difference

			/* Printing values */

			if (true)
				printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",time,X0+X,Y,Vx,Vy);

			Xlast = X;
			Ylast = Y;

			if ((Vy<0) && (Y <= bSize))									// 	if ball hits the ground
			{
				if (true) printf("bounce\n");							// print "bounce"

				KEx = 0.5*Vx*Vx*(1-loss); 								// Kinetic energy in X direction after collision
				KEy = 0.5*Vy*Vy*(1-loss); 								// Kinetic energy in Y direction after collision

				V0x = sqrt(2*KEx); 										// Resulting horizontal velocity
				V0y = sqrt(2*KEy);										// Resulting vertical velocity

				/* Reinitializing values */

				time = 0;
				Y = bSize;
				X0 += X;
				X = 0;
				Xlast = X;
				Ylast = Y;
			}
			if ((KEy < ETHR) | (KEx < ETHR)) break;						// if energy is less than ETHR, break;
			time += TICK;
		}
		return 0;
}
