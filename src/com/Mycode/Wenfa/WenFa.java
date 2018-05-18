package com.Mycode.Wenfa;
/**
 * 1.从文件中读入文法(可修改文法内容)
 * 2.构造first集
 * 3.构造follow集
 * 4.构造预测分析表
 * 5.用户输入给定句子
 * 6.模拟进栈出栈过程
 * @author 林宇
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
	// 存放文法的每一个产生式
	static private ArrayList<MyString> wenfa = new ArrayList<MyString>();
	// 存放First集
	static Map<String, MyString> firsts = new HashMap<String, MyString>();
	// 存放Follow集
	static Map<String, MyString> follows = new HashMap<String, MyString>();
	// 存放预测分析表
	static Map<String, String> M = new HashMap<String, String>();
	// 存放所有的终结符，为了程序方便，此处包含空字
	static ArrayList<String> AT = new ArrayList<String>();
	// 存放所有的非终结符
	static ArrayList<String> ANT = new ArrayList<String>();
	// 记录所有的终结符，set集合不会出现重复元素
	static Set<String> ST = new HashSet<String>();
    // 记录所有的非终结符，set集合不会出现重复元素
	static Set<String> SNT = new HashSet<String>();
	// 记录文法产生式的总数
	static int sum = 0;
	// 还有多少个非终结符的first集未计算
	static int firstcount = 0;
	// 还有多少个非终结符的follow集未计算
	static int followcount = 0; 
	//输入的句子
	static String input;
	//分析栈
	static ArrayList<String>stack=new ArrayList<String>();
	//记录文法的开始符号
	static String begin;

	// 静态语句初始化快，用于存放文法
	static {
		    BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("g:/Code/wenfa.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("要读取的文法文件不存在！");
		}
		String line;
		// 读取产生式的每一行，存放到wenfa容器中
		try {
			while ((line = reader.readLine()) != null) {
				MyString str = new MyString();
				str.setLeft(line.substring(0, line.indexOf("-")));
				str.setRight(line.substring(line.indexOf(">") + 1, line.length()));
				// 存放每一行文法
				wenfa.add(str);
				// 存放所有的非终结符
				SNT.add(str.getLeft());
				// 存放所有的终结符
				for (int i = 0; i < str.getRight().length(); i++) {
					if (str.isT(str.getRight().charAt(i))) {
						ST.add(String.valueOf(str.getRight().charAt(i)));
					}
				}
				// 每次读取一行，文法产生式的总数+1
				sum++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		firstcount = sum;
		followcount = sum;
		// 构造First集
		First();
		// 构造Follow集
		Follow();
		//构造预测分析表
		Analyze();
	}

	// 构造First集
	private static void First() {
		// 先求产生式右部第一个字符为终结符的first集
		for (MyString temp : wenfa) {
			if (temp.isT()) {
				if (!firsts.containsKey(temp.getLeft())) {
					firsts.put(temp.getLeft(), new MyString());
				}
				MyString myString = firsts.get(temp.getLeft());
				myString.setLeft(temp.getLeft());
				myString.appendFirst(String.valueOf((temp.getRight().charAt(0))));
				// 每求出一个first集，未求出的first集数目减一
				firstcount--;
			}

		}
		// 再求产生式右部第一个字符为非终结符的first集
		while (firstcount != 0) {
			for (MyString temp : wenfa) {
				// 读取一个产生式
				if (temp.isNT() && !firsts.containsKey(temp.getLeft())) {
					// 获得该产生式右部
					String string = temp.getRight().toString();
					// 获取该产生式右部的字符个数
					int len = string.length();
					int sort = 0;
					for (int i = 0; i < len; i++) {
						// 如果该右部的非终结符的first已经求出，并入first集
						if (firsts.containsKey(String.valueOf(string.charAt(i)))) {
							sort++;
							if (sort == len) {
								firsts.put(temp.getLeft(), new MyString());
								MyString myString = firsts.get(temp.getLeft());
								myString.setLeft(temp.getLeft());
								for (int j = 0; j < len; j++) {
									myString.appendFirst(firsts.get(String.valueOf(string.charAt(j))).getRight().toString());
									// 如果不包含空字，则到此结束
									if (!firsts.get(String.valueOf(string.charAt(j))).getRight().toString()
											.contains("ε")) {
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
		// 初始化所有的终结符的First集都为自身，便于后面的follow构造方便
		Iterator<String> iterator = ST.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			firsts.put(string, new MyString());
			MyString myString = firsts.get(string);
			myString.setLeft(string);
			myString.appendFirst(string);

		}

	}

	// 构造Follow集
	private static void Follow() {
		// 1.先将#放入文法开始符的Follow集
		MyString temp1 = wenfa.get(0);
		follows.put(temp1.getLeft(), new MyString());
		MyString myString = follows.get(temp1.getLeft());
		myString.setLeft(temp1.getLeft());
		myString.appendFollow("#");
		// 2.第一遍扫描，若有A->....BP形式，则将First(P)去空字加入到Follow(B)中。
		for (MyString temp2 : wenfa) {
			int len = temp2.getRight().length();
			if (len >= 2) {
				// 获得P的First集
				MyString P = firsts.get(String.valueOf(temp2.getRight().charAt(len - 1)));
				// 获得B的符号
				char B = temp2.getRight().charAt(len - 2);
				// 如果B是非终结符，则将First(P)去空字加入到Follow(B)中。
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
		// 设置循环次数，便于follow集的同步
		int number = 0;
		while (number != 10) {
			// 3.第二遍扫描，若有A->.....P形式，则将Follow(A)加入到Follow(P)中。
			for (MyString temp3 : wenfa) {
				// 获得产生式右部的长度
				int len = temp3.getRight().length();
				// 获得产生式右部P的单个字符
				String pString = String.valueOf(temp3.getRight().charAt(len - 1));
				// 如果P是非终结符，则将Follow(A)加入到Follow(P)中。
				if (temp3.isNT(pString.charAt(0))) {
					// 获得A的follow集
					MyString A = follows.get(temp3.getLeft());
					// 如果A的Follow集存在了，才可以加入到Follow(p)中，也就是执行下面的代码
					if (A != null) {
						// 获得P的follow集
						if (!follows.containsKey(pString)) {
							follows.put(pString, new MyString());
						}
						MyString myString3 = follows.get(pString);
						myString3.setLeft(pString);
						myString3.appendFollow(A.getRight().toString());
					}

				}
			}
			// 4.第三遍扫描，若有A->.....PB形式，B的first集包含空字，则将Follow(A)加入到Follow(P)中。
			for (MyString temp4 : wenfa) {
				// 获得产生式右部的长度
				int len = temp4.getRight().length();
				if (len >= 2) {
					// 获得产生式右部B的单个字符
					String bString = String.valueOf(temp4.getRight().charAt(len - 1));
					String pString = String.valueOf(temp4.getRight().charAt(len - 2));
					// 获得B的first集合
					MyString firstB = firsts.get(bString);
					// 如果B是非终结符且B的first集包含空字，则将Follow(A)加入到Follow(P)中。
					if (temp4.isNT(pString.charAt(0)) && firstB.getRight().toString().contains("ε")) {
						// 获得A的follow集
						MyString A = follows.get(temp4.getLeft());
						// 如果A的Follow集存在了，才可以加入到Follow(p)中，也就是执行下面的代码
						if (A != null) {
							// 获得P的follow集
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
	//构造预测分析表
	private static void Analyze() {
		//遍历原先记录的终结符
		Iterator<String> iterator = ST.iterator();
		String string;
		while (iterator.hasNext()) {
			string=iterator.next();
			if(!string.equals("ε"))
			{
				AT.add(string);
			}
		}
		//加入#号
		AT.add("#");
		//遍历原先记录的非终结符
		Iterator<String> iterator2 = SNT.iterator();
		while (iterator2.hasNext()) {
			ANT.add(iterator2.next());
		}
		//预测分析表初始化全部为error
		for(String nt:ANT) {
			for(String t:AT) {
				M.put(nt+","+t, "error");
			}
		}
		//第一步，先根据First集构造分析表
		for(String nt:ANT) {
			//获取所有非终结符的first集
			MyString myString=firsts.get(nt);
			//获取first的长度
			int len=myString.getRight().length();
			//获取first集的所有元素
			String sum=myString.getRight().toString();
			//如果包含空字，长度减1，因为预测分析表不包含空字
			if(myString.getRight().toString().contains("ε")) {
				len-=1;
			}
			for(int i=0;i<len;i++) {
				//一个个读取该first集的终结符
				char c=sum.charAt(i);
				if(c=='ε') {
					//如果读到空字，则什么也不做
					//因为预测分析表没有空字
				}
				else {
					for(MyString temp : wenfa)
					{
						//遇到空字的产生式不能加入，要加入它的另外一个产生式，优先加入包含该终结符的产生式
						if(temp.getLeft().equals(myString.getLeft())&&(!temp.getRight().toString().equals("ε"))) {
							if(temp.getRight().toString().contains(String.valueOf(c))) {
								M.put(nt+","+c, temp.getRight().toString());
							}
						}
					}
					for(MyString temp : wenfa)
					{
						//遇到空字的产生式不能加入，要加入它的另外一个产生式，弥补前一个循环没有加入的产生式
						if(temp.getLeft().equals(myString.getLeft())&&(!temp.getRight().toString().equals("ε"))) {
							//如果该项已经有产生式了，则不加入，error为初始态，为第一次循环之后还没有找到
							//包含该终结符的产生式，直接将唯一的产生式加入
							if(M.get(nt+","+c).equals("error")) {
								M.put(nt+","+c, temp.getRight().toString());
								
							}
						}
					}
					
				}
			}
			
		}
		//第二步，根据Follow集构造分析表
		for(String temp:firsts.keySet()) {
			MyString first=firsts.get(temp);
			//如果是非终结符
			if(first.isNT(temp.charAt(0))) {
			//获取first集的左部
			String firstKey=first.getLeft();
			//获取first集的右部
			String firstValue=first.getRight().toString();
			//先判断哪个First集含有空字
			if(firstValue.contains("ε")) {
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
						//遇到空字的产生式才加入
						if(wf.getLeft().equals(nt)&&(wf.getRight().toString().equals("ε"))) {
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

	// 将输入的句子进行查表，分析,进行出栈进栈操作
	private static void Stack() {
		System.out.println("用户输入的句子是：" + input);
		// 获得文法的开始符号
		begin = wenfa.get(0).getLeft();
		// 将#和文法开始符号压栈
		stack.add("#");
		stack.add(begin);
		// 记录当前扫描串的位置
		int pos = 0;
		// 记录栈顶的符号
		String x = "empty";
		// 把第一个输入符号读入a
		String a = String.valueOf(input.charAt(0));
		do {
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("符号栈 ：" + stack.toString() + "  当前输入符号：" + a);
			// 把栈顶符号弹出，并且放入x中
			x = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			// 如果该符号是终结符
			if (wenfa.get(0).isT(x.charAt(0))) {
				if (x.equals(a)) {
					if (!a.equals("#")) {
						pos++;
						a = String.valueOf(input.charAt(pos));
					} else {
						System.err.println("输入串含有语法错误！");
						break;
					}

				}
			} else {
                 if(x.equals("#")&&a.equals("#")) 
                 {
                	 System.out.println("分析成功！该句子是文法的句子！");
                 }
                 else{
				      String temp = M.get(x + "," + a);
				      if (temp != null && !temp.equals("error")) {
					  // 不是空字的产生式才压栈
					   if (!temp.equals("ε")) {
						int len = temp.length();
						len--;
						for (; len >= 0; len--) {
							stack.add(String.valueOf(temp.charAt(len)));
						}
					}
				} 
				      else {
							System.err.println("输入串含有语法错误！");
							break;
						}
              }
                 

			}
		} while (x != "#");
		//分析之后，清空栈，便于进行下一次的分析
		stack.clear();
	}

	public static void main(String[] args) {
		// 输出从文件中读取的文法
		System.out.println("输入的文法为：");
		for (MyString temp : wenfa) {
			System.out.println(temp.getLeft() + "->" + temp.getRight());
		}
		System.out.println("");
		// 输出终结符
		System.out.print("终结符为：     {");
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
		// 输出非终结符
		System.out.print("非终结符为： {");
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
		// 输出First集
		System.out.println("构造的First集为：");
		for (String temp : firsts.keySet()) {
			MyString myString = firsts.get(temp);
			System.out.println("First" + "(" + myString.getLeft() + ")=" + " {" + myString.getRight() + "}");
		}
		System.out.println();
		System.out.println("构造的Follow集为：");
		for (String temp : follows.keySet()) {
			MyString myString = follows.get(temp);
			System.out.println("Follow" + "(" + myString.getLeft() + ")=" + " {" + myString.getRight() + "}");
		}
		System.out.println();
		System.out.println("预测分析表如下：");
		System.out.print("     ");//四个空格
		//输出第一行的标头,由终结符和#号组成
		for(String t:AT)
		{
			System.out.print(t+"      ");
		}
		System.out.println();
		for(String nt:ANT) {
			//输出每一行的非终结符
			System.out.print(nt);
			for(String t:AT) {
				String key=nt+","+t;
				String value=M.get(key);
				System.out.print("  "+value);//两个空格
			}
			//每输出完一行，换行
			System.out.println();
		}
		while(true) {
		System.out.println("请输入句子：");
		Scanner scanner=new Scanner(System.in);
		input=scanner.next();
		//进行查表分析,模拟进栈出栈的过程
	    Stack();
		}
		

	}
}
