package guardian.being;

public class Cut extends GWound {
	public int openness;
	public int recoveryRate;
	public void recover(){
		openness -= recoveryRate;
	}

	public boolean hasHealed(){
		if (openness < 0){
			return true;
		}
		return false;
	}

	public void setRecoveryRate(int rate){
		recoveryRate = rate;
	}

	public Cut(int cutGravity, BodyPart location){
		openness = cutGravity;
		recoveryRate = location.getCutRecovery();
	}

	public int getIntegrityReduction(){
		return openness;
	}


}