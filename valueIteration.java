import java.text.DecimalFormat;
import java.util.Scanner;

public class valueIteration 
{
    public static int x;
    public static int y;
    
    public static int goal_x;
    public static int goal_y;
    public static double goal_val;

    public static int pit_x;
    public static int pit_y;
    public static double pit_val;

    public static String [][] policy_val;
    public static double [][] grid_matrix;
    public static int num_iterations =0;


    public static void setGrid()
    {
        Scanner sc = new Scanner(System.in);

        //The size of the grid_matrix to be used is specified
        System.out.println("Give the size of grid: Default-(4,3)");
        System.out.println("x: ");
        x=sc.nextInt();
        
        System.out.println("y: ");
        y=sc.nextInt();
        
        /*x = 4;
          y = 3;*/
        
        grid_matrix = new double[x][y];
        policy_val = new String[x][y];

        System.out.println("Give the position of goal (+1 value): Default-(4,3) ");
        System.out.println("x: ");
        goal_x=sc.nextInt();
        
        System.out.println("y: ");
        goal_y=sc.nextInt();
        
        goal_val = 1;
       
        /*goal_x = 4;
        goal_y = 3;
        goal_val = 1;*/
        
        System.out.println("Give the position of pit (-1 value): Default-(4,2)");
        System.out.println("x: ");
        pit_x=sc.nextInt();
        
        System.out.println("y: ");
        pit_y=sc.nextInt();
        
        pit_val = -1;

        sc.close();
        
        /*pit_x = 4;
        pit_y = 2;
        pit_val = -1;*/

        //grid_matrix Initialization
        for (int i = 0; i < x ; i++)
        {
            for (int j = 0; j < y ; j++)
            {
            	//Search for the goal & pit location  
                if (i == goal_x-1 && j == goal_y-1)
                {
                	//Setting +1 for goal
                    grid_matrix[i][j] = goal_val;
                }

                else if (i == pit_x-1 && j == pit_y-1)
                {
                	//Setting -1 for pit
                    grid_matrix[i][j] = pit_val;
                }
                else
                {
                	//Initialize other nodes to 0
                    grid_matrix[i][j] =0;
                }
            }
        }
    }

    public static int utilityValueCalc() 
    {
    	//Epsilon & Gamma vales can be altered
        double epsilon = 0.001; 
        double delta;
        //Expected: 0 < gamma < 1
        double gamma = 0.9;
        boolean convergence = false;

        while (!convergence) 
        {
            delta = 0;
            for (int i=0; i < x; i++) 
            {
                for (int j=0; j < y; j++) 
                {
                    if (i == goal_x - 1 && j == goal_y - 1)
                    {
                        continue;
                    }
                    else if (i == pit_x - 1 && j == pit_y - 1) 
                    {
                        continue;
                    }
                    else
                    {
                        double currValue = grid_matrix[i][j];
                        //Calculate utility for (i,j)
                        double max_comp=computeMax ( i, j );
                        //Calculate utility according to formula
                        grid_matrix[i][j]= -0.04 + gamma * max_comp;
                        delta = Math.max ( delta , Math.abs(grid_matrix[i][j] - currValue));
                        //Check for convergence according to the formula
                        if (delta < epsilon * (1 - gamma) / gamma)
                        {
                            convergence = true;
                            //Stop loop after this
                        }
                        else
                        {
                            convergence = false;
                        }
                    }
                }
            }
            //Making a note of the number of iterations in the while loop
            num_iterations = num_iterations + 1;
        }
        return num_iterations;
    }

   
    public static void displayUtilMatrix()
    {
        System.out.println ( "\nUtilities of each state in the grid_matrix after " + num_iterations + " iterations are:\n");
        for (int j=y-1; j >= 0 ; j--) 
        {
        	System.out.print("|");
        	for (int i=0; i < x; i++) 
            {
            	DecimalFormat df = new DecimalFormat("#.###");
            	double p = Double.parseDouble(df.format(grid_matrix[i][j]));
            	
            	System.out.print(p+"\t|");
            }
            System.out.println();
        }
    }   

   

    public static double computeMax(int i, int j)
    {
    	//Checking for wall bumps
        int p = i + 1;
        if (p > x-1)
            p = i;
        
        int q = i - 1;
        if (q < 0)
            q = i;
        
        int r = j + 1;
        if (r > y-1)
            r = j;
        
        int s = j - 1;
        if (s < 0)
            s = j;


        //Going up 
        double up = 0.8 * grid_matrix[i][r] + 0.1 * grid_matrix[q][j] + 0.1 * grid_matrix[p][j];
        //Going down
        double down = 0.8 * grid_matrix[i][s] + 0.1 * grid_matrix[q][j] + 0.1 * grid_matrix[p][j];
        //Going right
        double right = 0.8 * grid_matrix[p][j] + 0.1 * grid_matrix[i][r] + 0.1 * grid_matrix[i][s];
        //Going left
        double left = 0.8 * grid_matrix[q][j] + 0.1 * grid_matrix[i][r] + 0.1 * grid_matrix[i][s];

        //Checking for maximum value for every possible move
        double direction = Math.max(Math.max(Math.max(up, down), right),left);
        
        if (direction == up)
            policy_val[i][j] = "U";
        else if (direction == down)
            policy_val[i][j] = "D";
        else if (direction == right)
            policy_val[i][j] = "R";
        else
            policy_val[i][j] = "L";
        
        //Return the final direction 
        return direction; 
    }
    public static void displayDirectionMatrix()
    {
        System.out.println ( "\nOptimal policy_val of each state: " );
        for (int j=y-1; j >= 0 ; j--) 
        {
        	System.out.print("|");
        	for (int i=0; i < x; i++) 
            {
                System.out.print(policy_val[i][j]+"\t|");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        setGrid ();
        int itr=utilityValueCalc();
        System.out.println("The utility loop ran for: "+itr+" times till converging.");
        displayUtilMatrix();
        displayDirectionMatrix();
    }
}
