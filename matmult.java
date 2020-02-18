import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class matmult extends RecursiveAction
{
   private static float[][] matA;
   private static float[][] matB;
   private static float[][] matC;
   private static int rowsM;
   private static int rowcolN;
   private static int colsP;
   protected static int threshold = 1;
   private static int split = 0;
   
   //This method multiplies a matrix 'A' which has 1 row, by matrix 'B'
   protected void computeDirectly()
   {
      for(int i = 0; i < colsP; i++)
      {
         for(int j = 0; j < rowcolN; j++)
         {
            matC[split][colsP] = matC[split][colsP] + matA[split][rowcolN]*matB[rowcolN][colsP];
         }
      }
      split = split + 2;
   }
   
   //Continue splitting Matrix A by its rows until it is a matrix of one row
   protected void compute()
   {
      if(split == 0) //If the matrix A has 1 row, computeDirectly
      {
         computeDirectly();
         return;
      }
      
      //Keep cutting down matrix A until it is a matrix of one row, then compute directly with that row segment of A, by the entire matrix B
      while(split != rowsM || split < rowsM)
      {
         if(split+1 < rowsM && rowsM != 1)
         {
            invokeAll(new matmult(matA, matB, split, rowcolN, colsP), new matmult(matA, matB, split+1, rowcolN, colsP));
         }
         else if(split+1 == rowsM)
         {
            invokeAll(new matmult(matA, matB, split, rowcolN, colsP));
         }
      }
      
   
   }
   public matmult(float[][] A, float[][] B, int m, int n, int p)
   {
      matA = A;
      matB = B;
      rowsM = m;
      rowcolN = n;
      colsP = p;
   }
   
   public static int multiply()
   {
      matmult mm = new matmult(matA, matB, rowsM, rowcolN, colsP);
      ForkJoinPool pool = new ForkJoinPool();
      long startTime = System.currentTimeMillis();
      pool.invoke(mm);
      long endTime = System.currentTimeMillis();
      System.out.println("Multiplication took " + (endTime - startTime) + " milliseconds");
      return 1;
   }
   public static void main(String[] args) throws Exception
   {
      Random rand = new Random();
      int nValue = rand.nextInt(10);
      int mValue = rand.nextInt(10);
      int pValue = rand.nextInt(10);
      matA = new float[mValue][nValue];
      matB = new float[nValue][pValue];
      matC = new float[mValue][pValue];
      for(int i = 0; i < mValue; i++)
      {
         for(int j = 0; j < nValue; j++)
         {
            matA[i][j] = rand.nextInt(10);
         }
      }
     for(int i = 0; i < nValue; i++)
     {
        for(int j = 0; j < pValue; j++)
        {
           matB[i][j] = rand.nextInt(10);
        }
     }
     for(int i = 0; i < mValue; i++)
     {
        for(int j = 0; j < pValue; j++)
        {
           matC[i][j] = 0;
        }
     }
     multiply();
     for(int i = 0; i < mValue; i++)
     {
        for(int j = 0; j < pValue; j++)
        {
           System.out.print(matC[i][j]);
        }
        System.out.println();
     }
  }
}