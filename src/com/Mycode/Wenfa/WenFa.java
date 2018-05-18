package com.Mycode.Wenfa;
/**
 * 1.���ļ��ж����ķ�(���޸��ķ�����)
 * 2.����first��
 * 3.����follow��
 * 4.����Ԥ�������
 * 5.�û������������
 * 6.ģ���ջ��ջ����
 * @author ����
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class WenFa {
	// ����ķ���ÿһ������ʽ
	static private ArrayList<MyString> wenfa = new ArrayList<MyString>();
	// ���First��
	static Map<String, MyString> firsts = new HashMap<String, MyString>();
	// ���Follow��
	static Map<String, MyString> follows = new HashMap<String, MyString>();
	// ���Ԥ�������
	static Map<String, String> M = new HashMap<String, String>();
	// ������е��ս����Ϊ�˳��򷽱㣬�˴���������
	static ArrayList<String> AT = new ArrayList<String>();
	// ������еķ��ս��
	static ArrayList<String> ANT = new ArrayList<String>();
	// ��¼���е��ս����set���ϲ�������ظ�Ԫ��
	static Set<String> ST = new HashSet<String>();
    // ��¼���еķ��ս����set���ϲ�������ظ�Ԫ��
	static Set<String> SNT = new HashSet<String>();
	// ��¼�ķ�����ʽ������
	static int sum = 0;
	// ���ж��ٸ����ս����first��δ����
	static int firstcount = 0;
	// ���ж��ٸ����ս����follow��δ����
	static int followcount = 0; 
	//����ľ���
	static String input;
	//����ջ
	static ArrayList<String>stack=new ArrayList<String>();
	//��¼�ķ��Ŀ�ʼ����
	static String begin;

	// ��̬����ʼ���죬���ڴ���ķ�
	static {
		    BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("g:/Code/wenfa.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Ҫ��ȡ���ķ��ļ������ڣ�");
		}
		String line;
		// ��ȡ����ʽ��ÿһ�У���ŵ�wenfa������
		try {
			while ((line = reader.readLine()) != null) {
				MyString str = new MyString();
				str.setLeft(line.substring(0, line.indexOf("-")));
				str.setRight(line.substring(line.indexOf(">") + 1, line.length()));
				// ���ÿһ���ķ�
				wenfa.add(str);
				// ������еķ��ս��
				SNT.add(str.getLeft());
				// ������е��ս��
				for (int i = 0; i < str.getRight().length(); i++) {
					if (str.isT(str.getRight().charAt(i))) {
						ST.add(String.valueOf(str.getRight().charAt(i)));
					}
				}
				// ÿ�ζ�ȡһ�У��ķ�����ʽ������+1
				sum++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		firstcount = sum;
		followcount = sum;
		// ����First��
		First();
		// ����Follow��
		Follow();
		//����Ԥ�������
		Analyze();
	}

	// ����First��
	private static void First() {
		// �������ʽ�Ҳ���һ���ַ�Ϊ�ս����first��
		for (MyString temp : wenfa) {
			if (temp.isT()) {
				if (!firsts.containsKey(temp.getLeft())) {
					firsts.put(temp.getLeft(), new MyString());
				}
				MyString myString = firsts.get(temp.getLeft());
				myString.setLeft(temp.getLeft());
				myString.appendFirst(String.valueOf((temp.getRight().charAt(0))));
				// ÿ���һ��first����δ�����first����Ŀ��һ
				firstcount--;
			}

		}
		// �������ʽ�Ҳ���һ���ַ�Ϊ���ս����first��
		while (firstcount != 0) {
			for (MyString temp : wenfa) {
				// ��ȡһ������ʽ
				if (temp.isNT() && !firsts.containsKey(temp.getLeft())) {
					// ��øò���ʽ�Ҳ�
					String string = temp.getRight().toString();
					// ��ȡ�ò���ʽ�Ҳ����ַ�����
					int len = string.length();
					int sort = 0;
					for (int i = 0; i < len; i++) {
						// ������Ҳ��ķ��ս����first�Ѿ����������first��
						if (firsts.containsKey(String.valueOf(string.charAt(i)))) {
							sort++;
							if (sort == len) {
								firsts.put(temp.getLeft(), new MyString());
								MyString myString = firsts.get(temp.getLeft());
								myString.setLeft(temp.getLeft());
								for (int j = 0; j < len; j++) {
									myString.appendFirst(firsts.get(String.valueOf(string.charAt(j))).getRight().toString());
									// ������������֣��򵽴˽���
									if (!firsts.get(String.valueOf(string.charAt(j))).getRight().toString()
											.contains("��")) {
										break;
									}
								}
								firstcount--;

							}
						}
					}

				}
			}
		}
		// ��ʼ�����е��ս����First����Ϊ�������ں����follow���췽��
		Iterator<String> iterator = ST.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			firsts.put(string, new MyString());
			MyString myString = firsts.get(string);
			myString.setLeft(string);
			myString.appendFirst(string);

		}

	}

	// ����Follow��
	private static void Follow() {
		// 1.�Ƚ�#�����ķ���ʼ����Follow��
		MyString temp1 = wenfa.get(0);
		follows.put(temp1.getLeft(), new MyString());
		MyString myString = follows.get(temp1.getLeft());
		myString.setLeft(temp1.getLeft());
		myString.appendFollow("#");
		// 2.��һ��ɨ�裬����A->....BP��ʽ����First(P)ȥ���ּ��뵽Follow(B)�С�
		for (MyString temp2 : wenfa) {
			int len = temp2.getRight().length();
			if (len >= 2) {
				// ���P��First��
				MyString P = firsts.get(String.valueOf(temp2.getRight().charAt(len - 1)));
				// ���B�ķ���
				char B = temp2.getRight().charAt(len - 2);
				// ���B�Ƿ��ս������First(P)ȥ���ּ��뵽Follow(B)�С�
				if (P.isNT(B)) {
					if (!follows.containsKey(String.valueOf(B))) {
						follows.put(String.valueOf(B), new MyString());
					}
					MyString myString2 = follows.get(String.valueOf(B));
					myString2.setLeft(String.valueOf(B));
					myString2.appendFollow(P.getRight().toString());

				}
			}
		}
		// ����ѭ������������follow����ͬ��
		int number = 0;
		while (number != 10) {
			// 3.�ڶ���ɨ�裬����A->.....P��ʽ����Follow(A)���뵽Follow(P)�С�
			for (MyString temp3 : wenfa) {
				// ��ò���ʽ�Ҳ��ĳ���
				int len = temp3.getRight().length();
				// ��ò���ʽ�Ҳ�P�ĵ����ַ�
				String pString = String.valueOf(temp3.getRight().charAt(len - 1));
				// ���P�Ƿ��ս������Follow(A)���뵽Follow(P)�С�
				if (temp3.isNT(pString.charAt(0))) {
					// ���A��follow��
					MyString A = follows.get(temp3.getLeft());
					// ���A��Follow�������ˣ��ſ��Լ��뵽Follow(p)�У�Ҳ����ִ������Ĵ���
					if (A != null) {
						// ���P��follow��
						if (!follows.containsKey(pString)) {
							follows.put(pString, new MyString());
						}
						MyString myString3 = follows.get(pString);
						myString3.setLeft(pString);
						myString3.appendFollow(A.getRight().toString());
					}

				}
			}
			// 4.������ɨ�裬����A->.....PB��ʽ��B��first���������֣���Follow(A)���뵽Follow(P)�С�
			for (MyString temp4 : wenfa) {
				// ��ò���ʽ�Ҳ��ĳ���
				int len = temp4.getRight().length();
				if (len >= 2) {
					// ��ò���ʽ�Ҳ�B�ĵ����ַ�
					String bString = String.valueOf(temp4.getRight().charAt(len - 1));
					String pString = String.valueOf(temp4.getRight().charAt(len - 2));
					// ���B��first����
					MyString firstB = firsts.get(bString);
					// ���B�Ƿ��ս����B��first���������֣���Follow(A)���뵽Follow(P)�С�
					if (temp4.isNT(pString.charAt(0)) && firstB.getRight().toString().contains("��")) {
						// ���A��follow��
						MyString A = follows.get(temp4.getLeft());
						// ���A��Follow�������ˣ��ſ��Լ��뵽Follow(p)�У�Ҳ����ִ������Ĵ���
						if (A != null) {
							// ���P��follow��
							if (!follows.containsKey(pString)) {
								follows.put(pString, new MyString());
							}
							MyString myString4 = follows.get(pString);
							myString4.setLeft(pString);
							myString4.appendFollow(A.getRight().toString());
						}
					}

				}
			}
			number++;
		}

	}
	//����Ԥ�������
	private static void Analyze() {
		//����ԭ�ȼ�¼���ս��
		Iterator<String> iterator = ST.iterator();
		String string;
		while (iterator.hasNext()) {
			string=iterator.next();
			if(!string.equals("��"))
			{
				AT.add(string);
			}
		}
		//����#��
		AT.add("#");
		//����ԭ�ȼ�¼�ķ��ս��
		Iterator<String> iterator2 = SNT.iterator();
		while (iterator2.hasNext()) {
			ANT.add(iterator2.next());
		}
		//Ԥ��������ʼ��ȫ��Ϊerror
		for(String nt:ANT) {
			for(String t:AT) {
				M.put(nt+","+t, "error");
			}
		}
		//��һ�����ȸ���First�����������
		for(String nt:ANT) {
			//��ȡ���з��ս����first��
			MyString myString=firsts.get(nt);
			//��ȡfirst�ĳ���
			int len=myString.getRight().length();
			//��ȡfirst��������Ԫ��
			String sum=myString.getRight().toString();
			//����������֣����ȼ�1����ΪԤ���������������
			if(myString.getRight().toString().contains("��")) {
				len-=1;
			}
			for(int i=0;i<len;i++) {
				//һ������ȡ��first�����ս��
				char c=sum.charAt(i);
				if(c=='��') {
					//����������֣���ʲôҲ����
					//��ΪԤ�������û�п���
				}
				else {
					for(MyString temp : wenfa)
					{
						//�������ֵĲ���ʽ���ܼ��룬Ҫ������������һ������ʽ�����ȼ���������ս���Ĳ���ʽ
						if(temp.getLeft().equals(myString.getLeft())&&(!temp.getRight().toString().equals("��"))) {
							if(temp.getRight().toString().contains(String.valueOf(c))) {
								M.put(nt+","+c, temp.getRight().toString());
							}
						}
					}
					for(MyString temp : wenfa)
					{
						//�������ֵĲ���ʽ���ܼ��룬Ҫ������������һ������ʽ���ֲ�ǰһ��ѭ��û�м���Ĳ���ʽ
						if(temp.getLeft().equals(myString.getLeft())&&(!temp.getRight().toString().equals("��"))) {
							//��������Ѿ��в���ʽ�ˣ��򲻼��룬errorΪ��ʼ̬��Ϊ��һ��ѭ��֮��û���ҵ�
							//�������ս���Ĳ���ʽ��ֱ�ӽ�Ψһ�Ĳ���ʽ����
							if(M.get(nt+","+c).equals("error")) {
								M.put(nt+","+c, temp.getRight().toString());
								
							}
						}
					}
					
				}
			}
			
		}
		//�ڶ���������Follow�����������
		for(String temp:firsts.keySet()) {
			MyString first=firsts.get(temp);
			//����Ƿ��ս��
			if(first.isNT(temp.charAt(0))) {
			//��ȡfirst������
			String firstKey=first.getLeft();
			//��ȡfirst�����Ҳ�
			String firstValue=first.getRight().toString();
			//���ж��ĸ�First�����п���
			if(firstValue.contains("��")) {
				//System.out.println(follows.get(firstKey).getLeft()+" "+followValue);
				//R #)
				//Y +#)
				String followkey=follows.get(firstKey).getLeft();
				String nt=followkey;
				String followValue=follows.get(firstKey).getRight().toString();
				String c;
				int len=followValue.length();
				for(int i=0;i<len;i++) {
					c=String.valueOf(followValue.charAt(i));
					for(MyString wf: wenfa)
					{
						//�������ֵĲ���ʽ�ż���
						if(wf.getLeft().equals(nt)&&(wf.getRight().toString().equals("��"))) {
							if(M.get(nt+","+c).equals("error")) {
								M.put(nt+","+c,wf.getRight().toString());
							}
						}
					}
				}
			}
		}
	}
}

	// ������ľ��ӽ��в������,���г�ջ��ջ����
	private static void Stack() {
		System.out.println("�û�����ľ����ǣ�" + input);
		// ����ķ��Ŀ�ʼ����
		begin = wenfa.get(0).getLeft();
		// ��#���ķ���ʼ����ѹջ
		stack.add("#");
		stack.add(begin);
		// ��¼��ǰɨ�贮��λ��
		int pos = 0;
		// ��¼ջ���ķ���
		String x = "empty";
		// �ѵ�һ��������Ŷ���a
		String a = String.valueOf(input.charAt(0));
		do {
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("����ջ ��" + stack.toString() + "  ��ǰ������ţ�" + a);
			// ��ջ�����ŵ��������ҷ���x��
			x = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			// ����÷������ս��
			if (wenfa.get(0).isT(x.charAt(0))) {
				if (x.equals(a)) {
					if (!a.equals("#")) {
						pos++;
						a = String.valueOf(input.charAt(pos));
					} else {
						System.err.println("���봮�����﷨����");
						break;
					}

				}
			} else {
                 if(x.equals("#")&&a.equals("#")) 
                 {
                	 System.out.println("�����ɹ����þ������ķ��ľ��ӣ�");
                 }
                 else{
				      String temp = M.get(x + "," + a);
				      if (temp != null && !temp.equals("error")) {
					  // ���ǿ��ֵĲ���ʽ��ѹջ
					   if (!temp.equals("��")) {
						int len = temp.length();
						len--;
						for (; len >= 0; len--) {
							stack.add(String.valueOf(temp.charAt(len)));
						}
					}
				} 
				      else {
							System.err.println("���봮�����﷨����");
							break;
						}
              }
                 

			}
		} while (x != "#");
		//����֮�����ջ�����ڽ�����һ�εķ���
		stack.clear();
	}

	public static void main(String[] args) {
		// ������ļ��ж�ȡ���ķ�
		System.out.println("������ķ�Ϊ��");
		for (MyString temp : wenfa) {
			System.out.println(temp.getLeft() + "->" + temp.getRight());
		}
		System.out.println("");
		// ����ս��
		System.out.print("�ս��Ϊ��     {");
		Iterator<String> iterator = ST.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			if (iterator.hasNext()) {
				System.out.print(string + ",");
			} else {
				System.out.print(string);
			}
		}
		System.out.println("}");
		// ������ս��
		System.out.print("���ս��Ϊ�� {");
		Iterator<String> iterator2 = SNT.iterator();
		while (iterator2.hasNext()) {
			String string = iterator2.next();
			if (iterator2.hasNext()) {
				System.out.print(string + ",");
			} else {
				System.out.print(string);
			}
		}
		System.out.println("}");
		System.out.println("");
		// ���First��
		System.out.println("�����First��Ϊ��");
		for (String temp : firsts.keySet()) {
			MyString myString = firsts.get(temp);
			System.out.println("First" + "(" + myString.getLeft() + ")=" + " {" + myString.getRight() + "}");
		}
		System.out.println();
		System.out.println("�����Follow��Ϊ��");
		for (String temp : follows.keySet()) {
			MyString myString = follows.get(temp);
			System.out.println("Follow" + "(" + myString.getLeft() + ")=" + " {" + myString.getRight() + "}");
		}
		System.out.println();
		System.out.println("Ԥ����������£�");
		System.out.print("     ");//�ĸ��ո�
		//�����һ�еı�ͷ,���ս����#�����
		for(String t:AT)
		{
			System.out.print(t+"      ");
		}
		System.out.println();
		for(String nt:ANT) {
			//���ÿһ�еķ��ս��
			System.out.print(nt);
			for(String t:AT) {
				String key=nt+","+t;
				String value=M.get(key);
				System.out.print("  "+value);//�����ո�
			}
			//ÿ�����һ�У�����
			System.out.println();
		}
		while(true) {
		System.out.println("��������ӣ�");
		Scanner scanner=new Scanner(System.in);
		input=scanner.next();
		//���в�����,ģ���ջ��ջ�Ĺ���
	    Stack();
		}
		

	}
}
