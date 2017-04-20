
public class WaitVsSleepExample {
	private static boolean isFileProcessed = false;
	private static Object o = new Object();		//to acquire lock on
	public class SleepExampleThread1 extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(!isFileProcessed){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			System.out.println("Sleep Example : File processed. Go for next step");
		}
		
	}
	
	public class SleepExampleThread2 extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			System.out.println("Sleep Example : Started processing file");
			//do processing here
			isFileProcessed = true;
		}
	}
	
	public class WaitExampleThread1 extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(!isFileProcessed){
				synchronized (o) {
					try {
						o.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println("Wait Example : File processed. Go for next step");
		}
	}
	
	public class WaitExampleThread2 extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			System.out.println("Wait Example : Started processing file");
			isFileProcessed = true;
			synchronized (o) {
				o.notifyAll();
			}
		}
	}
	
	void testFunctionForSleep(){
		SleepExampleThread1 st1 = new SleepExampleThread1();
		SleepExampleThread2 st2 = new SleepExampleThread2();
		st1.start();
		st2.start();
	}
	
	void testFunctionForWait(){
		WaitExampleThread1 wt1 = new WaitExampleThread1();
		WaitExampleThread2 wt2 = new WaitExampleThread2();
		wt1.start();
		wt2.start();
	}
	
	public static void main(String[] args){
		WaitVsSleepExample mWaitVsSleepExample = new WaitVsSleepExample();
		mWaitVsSleepExample.testFunctionForSleep();
		//mWaitVsSleepExample.testFunctionForWait();
	}
}
