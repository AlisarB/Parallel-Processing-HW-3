import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class SockMachine
{
   //sock color counters
   public static int red = 0;
   public static int green = 0;
   public static  int blue = 0;
   public static int orange = 0;
   public static class Washer implements Callable //Class for Washing thread
   {
      public Washer()
      {
      }
      public Object call() throws Exception
      {
         System.out.println("We are now in the Washer thread!");
         red = 0;
         green = 0;
         blue = 0;
         orange = 0;
         System.out.println("The socks have been destroyed...");
         return 0;
      }
   }
   public static class Matcher implements Callable //Class for matching thread
   {
      public Matcher()
      {
      }
      public Object call() throws Exception
      {
         //make number of sockes of each color even, so each is a pair
         System.out.println("Inside the Matching Thread!");
         if(red%2 != 0)
         {
            red--;
         }
         if(green%2 != 0)
         {
            green--;
         }
         if(blue%2 != 0)
         {
            blue--;
         }
         if(orange%2 != 0)
         {
            orange--;
         }
         System.out.println("We have " + red + " many pairs of red socks");
         System.out.println("We have " + green + " many pairs of green socks");
         System.out.println("We have " + blue + " many pairs of blue socks");
         System.out.println("We have " + orange + " many pairs of orange socks");
         return 1;
      }
   }
   public static class Sock implements Callable
   { 
      int color;
      Random rand = new Random();
      public Sock()
      {
         color = makeColor();
      }
      private int makeColor()
      {
         // 0 = red, 1 = green, 2 = blue, 3 = orange
         int num;
         num = rand.nextInt(4);
         return num;
      }
      public int getColor()
      {
         return color;
      }
      @Override
      public Object call() throws Exception
      {
         
         int numEachSocks = 0;
         int amount = (rand.nextInt(100) + 1);
         Sock[] socks = new Sock[amount];
         System.out.println("Just entered " + Thread.currentThread().getName());
         for(int i = 0; i < amount; i++)
         {
            socks[i] = new Sock();
            if(socks[i].getColor() == 0)
            {
               red++;
            }
            else if(socks[i].getColor() == 1)
            {
               green++;
            }
            else if(socks[i].getColor() == 2)
            {
               blue++;
            }
            else if(socks[i].getColor() == 3)
            {
               orange++;
            }
         }
         System.out.println(Thread.currentThread().getName() + " has just finished its run method and made " + amount + " socks");
         return 0;
      }
   }
   
   
   public static void main(String[] args) throws Exception
   {
      int length;
      FutureTask matchTask = new FutureTask(new Matcher());
      Thread matchingThread = new Thread(matchTask);
      FutureTask washerTask = new FutureTask(new Washer());
      Thread washerThread = new Thread(washerTask);
      Sock[] arr = new Sock[100];
      FutureTask[] tasks = new FutureTask[4];
      Thread[] threads = new Thread[4];
     
      for(int i = 0; i < tasks.length; i++)
      {
         tasks[i] = new FutureTask(new Sock());
         threads[i] = new Thread(tasks[i]);
         threads[i].start();
         tasks[i].get();
      }
      matchingThread.sleep(1000);
      matchingThread.start();
      washerThread.sleep(1000);
      washerThread.start();
      
         
      
   }
}