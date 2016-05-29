import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;


public class SingletonExample {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException{
		//This code for reflection can destroy singleton property of any of the classes till BillPughSingleton
		EagerInitializedSingleton instanceOne = EagerInitializedSingleton.getInstance();
		EagerInitializedSingleton instanceTwo = null;
		try{
			Constructor[] constructors = EagerInitializedSingleton.class.getDeclaredConstructors();
			System.out.println("The number of declared constructors = "+constructors.length);
			for(Constructor constructor:constructors){
				System.out.println("inside constructor loop, constructor = "+constructor);
				constructor.setAccessible(true);
				instanceTwo = (EagerInitializedSingleton) constructor.newInstance();
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(instanceOne.hashCode());
        System.out.println(instanceTwo.hashCode());
        
        //Deserialization of SerializedSingleton class object produces a new instance violating singleton property
        //serialize
        SerializedSingleton instance1 = SerializedSingleton.getInstance();
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("object.txt"));
        out.writeObject(instance1);
        out.close();
        
        //deserialize
        ObjectInput in = new ObjectInputStream(new FileInputStream("object.txt"));
        SerializedSingleton instance2 = (SerializedSingleton)in.readObject();
        in.close();
        
        System.out.println("instance1 hashCode="+instance1.hashCode());
        System.out.println("instance2 hashCode="+instance2.hashCode());

	}
}

class EagerInitializedSingleton{
	private static final EagerInitializedSingleton instance = new EagerInitializedSingleton();
	private EagerInitializedSingleton(){}
	public static EagerInitializedSingleton getInstance(){return instance;}
}

class StaticBlockInitializationSingleton{
	private static StaticBlockInitializationSingleton instance;
	static{
		try{
			instance = new StaticBlockInitializationSingleton();
		}catch(Exception e){
			throw new RuntimeException("Exception occured in creating singleton instance");
		}
	}
	private StaticBlockInitializationSingleton(){}
	public static StaticBlockInitializationSingleton getInstance(){return instance;}
}

class LazyInitializationSingleton{
	private static LazyInitializationSingleton instance = null;
	private LazyInitializationSingleton(){}
	public static LazyInitializationSingleton getInstance(){
		if(instance == null)
			instance = new LazyInitializationSingleton();
		return instance;
	}
}

class ThreadSafeSingleton{
	private static ThreadSafeSingleton instance = null;
	private ThreadSafeSingleton(){}
	public static synchronized ThreadSafeSingleton getInstance(){
		if(instance == null)
			instance = new ThreadSafeSingleton();
		return instance;
	}
}

class ThreadSafeDoubleCheckLockingSingleton{
	private static ThreadSafeDoubleCheckLockingSingleton instance = null;
	private ThreadSafeDoubleCheckLockingSingleton(){}
	public static ThreadSafeDoubleCheckLockingSingleton getInstance(){
		if(instance == null){
			synchronized (ThreadSafeDoubleCheckLockingSingleton.class) {
				if(instance == null)
					instance = new ThreadSafeDoubleCheckLockingSingleton();
			}
		}
		return instance;
	}
}

class BillPughSingleton{
	private BillPughSingleton(){}
	private static class InnerHelper{
		private static final BillPughSingleton instance = new BillPughSingleton();
	}
	public static BillPughSingleton getInstance(){
		return InnerHelper.instance;
	}
}

class SerializedSingleton implements Serializable{
	private static final long serialVersionUID = -7604766932017737115L;

    private SerializedSingleton(){}
    
    private static class SingletonHelper{
    	private static final SerializedSingleton instance = new SerializedSingleton();
    }
    
    public static SerializedSingleton getInstance(){
        return SingletonHelper.instance;
    }
    //This function avoids creating a new object of this class during deserialization
    protected Object readResolve() {
        return getInstance();
    }
}
