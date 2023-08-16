package guardian.world;

public class GTime {
	private int second, minute, hour, day, month, year;
	private long secondsSinceCreation;

	private final static int MINUTEMOD = 60, HOURMOD = 60, DAYMOD = 24,
				MONTHMOD = 30, YEARMOD = 360;

	public void addSeconds(int what){
		secondsSinceCreation += what;
		addMinutes ( (int)((second + what)/MINUTEMOD));
		second = (second + what) % MINUTEMOD;
	}

	private void addMinutes(int what){
		addHours ( (int)((minute+what)/HOURMOD));
		minute = (minute + what) % HOURMOD;

	}

	private void addHours(int what){
		addDays ( (int)((hour + what) /DAYMOD));
		hour = (hour + what) % DAYMOD;

	}

	private void addDays(int what){
		addMonths ( (int)((day + what)/MONTHMOD));
		day = (day + what) % MONTHMOD;

	}

	private void addMonths(int what){
		addYears ( (int)((month + what)/YEARMOD));
		month = (month + what) % YEARMOD;

	}

	private void addYears(int what){
		year += what;
	}

	public String toString(){
		//return day+"/"+month+"/"+year+","+hour+":"+minute+":"+second;
		return "("+secondsSinceCreation+")";
	}

	public boolean isAfter(GTime what){
		//return true;
		return (secondsSinceCreation >= what.getSecondsSinceCreation());
	}

	public GTime(GTime base){
		secondsSinceCreation = base.getSecondsSinceCreation();
		second = base.getSecond();
		minute = base.getMinute();
		hour = base.getHour();
		day = base.getDay();
		month = base.getMonth();
		year = base.getYear();
	}

	public GTime(int pYear, int pMonth, int pDay, int pHour, int pMinute, int pSecond){
		year = pYear;
		month = pMonth;
		day = pDay;
		hour = pHour;
		minute = pMinute;
		second = pSecond;
		secondsSinceCreation = second + minute * MINUTEMOD + hour * MINUTEMOD * HOURMOD +
			day * MINUTEMOD * HOURMOD * DAYMOD + month * MINUTEMOD * HOURMOD * DAYMOD * MONTHMOD +
			year * MINUTEMOD * HOURMOD * DAYMOD * MONTHMOD * YEARMOD;
	}

	public GTime(int pYear, int pMonth, int pDay){
		year = pYear;
		month = pMonth;
		day = pDay;
		hour = 0;
		minute = 0;
		second = 0;
		secondsSinceCreation = second + minute * MINUTEMOD + hour * MINUTEMOD * HOURMOD +
			day * MINUTEMOD * HOURMOD * DAYMOD + month * MINUTEMOD * HOURMOD * DAYMOD * MONTHMOD +
			year * MINUTEMOD * HOURMOD * DAYMOD * MONTHMOD * YEARMOD;
	}


	public int getSecond() {
		return second;
	}

	public int getMinute() {
		return minute;
	}

	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public boolean equals(Object o){
		GTime gt = (GTime) o;
		return secondsSinceCreation == gt.getSecondsSinceCreation();
	}

	public long getSecondsSinceCreation(){
		return secondsSinceCreation;
	}
}