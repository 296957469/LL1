package com.Mycode.Wenfa;

/**
 * 自定义的数据结构
 * 可以用来存储以下结构
 * 1.存放文法每一个产生式的左部和右部
 * 2.存放First集
 * 3.存放Follow集
 * @author 林宇
 *
 */
public class MyString {
	//存放产生式的左部
	private String left;
	//存放产生式的右部
	private StringBuilder right;
	
	//默认构造器
	public MyString() {
		super();
		right = new StringBuilder();
	}
	
	// 用于构造First集的添加
	public MyString appendFirst(String c) {
		// 将要添加的字符串拆成一个个字符，判断一个个字符是否已经包含在该first集
		//不包含则添加，包含则跳过
		//获取该字符串的长度
		int len = c.length();
		//一个个判断该字符是否已经存在first集中
		for (int i = 0; i < len; i++) {
			if (right.toString().contains(String.valueOf(c.charAt(i)))) {
				// 重复则不添加
			} else {
				right.append(String.valueOf(c.charAt(i)));
			}
		}
		return this;
	}

	// 用于构造Follow的添加，去除空字的加入
	public MyString appendFollow(String c) {
		// 将要添加的字符串拆成一个个字符，判断一个个字符是否已经包含在该follow集
	    //不包含则添加，包含则跳过
	    //获取该字符串的长度
		int len = c.length();
		//一个个判断该字符是否已经存在follow集中
		for (int i = 0; i < len; i++) {
			if (right.toString().contains(String.valueOf(c.charAt(i)))) {
				// 重复则不添加
			}
			// 含有空字则不添加
			else if (c.charAt(i) != 'ε') {
				right.append(String.valueOf(c.charAt(i)));
			}
		}
		return this;
	}
	
   //获取产生式的左部
	public String getLeft() {
		return left;
	}
	
   //赋值产生式的左部
	public void setLeft(String left) {
		this.left = left;
	}
	
    //获取产生式的右部
	public StringBuilder getRight() {
		return right;
	}
	
    //赋值产生式的右部
	public void setRight(String right) {
		this.right.append(right);
	}

	// 判断产生式右部第一个字符是否是终结符，是返回true，否则返回false
	public boolean isT() {
		char c = right.charAt(0);
		if (('a' <= c && c <= 'z') || (c == '+') || (c == '-') || (c == '*') || (c == '(') || (c == ')')
				|| (c == 'ε')) {
			return true;
		} else {
			return false;
		}
	}

	// 判断产生式右部第一个字符是否是非终结符，是返回true，否则返回false
	public boolean isNT() {
		char c = right.charAt(0);
		if ('A' <= c && c <= 'Z') {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否是终结符，是返回true，否则返回false，提供给外部使用
	//传入一个char字符进行判断
	public boolean isT(char c) {
		if (('a' <= c && c <= 'z') || (c == '+') || (c == '-') || (c == '*') || (c == '(') || (c == ')')
				|| (c == 'ε')) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否是非终结符，是返回true，否则返回false，提供给外部使用
	//传入一个char字符进行判断
	public boolean isNT(char c) {
		if ('A' <= c && c <= 'Z') {
			return true;
		} else {
			return false;
		}
	}
}
