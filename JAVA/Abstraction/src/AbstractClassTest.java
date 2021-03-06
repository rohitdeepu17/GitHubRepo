//an abstract class can be declared without any abstract method
abstract class A{
	
}

//An abstract class may have non-abstract methods along with abstract methods
abstract class GrandFather{
	int age;
	GrandFather(){
		System.out.println("Constructor of GrandFather called");
	}
	//abstract method of this abstract class
	abstract int getAge();
	void setAge(int x){
		System.out.println("setAge() method of GrandFather called.");
		age = x;
	}
	
	void implementedFunction(){
		System.out.println("A function implemented in abstract class itself");
	}
}

//class Father extends abstract class GrandFather. Therefore, either it should
//implement abstract function OR this class should be declared as abstract
//Here we are implementing abstract method getAge()
class Father extends GrandFather{
	Father(){
		System.out.println("Constructor of Father called");
	}
	
	@Override
	int getAge(){
		return age;
	}
	
	//Overriding method setAge()
	void setAge(int x)
	{
		System.out.println("setAge() method of Father called.");
		age = x;
	}
}

//Here we are declaring class Son as abstract
abstract class Son extends GrandFather{
	Son(){
		System.out.println("Constructor of Son called");
	}
	
	//Overriding method setAge()
	/*void setAge(int x)
	{
		System.out.println("setAge() method of Son called.");
		age = x;
	}*/
}

class Infant extends Son{
	Infant(){
		System.out.println("Constructor of Infant called");
	}

	@Override
	int getAge() {
		// TODO Auto-generated method stub
		return age;
	}
	
}

public class AbstractClassTest {
	public static void main(String args[]){
		//This statement will not work as we cannot instantiate an abstract class
		//GrandFather g = new GrandFather();
		
		//Here we are creating reference of GrandFather class, but the
		//object to which it will refer is of Father class
		GrandFather f = new Father();
		f.setAge(40);
		System.out.println("The age of Father is : " + f.getAge());
		f.implementedFunction();
		
		System.out.println("");
		
		GrandFather i = new Infant();
		//setAge method is not overridden in class Infant and class Son. So, it 
		//will be called from class GrandFather
		i.setAge(2);
		System.out.println("The age of Infant is : " + i.getAge());
		i.implementedFunction();
	}
}
