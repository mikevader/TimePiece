package timepiece.watchface;

public class Candidate {
    private int fitness = Integer.MIN_VALUE;


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

    private int checkedOK = 0;
    private int checkedNOK = 0;
    private int checkedTimesOK = 0;
    private int checkedTimesNOK = 0;
    private int splitPos = 0;
    private double variance = 0;
    private double avgLen = 0;

    @Override
    public String toString() {
        return String.format("%10d: splits: %d, check ok: %d, nok: %d, times ok: %d, nok %d, %s", getFitness(), getSplitPos(),
                getCheckedOK(), getCheckedNOK(), getCheckedTimesOK(), getCheckedTimesNOK(),
                getCandidate());
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public int getCheckedOK() {
        return checkedOK;
    }

    public void setCheckedOK(int checkedOK) {
        this.checkedOK = checkedOK;
    }

    public int getCheckedNOK() {
        return checkedNOK;
    }

    public void setCheckedNOK(int checkedNOK) {
        this.checkedNOK = checkedNOK;
    }

    public int getCheckedTimesOK() {
        return checkedTimesOK;
    }

    public void setCheckedTimesOK(int checkedTimesOK) {
        this.checkedTimesOK = checkedTimesOK;
    }

    public int getCheckedTimesNOK() {
        return checkedTimesNOK;
    }

    public void setCheckedTimesNOK(int checkedTimesNOK) {
        this.checkedTimesNOK = checkedTimesNOK;
    }

    public int getSplitPos() {
        return splitPos;
    }

    public void setSplitPos(int splitPos) {
        this.splitPos = splitPos;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getAvgLen() {
        return avgLen;
    }

    public void setAvgLen(double avgLen) {
        this.avgLen = avgLen;
    }
}
