import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class OrderedLeaderElection
{
   public static Integer rank = 0;
   public static Integer highestRank = (-1)*(Integer.MAX_VALUE);
   public static class ElectedOfficial implements Callable
   {
      private Random rand = new Random();
      private int negOrPos;
      
      public ElectedOfficial()
      {
         //randomly decides whether positive or negative number
         negOrPos = rand.nextInt(2);
         if(negOrPos == 1)
         {
            rank = rand.nextInt(Integer.MAX_VALUE);
         }
         else
         {
            rank = (-1)*rand.nextInt(Integer.MAX_VALUE);
         }
      }
      public Object call() throws Exception
      {
         System.out.println("My name is " + Thread.currentThread().getName() + ", and I think I am the leader. My ranking is: " + rank);
         System.out.println("The current highest ranking is: " + highestRank);
         return 0;
      }
   }
   public static void main(String[] args) throws Exception
   {
      int highestRankIndex = 0;
      Integer tempRank;
      Random r = new Random();
      int numOfficials = r.nextInt(500);
      
      //Create the rank thread
      ElectedOfficial rankOfficial = new ElectedOfficial();
      FutureTask rankTask = new FutureTask(rankOfficial);
      Thread rankThread = new Thread(rankTask);
      
      //Create the random number of elected official threads
      //Keep track of the highest ranking thread with Integer highestRank
      FutureTask[] tasks = new FutureTask[numOfficials];
      Thread[] threads = new Thread[numOfficials];
      for(int i = 0; i < numOfficials; i++)
      {
         tasks[i] = new FutureTask(new ElectedOfficial());
         threads[i] = new Thread(tasks[i]);
         threads[i].start();         
         tasks[i].get();        
         rankThread.interrupt();  
         if(rank > highestRank)
         {
            highestRank = rank;
         }
      }
      System.out.print("The Elected Official with the highest ranking " + highestRank);
   }
}