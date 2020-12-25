/*
 ============================================================================
 Name        : dbReader.c
 Author      : Namdar Kabolinjad
 Description : A simple program to manage a small database of student
             : records using B-Trees for storage. This code has been built on prof. F. Ferrie's code.
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAXLEN 20
#define false 0
#define true !false

// Structure Templates

typedef struct SR {				// The student record object
    char Last[MAXLEN];
	char First[MAXLEN];
	int ID;
	int marks;
} SRecord;

typedef struct bN {				// The bNode object (not used
	struct SR *Srec;			// in this demo, but you will
	struct bN *left;			// need it for A6).
	struct bN *right;
} bNode;

// Function Prototypes

bNode *addNode_Name(bNode *root, SRecord *Record);
bNode *addNode_ID(bNode *root, SRecord *Record);
bNode *addNode_Mark(bNode *root, SRecord *Record);	// not part of the assigment
bNode *makeNode(SRecord *data);

void inorder(bNode *root);
void search_Name(bNode *root, char *data);
void search_ID(bNode *root, int ID);
void search_Mark(bNode *root, int Mark); 			// not part of the assigment
void str2upper(char *string);
void help();

//
// Since we haven't done pointer-pointers in this course, we'll use a
// global variable to return the matching student record.  This is
// equivalent to what we did in Java using an instance variable.

bNode *match;

// Main entry point is here.  Program uses the standard Command Line Interface

int main(int argc, char *argv[]) {

// Internal declarations

    FILE * NAMESIDS;        // File descriptor (an object)!
	FILE * MARKS;           // Will have two files open

    bNode *root_N;   		// Pointer to names B-Tree
    bNode *root_I;   		// Pointer to IDs B-Tree
    bNode *root_M;
    SRecord *Record;   		// Pointer to current record read in

	int NumRecords;
	char cmd[MAXLEN], sName[MAXLEN];
	int sID;
	int sMark;

// Argument check
        if (argc != 3) {
                printf("Usage: sdb [Names+IDs] [marks] \n");
                return -1;
        }

// Attempt to open the user-specified file.  If no file with
// the supplied name is found, exit the program with an error
// message.

        if ((NAMESIDS=fopen(argv[1],"r"))==NULL) {
                printf("Can't read from file %s\n",argv[1]);
                return -2;
        }

        if ((MARKS=fopen(argv[2],"r"))==NULL) {
                printf("Can't read from file %s\n",argv[2]);
                fclose(NAMESIDS);
                return -2;
        }

// Initialize B-Trees by creating the root pointers;

    root_N=NULL;
	root_I=NULL;
	root_M=NULL;

//  Read through the NamesIDs and marks files, record by record.

	NumRecords=0;

	printf("Building database...\n");

	while(true) {

// 	Allocate an object to hold the current data

		Record = (SRecord *)malloc(sizeof(SRecord));
		if (Record == NULL) {
			printf("Failed to allocate object for data in main\n");
			return -1;
		}

//  Read in the data.  If the files are not the same length, the shortest one
//  terminates reading.

		int status = fscanf(NAMESIDS,"%s%s%d",Record->First,Record->Last,&Record->ID);
		if (status == EOF) break;
		status = fscanf(MARKS,"%d",&Record->marks);
		if (status == EOF) break;
		NumRecords++;

//	Add the record just read in to 2 B-Trees - one ordered
//  by name and the other ordered by student ID.

	    root_N=addNode_Name(root_N,Record);
	    root_I=addNode_ID(root_I,Record);
	    root_M=addNode_Mark(root_M,Record);

//	For this demo we'll simply list each record as we receive it

		printf("\nStudent Name:\t\t%s %s\n",Record->First,Record->Last);
		printf("Student ID:\t\t%d\n",Record->ID);
		printf("Total Grade:\t\t%d\n",Record->marks);

	}

// Close files once we're done

	fclose(NAMESIDS);
	fclose(MARKS);

	printf("Finished, %d records found...\n",NumRecords);


//
//  Simple Command Interpreter: - This is commented out until you implement the functions listed above
//

	while (1) {
	    printf("sdb> ");
	    scanf("%s",cmd);					  // read command
	    str2upper(cmd);

	    if (0) {							  // This is a temporary stub
	    	continue;
	    }

//#ifdef notdef                   // This directive hides the subsequent text from
                                // the compiler until it sees #endif
// List by Name

		if (strncmp(cmd,"LN",2)==0) {         // List all records sorted by name
			printf("Student Record Database sorted by Last Name\n\n");
			inorder(root_N);
			printf("\n");
		}

// List by ID

		else if (strncmp(cmd,"LI",2)==0) {    // List all records sorted by ID
			printf("Student Record Database sorted by Student ID\n\n");
			inorder(root_I);
			printf("\n");
		}

// List by Marks
		else if (strncmp(cmd,"LM",2)==0) {         // List all records sorted by mark
			printf("Student Record Database sorted by Last by Mark\n\n");
			inorder(root_M);
			printf("\n");
		}

// Find record that matches Name

		else if (strncmp(cmd,"FN",2)==0) {    // List record that matches name
			printf("Enter name to search: ");
			scanf("%s",sName);
			match=NULL;
			search_Name(root_N,sName);
			if (match==NULL)
			  printf("There is no student with that name.\n");
	        else {
			  if (strlen(match->Srec->First)+strlen(match->Srec->Last)>15) {
				printf("\nStudent Name:\t%s %s\n",match->Srec->First,match->Srec->Last);
			  } else {
				printf("\nStudent Name:\t\t%s %s\n",match->Srec->First,match->Srec->Last);
			  }
			  printf("Student ID:\t\t%d\n",match->Srec->ID);
			  printf("Total Grade:\t\t%d\n\n",match->Srec->marks);
	        }
		}

// Find record that matches ID


		else if (strncmp(cmd,"FI",2)==0) {    // List record that matches ID
			printf("Enter ID to search: ");
			scanf("%d",&sID);
			match=NULL;
			search_ID(root_I,sID);
			if (match==NULL)
			  printf("There is no student with that ID.\n");
	        else {
			  if (strlen(match->Srec->First)+strlen(match->Srec->Last)>15) {
				printf("\nStudent Name:\t%s %s\n",match->Srec->First,match->Srec->Last);
			  } else {
				printf("\nStudent Name:\t\t%s %s\n",match->Srec->First,match->Srec->Last);
			  }
			printf("Student ID:\t\t%d\n",match->Srec->ID);
			printf("Total Grade:\t\t%d\n\n",match->Srec->marks);
	      }


// Find record that matches Marks

		} else if (strncmp(cmd,"FM",2)==0) {    // List record that matches ID
			printf("Enter Mark to search: ");
			scanf("%d",&sMark);
			match=NULL;
			search_Mark(root_M,sMark);
			if (match==NULL)
			  printf("There is no student with that Mark.\n");
	        else {
			  if (strlen(match->Srec->First)+strlen(match->Srec->Last)>15) {
				printf("\nStudent Name:\t%s %s\n",match->Srec->First,match->Srec->Last);
			  } else {
				printf("\nStudent Name:\t\t%s %s\n",match->Srec->First,match->Srec->Last);
			  }
			printf("Student ID:\t\t%d\n",match->Srec->ID);
			printf("Total Grade:\t\t%d\n\n",match->Srec->marks);
	      }

//#endif

// Help

		} else if (strncmp(cmd,"H",1)==0) {  // Help
			help();
		}

		else if (strncmp(cmd,"?",2)==0) {     // Help
			help();
		}

// Quit

		else if (strncmp(cmd,"Q",1)==0) {  // Help
			printf("Program terminated...\n");
			return 0;
		}

// Command not understood

		else {
			printf("Command not understood.\n");
		}
	}

}

//
//	Write and insert the functions listed in the prototypes here.
//


//
//  Convert a string to upper case
//

void str2upper (char *string) {
    int i;
    for(i=0;i<strlen(string);i++)
       string[i]=toupper(string[i]);
     return;
}

// Help
// prints command list

void help() {
	printf("LN List all the records in the database ordered by last name.\n");
	printf("LI List all the records in the database ordered by student ID.\n");
	printf("FN Prompts for a name and lists the record of the student with the corresponding name.\n");
	printf("FI Prompts for a name and lists the record of the student with the Corresponding ID.\n");
	printf("HELP Prints this list.\n");
	printf("? Prints this list.\n");
	printf("Q Exits the program.\n\n");

	return;
}



// functions I wrote:

// make node function
bNode *makeNode(SRecord *data){
bNode *node = (bNode*)malloc(sizeof(bNode));
node->Srec = data;				// payload of node
node->left = NULL;				// left and right nodes are set to NULL
node->right = NULL;
return node;
}


// addNode_ID function
bNode *addNode_ID(bNode *root, SRecord *data){
	bNode *current;
if (root == NULL){
	root = makeNode(data);
}
else{
	current = root;
	while(true){
		if(data->ID < current->Srec->ID){			// if given value is less than the value of the current node
			if(current->left == NULL){				
				current->left = makeNode(data);		// if reach a leaf node then make node
				break;
			}
			else{									// if not reached leaf node-go left
				current = current->left;
			}
		}
		else{										// if given value is more than the value of current node
			if(current->right == NULL){				
				current->right = makeNode(data);	// if reached a leaf node
				break;
			}
			else{
				current = current->right;			// if not reached a leaf node-go right
			}
		}
	}
}
return root;										// returns root
}													// **all addNode functions follow the same pattern**

// addNode_Name function
bNode *addNode_Name(bNode *root, SRecord *data){
	bNode *current;
if (root == NULL){
	root = makeNode(data);
}
else{
	current = root;
	while(true){
		if(strcmp(data->Last, current->Srec->Last) < 0){
			if(current->left == NULL){
				current->left = makeNode(data);
				break;
			}
			else{
				current = current->left;
			}
		}
		else{
			if(current->right == NULL){
				current->right = makeNode(data);
				break;
			}
			else{
				current = current->right;
			}
		}
	}
}
return root;
}


// addNode_Mark function **not part of the assigment-just for fun**
bNode *addNode_Mark(bNode *root, SRecord *data){
	bNode *current;
if (root == NULL){
	root = makeNode(data);
}
else{
	current = root;
	while(true){
		if(data->marks < current->Srec->marks){
			if(current->left == NULL){
				current->left = makeNode(data);
				break;
			}
			else{
				current = current->left;
			}
		}
		else{
			if(current->right == NULL){
				current->right = makeNode(data);
				break;
			}
			else{
				current = current->right;
			}
		}
	}
}
return root;
}

// inorder traversing through the tree
void inorder(bNode *root) {
    if (root->left != NULL) inorder(root->left);
    printf("%-10s %-10s %-5d %d \n", root->Srec->First, root->Srec->Last, root->Srec->ID, root->Srec->marks);
    if (root->right != NULL) inorder(root->right);
}

// search_ID function 
void search_ID(bNode *root, int ID){
	bNode *current;
if (root->Srec->ID == ID){						// if the value we are looking for is the root node
	match = root;
}
else{
	current = root;
	while(true){
		if(current->Srec->ID > ID){				// if the value we are looking is less than current node
			current = current->left;			// go left
		}
		else if(current->Srec->ID < ID){		// if value we are looking for is more than current node
			current = current->right;			// go right
		}
		else if(current->Srec->ID == ID){		// if we reached the desired node
			match = current;
			break;
			}
		if(current == NULL) {					// if the value we are looking for doesn't exist
			match = NULL;						// **all search functions follow the same pattern**
			break;
		}
		}
	}
}

// search_Name function
void search_Name(bNode *root, char *data){
	bNode *current;
if (strcasecmp(root->Srec->Last, data) == 0){
	match = root;
}
else{
		current = root;
		while(true){
			if(strcasecmp(data, current->Srec->Last) < 0){
					current = current->left;
			}
			else if (strcasecmp(data, current->Srec->Last) > 0){
					current = current->right;
			}
			else if (strcasecmp(current->Srec->Last, data) == 0){
				match = current;
				break;
			}
			if (current == NULL){
				match = NULL;
				break;
			}	
		}
	}
}

// search_Mark function **not part of the assigment-just for fun**
void search_Mark(bNode *root, int Mark){
	bNode *current;
if (root->Srec->marks == Mark){
	match = root;
}
else{
	current = root;
	while(true){
		if(current->Srec->marks > Mark){
			current = current->left;
		}
		else if(current->Srec->marks < Mark){
			current = current->right;
		}
		else if(current->Srec->marks == Mark){
			match = current;
			break;
			}
		if(current == NULL) {
			match = NULL;
			break;
		}
		}
	}
}