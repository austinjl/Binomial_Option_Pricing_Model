/*
 * Math 248 - Section 2 Project
 * 12/3/2018
 * By: Kirsten Ziviello and Austin Lam
 */

public class BinomialOptionPricing
{

  public static void main(String[] args)
  {

    // inputs
    double strikePrice = 39.00;
    double currPrice = 45.48;
    double volatility = 0.419;
    double interestRate = 0.0223;
    double years = 2.0/12.0;
    int numSteps = 2;
    
    // calculate period time and number of nodes
    double periodTime = years / numSteps; // delta T
    int totalNodes = calcTotalNodes(numSteps);
    
    // initialize s and c arrays
    double[] sArray = new double[totalNodes];
    double[] cArray = new double[totalNodes];
    
    // calculate U, D, and P
    double U = calcU(volatility, periodTime);
    double D = calcD(volatility, periodTime);
    double P = calcP(interestRate, periodTime, D, U);
    
    sArray[0] = currPrice; // set the root node
    int arrayIndex = 1; // track node index
    int matchingCount = 0; // track the matching S value
    
    // loop through each step
    for(int i = 1; i <= numSteps; i++)
    {
      // set the top new S value
      sArray[arrayIndex] = currPrice * Math.pow(U, i);
      arrayIndex++;
      
      // carry the matching S value
      for (int j = 1; j < i; j++)
      {
       sArray[arrayIndex] = sArray[matchingCount];
       matchingCount++;
       arrayIndex++;
      }
      
      // set the bottom new S value
      sArray[arrayIndex] = currPrice * Math.pow(D, i);
      arrayIndex++;
    }
    
    // set the C for the last step of nodes
    for (int l = sArray.length - 1; l > sArray.length - numSteps - 2; l--)
    {
      cArray[l] = Math.max(sArray[l] - strikePrice, 0);
    }
    
    arrayIndex = sArray.length - 1; // track node index
    
    // step back through the graph for each step
    for(int j = numSteps; j > 0; j--)
    {
      // set the nodes of the current step
      for (int k = 0; k < j; k++)
        {
          cArray[arrayIndex - 1 - j] = 
              calcC(interestRate, periodTime, P, cArray[arrayIndex - 1], cArray[arrayIndex]);
          arrayIndex--;
        }
      arrayIndex--;
    }
    
    // print outputs
    System.out.println("S-Nodes:");
    for (int k = 0; k < sArray.length; k++)
    {
      System.out.println(sArray[k]);
    }
    
    System.out.println("\nC-Nodes");
    for (int k = 0; k < sArray.length; k++)
    {
      System.out.println(cArray[k]);
    }
  }
  
  // calculate the total number of nodes
  public static int calcTotalNodes(int steps)
  {
    int totalNodes = 0;
    for (int i = 1; i < steps + 2; i++)
    {
      totalNodes += i;
    }
    return totalNodes;
  }
  
  // calculate U
  public static double calcU(double volatility, double periodTime)
  {
    return Math.exp(volatility * Math.sqrt(periodTime));
  }
  
  // calculate D
  public static double calcD(double volatility, double periodTime)
  {
    return 1 / calcU(volatility, periodTime);
  }
  
  // calculate P
  public static double calcP(double interestRate, double periodTime, double d, double u)
  {
    return (Math.exp(interestRate * periodTime) - d) / (u - d);
  }
  
  // calculate C
  public static double calcC (double interestRate, double periodTime, double P, double Cu, double Cd)
  {
   return Math.exp(interestRate * periodTime * -1) * ((P * Cu) + ((1 - P) * Cd));
  }
  
}
