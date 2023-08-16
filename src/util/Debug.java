package util;

public class Debug {

	static int methodLevel;

	final static boolean debug = false;
	final static boolean timing = false;
	final static boolean gossip = false;


	static long firstTimer, lastTimer;

	public static void startTimer(){
		if (timing)
			firstTimer = System.currentTimeMillis();
	}

	public static void stopTimer(String desc){
		if (timing){
        	lastTimer = System.currentTimeMillis();
			System.out.println("Timing for "+desc+": "+(lastTimer-firstTimer));
		}
	}

	public static void byebye(String why){
		/** Terminates the application because of
		 * a critical, unrecoverable error, with
		 * a message that explains why */
		System.out.println(why);
		System.exit(-1);
	}

	public static void enterMethod(Object cls, String method, Object params){
		if (!debug) return;
		methodLevel++;
		//System.out.println(spaces(methodLevel) + "-"+cls.getClass().getName()+"."+method+"("+params+")");
		//System.out.println(spaces(methodLevel) + "-"+cls+"("+cls.getClass().getName()+")."+method+"("+params+")");
		System.out.println(spaces(methodLevel) + "-"+cls+"."+method+"("+params+")");
	}

	public static void enterMethod(Object cls, String method){
		if (!debug) return;
		methodLevel++;
		//System.out.println(spaces(methodLevel) + "-"+cls.getClass().getName()+"."+method+"()");
		System.out.println(spaces(methodLevel) + "-"+cls+"."+method+"()");
	}

	/*public static void doAssert(boolean expression){

		if (!expression){
			System.out.println("Programming Assertion Failed");
			System.exit(0);
		}
	} */

	public static void doAssert(boolean expression, String msg){
		if (!expression){
			System.out.println("Programming Assertion Failed:"+msg);
			System.exit(0);
		}
	}

	public static void exitMethod(){
		if (!debug) return;
		System.out.println(spaces(methodLevel) +"-done.");
		methodLevel--;
	}

	public static void exitMethod(Object returns){
		if (!debug) return;
		System.out.println(spaces(methodLevel) +"-done, returns "+returns);
		methodLevel--;
	}


	public static void say(String s){
		if (!gossip) return;
		System.out.println(spaces(methodLevel+1) + s);
	}

	public static void say(Object o){
		if (!gossip) return;
		if (o==null){
			System.out.println(spaces(methodLevel+1) + "NULL");
		} else
			System.out.println(spaces(methodLevel+1) + o.toString());
	}

	public static void say(String s, int level){
		if (!gossip) return;
		if (level < 4)
			System.out.println(s);
	}

	private static String spaces(int n){

		String ret = "";
		for (int i=0; i<n; i++){
			ret += "   ";
		}
		return ret;
	}
}