
//Reference : http://www.journaldev.com/1058/java-deadlock-example-and-how-to-analyze-deadlock-situation
public class DeadlockSituationExample {
	
	public static void main(String[] args) throws InterruptedException{
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();
		
		Thread t1 = new Thread(new SyncThread(obj1,obj2),"t1");
		Thread t2 = new Thread(new SyncThread(obj2,obj3),"t2");
		Thread t3 = new Thread(new SyncThread(obj3,obj1),"t3");
		
		t1.start();
		Thread.sleep(5000);
		t2.start();
		Thread.sleep(5000);
		t3.start();
	}
}

class SyncThread implements Runnable{
	private Object obj1;
	private Object obj2;
	
	public SyncThread(Object obj1, Object obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String threadName = Thread.currentThread().getName();
//		System.out.println(threadName+" acquiring lock on "+obj1);
//		synchronized (obj1) {
//			System.out.println(threadName+" acquired lock on "+obj1);
//			work();
//			System.out.println(threadName+" acquiring lock on "+obj2);
//			synchronized (obj2) {
//				System.out.println(threadName+" acquired lock on "+obj2);
//				work();
//			}
//			System.out.println(threadName + " released lock on "+obj2);
//		}
//		System.out.println(threadName+" released lock on "+obj1);
//		System.out.println(threadName+" finished execution");
		
		//Use the following code to avoid deadlock in above code : do not use nested locks
		System.out.println(threadName+" acquiring lock on "+obj1);
		synchronized (obj1) {
			System.out.println(threadName+" acquired lock on "+obj1);
			work();
		}
		System.out.println(threadName+" released lock on "+obj1);
		System.out.println(threadName+" acquiring lock on "+obj2);
		synchronized (obj2) {
			System.out.println(threadName+" acquired lock on "+obj2);
			work();
		}
		System.out.println(threadName + " released lock on "+obj2);
		System.out.println(threadName+" finished execution");
	}
	
	private void work(){
		try{
			Thread.sleep(30000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
} 
