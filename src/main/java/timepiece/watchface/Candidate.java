package timepiece.watchface;

import java.util.Objects;

public class Candidate implements Comparable<Candidate> {
    private Fitness fitness = new Fitness();


//	public String watchFace = //10x10
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


    private String watchFace = //11x11
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
//	public String watchFace = //circle
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

//		public String watchFace = //triangle
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

//		public String watchFace = //diamond
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
        return String.format("%10d: splits: %d, check ok: %d, nok: %d, times ok: %d, nok %d, %s",
                fitness.getScore(),
                fitness.getSplitPos(),
                fitness.getCheckedOK(),
                fitness.getCheckedNOK(),
                fitness.getCheckedTimesOK(),
                fitness.getCheckedTimesNOK(),
                getWatchFace());
    }

    Fitness getFitnessResult() {
        return fitness;
    }

    void setFitnessResult(Fitness fitness) {
        this.fitness = fitness;
    }

    int getFitnessScore() {
        return fitness.getScore();
    }

    void setFitnessScore(int fitness) {
        this.fitness.setScore(fitness);
    }

    public String getWatchFace() {
        return watchFace;
    }

    public void setWatchFace(String watchFace) {
        this.watchFace = watchFace;
    }

    Candidate mixWith(Candidate other, int atPosition) {
        Candidate res = new Candidate();
        res.setWatchFace(this.getWatchFace().substring(0, atPosition) + other.getWatchFace().substring(atPosition));
        return res;
    }

    Candidate changeChar(char character, int atPosition) {
        Candidate res = new Candidate();

        char[] cand = getWatchFace().toCharArray();

        int pos = atPosition;
        while (cand[pos] == '|') {
            pos++;
        }

        cand[pos] = character;

        res.setWatchFace(new String(cand));
        return res;
    }

    @Override
    public int compareTo(Candidate o) {
        return Integer.compare(
                o.getFitnessScore(),
                getFitnessScore());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate1 = (Candidate) o;
        return Objects.equals(fitness, candidate1.fitness) &&
                Objects.equals(watchFace, candidate1.watchFace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fitness, watchFace);
    }
}
