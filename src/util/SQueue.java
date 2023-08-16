package util;

public class SQueue {
	private int queueSize;
	private Object[] contents;
	private int lastPointer;

	public SQueue(int pSize){
		queueSize = pSize;
		contents = new Object[queueSize];
		lastPointer = 0;
	}

	public Object getHead(){
		return contents[0];
	}

	public Object getTail(){
		return contents[lastPointer];
	}

	public Object elementAt(int i){
		return contents[i];
	}

	public int getEffectiveSize(){
		return lastPointer;
	}

	public void add(Object o){
		//Move all the contents one position
		for (int i=0; i<lastPointer; i++){
			contents[i+1] = contents[i];
		}

		lastPointer++;
		if (lastPointer > queueSize) lastPointer = queueSize;
	}



}