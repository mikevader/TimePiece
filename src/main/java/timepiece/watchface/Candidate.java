package timepiece.watchface;

public class Candidate {
    private Fitness fitness = new Fitness();


//	public String candidate = //10x10
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX|" +
//		"XXXXXXXXXX";


    private String candidate = //11x11
            "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX|" +
                    "XXXXXXXXXXX";


    //circle
//	public String candidate = //circle
//			      "ONER|" +
//			    "SETHIRTY|" +
//			  "FIFTYPTYHALF|" +
//			  "FTENETWENTYR|" +
//			 "FIVENTPASTNINE|" +
//			 "TIELEVENTHREEH|" +
//			"XSIXTFIFTEENTWOEN|" +
//			"TWELVEEIGHTSEVENR|" +
//			"YWEQUARTERFOURTYE|" +
//			"FOFIVENTCLOCKTENY|" +
//			 "NINEEPASTENTOU|" +
//			 "FIFTEENFOURTHY|" +
//			  "TWOHFIFTYEWR|" +
//			  "THIRTYTWENTY|" +
//			   "FIVENONE|" +
//			     "YTEN";

//		public String candidate = //triangle		
//			"PSEVENSIXTWELVEN|" +
//			"ONELEVENINEIGHT|" +
//			"TWENTYHYYTENNY|" +
//			"FOURTHYTHIRTY|" +
//			"EFIFTEENSTWO|" +
//			"FOURTYFIFTY|" +
//			"THREEFIVEY|" +
//			"PASTHFOUR|" +
//			"FNINEVEY|" +
//			"FIVEONE|" +
//			"CLOCKT|" +
//			"THREE|" +
//			"XTEN|" +
//			"TWO|" +
//			"TO|" +
//			"W";

//		public String candidate = //diamond
//			"H|" +
//			"TO|" +
//			"SIX|" +
//			"NINE|" +
//			"TWONE|" +
//			"FIFTYY|" +
//			"SSEVENO|" +
//			"LEIGHTEN|" +
//			"PASTWELVE|" +
//			"THREELEVEN|" +
//			"FIVEFOURTHY|" +
//			"TWENTYTENO|" +
//			"CLOCKPAST|" +
//			"FIFTEENN|" +
//			"FFOURTY|" +
//			"THIRTY|" +
//			"FIFTY|" +
//			"FIVE|" +
//			"TWO|" +
//			"TO|" +
//			"O";

    @Override
    public String toString() {
        return String.format("%10d: splits: %d, check ok: %d, nok: %d, times ok: %d, nok %d, %s", fitness.getFitness(), fitness.getSplitPos(),
                fitness.getCheckedOK(), fitness.getCheckedNOK(), fitness.getCheckedTimesOK(), fitness.getCheckedTimesNOK(),
                getCandidate());
    }

    public Fitness getFitnessResult() {
        return fitness;
    }

    public void setFitnessResult(Fitness fitness) {
        this.fitness = fitness;
    }

    public int getFitness() {
        return fitness.getFitness();
    }

    public void setFitness(int fitness) {
        this.fitness.setFitness(fitness);
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public int getCheckedOK() {
        return fitness.getCheckedOK();
    }

    public void setCheckedOK(int checkedOK) {
        fitness.setCheckedOK(checkedOK);
    }

    public int getCheckedNOK() {
        return fitness.getCheckedNOK();
    }

    public void setCheckedNOK(int checkedNOK) {
        fitness.setCheckedNOK(checkedNOK);
    }

    public int getCheckedTimesOK() {
        return fitness.getCheckedTimesOK();
    }

    public void setCheckedTimesOK(int checkedTimesOK) {
        fitness.setCheckedTimesOK(checkedTimesOK);
    }

    public int getCheckedTimesNOK() {
        return fitness.getCheckedTimesNOK();
    }

    public void setCheckedTimesNOK(int checkedTimesNOK) {
        fitness.setCheckedTimesNOK(checkedTimesNOK);
    }

    public int getSplitPos() {
        return fitness.getSplitPos();
    }

    public void setSplitPos(int splitPos) {
        fitness.setSplitPos(splitPos);
    }

    public double getVariance() {
        return fitness.getVariance();
    }

    public void setVariance(double variance) {
        fitness.setVariance(variance);
    }

    public double getAvgLen() {
        return fitness.getAvgLen();
    }

    public void setAvgLen(double avgLen) {
        fitness.setAvgLen(avgLen);
    }
}
