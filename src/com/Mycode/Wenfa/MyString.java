package com.Mycode.Wenfa;

/**
 * �Զ�������ݽṹ
 * ���������洢���½ṹ
 * 1.����ķ�ÿһ������ʽ���󲿺��Ҳ�
 * 2.���First��
 * 3.���Follow��
 * @author ����
 *
 */
public class MyString {
	//��Ų���ʽ����
	private String left;
	//��Ų���ʽ���Ҳ�
	private StringBuilder right;
	
	//Ĭ�Ϲ�����
	public MyString() {
		super();
		right = new StringBuilder();
	}
	
	// ���ڹ���First�������
	public MyString appendFirst(String c) {
		// ��Ҫ��ӵ��ַ������һ�����ַ����ж�һ�����ַ��Ƿ��Ѿ������ڸ�first��
		//����������ӣ�����������
		//��ȡ���ַ����ĳ���
		int len = c.length();
		//һ�����жϸ��ַ��Ƿ��Ѿ�����first����
		for (int i = 0; i < len; i++) {
			if (right.toString().contains(String.valueOf(c.charAt(i)))) {
				// �ظ������
			} else {
				right.append(String.valueOf(c.charAt(i)));
			}
		}
		return this;
	}

	// ���ڹ���Follow����ӣ�ȥ�����ֵļ���
	public MyString appendFollow(String c) {
		// ��Ҫ��ӵ��ַ������һ�����ַ����ж�һ�����ַ��Ƿ��Ѿ������ڸ�follow��
	    //����������ӣ�����������
	    //��ȡ���ַ����ĳ���
		int len = c.length();
		//һ�����жϸ��ַ��Ƿ��Ѿ�����follow����
		for (int i = 0; i < len; i++) {
			if (right.toString().contains(String.valueOf(c.charAt(i)))) {
				// �ظ������
			}
			// ���п��������
			else if (c.charAt(i) != '��') {
				right.append(String.valueOf(c.charAt(i)));
			}
		}
		return this;
	}
	
   //��ȡ����ʽ����
	public String getLeft() {
		return left;
	}
	
   //��ֵ����ʽ����
	public void setLeft(String left) {
		this.left = left;
	}
	
    //��ȡ����ʽ���Ҳ�
	public StringBuilder getRight() {
		return right;
	}
	
    //��ֵ����ʽ���Ҳ�
	public void setRight(String right) {
		this.right.append(right);
	}

	// �жϲ���ʽ�Ҳ���һ���ַ��Ƿ����ս�����Ƿ���true�����򷵻�false
	public boolean isT() {
		char c = right.charAt(0);
		if (('a' <= c && c <= 'z') || (c == '+') || (c == '-') || (c == '*') || (c == '(') || (c == ')')
				|| (c == '��')) {
			return true;
		} else {
			return false;
		}
	}

	// �жϲ���ʽ�Ҳ���һ���ַ��Ƿ��Ƿ��ս�����Ƿ���true�����򷵻�false
	public boolean isNT() {
		char c = right.charAt(0);
		if ('A' <= c && c <= 'Z') {
			return true;
		} else {
			return false;
		}
	}

	// �ж��Ƿ����ս�����Ƿ���true�����򷵻�false���ṩ���ⲿʹ��
	//����һ��char�ַ������ж�
	public boolean isT(char c) {
		if (('a' <= c && c <= 'z') || (c == '+') || (c == '-') || (c == '*') || (c == '(') || (c == ')')
				|| (c == '��')) {
			return true;
		} else {
			return false;
		}
	}

	// �ж��Ƿ��Ƿ��ս�����Ƿ���true�����򷵻�false���ṩ���ⲿʹ��
	//����һ��char�ַ������ж�
	public boolean isNT(char c) {
		if ('A' <= c && c <= 'Z') {
			return true;
		} else {
			return false;
		}
	}
}
