package util;

import java.util.*;

public class IntStack {

	private int[] s;

	private int top;

	public IntStack(int size){
		s = new int[size];
		top = -1;
	}

	public int pop(){
		int x = s[top];
		top--;
		return x;
	}

	public void push(int x){
		top++;
		s[top] = x;
	}

	public int peek(){
		return s[top];
	}

	public boolean isEmpty(){
		if (top == -1) return true; else return false;
	}

	public void empty(){
		top = -1;
	}

/*
	private Stack s;

	public IntStack(int size){
		s = new Stack();
	}

	public int pop(){
		return ((Integer) s.pop()).intValue();
	}

	public void push(int x){
		s.push(new Integer(x));
	}

	public int peek(){
		return -1;
	}

	public boolean isEmpty(){
		return s.isEmpty();
	}

  */
}