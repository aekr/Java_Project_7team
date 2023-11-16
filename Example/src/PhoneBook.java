import java.util.Scanner;
class Phone {
	String tel;String name;
	public Phone(String a, String b) {
		this.tel=b;this.name=a;
	}
}
public class PhoneBook {
	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		System.out.print("인원수>>");
		int n=scanner.nextInt();
		Phone [] p;
		p=new Phone[n];
		for(int i=0;i<n;i++) {
			System.out.print("이름과 전화번호(이름과 번호는 빈 칸없이 입력)>>");
			String name=scanner.next();
			String tel=scanner.next();
			p[i]=new Phone(name,tel);
		}
		System.out.println("저장되엇습니다.");
		while(true) {
			System.out.print("검색할 이름>>");
			String s=scanner.next();
			if(s.equals("그만")) 
				break;
			else {
				for(int i=0;i<n;i++) {
					if(s.equals(p[i].name)) {
						System.out.println(p[i].name+"의 번호는 "+p[i].tel+"입니다.");
						break;
					}
					else if(i==n-1)
						System.out.println(s+"이 없습니다.");
				}
			}
		}
		scanner.close();
	}
}
