package guardian.being;

public abstract class GWound {
	public abstract void recover();
	public abstract void setRecoveryRate(int rate);
	public abstract boolean hasHealed();
	public abstract int getIntegrityReduction();
}