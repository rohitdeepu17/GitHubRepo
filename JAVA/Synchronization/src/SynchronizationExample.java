import java.util.Arrays;

//Reference : http://www.journaldev.com/1061/java-synchronization-and-thread-safety-tutorial-with-examples
public class SynchronizationExample {
	public static void main(String[] args) throws InterruptedException{
		ProcessingThread pt = new ProcessingThread();
		Thread t1 = new Thread(pt,"t1");
		Thread t2 = new Thread(pt,"t2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println("The value of count = "+pt.getCount());
		
		String[] arr = {"1","2","3","4","5","6"};
		HashMapProcessor hmp = new HashMapProcessor(arr);
		Thread thread1 = new Thread(hmp,"t1");
		Thread thread2 = new Thread(hmp,"t2");
		Thread thread3 = new Thread(hmp,"t3");
		long start = System.currentTimeMillis();
		thread1.start();
		thread2.start();
		thread3.start();
		thread1.join();
		thread2.join();
		thread3.join();
		System.out.println("Time taken= "+(System.currentTimeMillis()-start));
        //check the shared variable value now
        System.out.println(Arrays.asList(hmp.getArr()));
	}
	
}

class ProcessingThread implements Runnable{
	//dummy object variable for synchronization
	private Object mutex=new Object();
	
	private int count=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=1;i<5;i++){
			doSomething(i);
			//count++;
			
			//using synchronized block to read, increment and update count value synchronously
			synchronized (mutex) {
			        count++;
			}
		}
	}
	
	public int getCount(){
		return this.count;
	}
	
	private void doSomething(int i){
		try{
			Thread.sleep(i*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class HashMapProcessor implements Runnable{
	//use this lock for synchronization
	private Object lock = new Object();
	
	private String[] arr = null;
	
	public HashMapProcessor(String[] arr) {
		super();
		this.setArr(arr);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		processArr(Thread.currentThread().getName());
	}
	
	private void processArr(String name){
		for(int i=0;i<arr.length;i++){
			doSomething();
			addThreadName(i,name);
		}
	}

	private void addThreadName(int i, String name) {
		// TODO Auto-generated method stub
		//arr[i] = arr[i]+":"+name;
		//use the following sychronized block for locking
		synchronized(lock){
		    arr[i] = arr[i] +":"+name;
		}

	}

	private void doSomething() {
		// TODO Auto-generated method stub
		try{
			Thread.sleep(3000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public String[] getArr() {
		return arr;
	}

	public void setArr(String[] arr) {
		this.arr = arr;
	}
	
}