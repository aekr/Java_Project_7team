import java.util.Scanner;
class Add{
	private int a;
	private int b;
	void setValue(int a,int b) {
		this.a=a;
		this.b=b;
	}
	public int calculate() {
		return a+b;
	}
}
class Sub{
	private int a;
	private int b;
	void setValue(int a,int b) {
		this.a=a;
		this.b=b;
	}
	public int calculate() {
		return a-b;
	}
}
class Mul{
	private int a;
	private int b;
	void setValue(int a,int b) {
		this.a=a;
		this.b=b;
	}
	public int calculate() {
		return a*b;
	}
}
class Div{
	private int a;
	private int b;
	void setValue(int a,int b) {
		this.a=a;
		this.b=b;
	}
	public int calculate() {
		return a/b;
	}
}
public class Example11 {
	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		System.out.print("두 정수의 연산자를 입력하시오>>");
		int n1=scanner.nextInt();
		int n2=scanner.nextInt();
		String s=scanner.next();
		if(s.equals("+")){
			Add add=new Add();
			add.setValue(n1,n2);
			System.out.print(add.calculate());
		}
		else if(s.equals("-")){
			Sub sub=new Sub();
			sub.setValue(n1,n2);
			System.out.print(sub.calculate());
		}
		else if(s.equals("*")){
			Mul mul=new Mul();
			mul.setValue(n1,n2);
			System.out.print(mul.calculate());
		}
		else if(s.equals("/")){
			Div div=new Div();
			div.setValue(n1,n2);
			System.out.print(div.calculate());
		}
		scanner.close();
	}
}
