package timepiece.watchface;

import java.util.Objects;

public class Candidate implements Comparable<Candidate> {
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
        return String.format("%10d: splits: %d, check ok: %d, nok: %d, times ok: %d, nok %d, %s",
                fitness.getFitness(),
                fitness.getSplitPos(),
                fitness.getCheckedOK(),
                fitness.getCheckedNOK(),
                fitness.getCheckedTimesOK(),
                fitness.getCheckedTimesNOK(),
                getCandidate());
    }

    Fitness getFitnessResult() {
        return fitness;
    }

    void setFitnessResult(Fitness fitness) {
        this.fitness = fitness;
    }

    int getFitness() {
        return fitness.getFitness();
    }

    void setFitness(int fitness) {
        this.fitness.setFitness(fitness);
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    Candidate mixWith(Candidate other, int atPosition) {
        Candidate res = new Candidate();
        res.setCandidate(this.getCandidate().substring(0, atPosition) + other.getCandidate().substring(atPosition));
        return res;
    }

    Candidate changeChar(char character, int atPosition) {
        Candidate res = new Candidate();

        char[] cand = getCandidate().toCharArray();

        int pos = atPosition;
        while (cand[pos] == '|') {
            pos++;
        }

        cand[pos] = character;

        res.setCandidate(new String(cand));
        return res;
    }

    @Override
    public int compareTo(Candidate o) {
        return Integer.compare(
                o.getFitness(),
                getFitness());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate1 = (Candidate) o;
        return Objects.equals(fitness, candidate1.fitness) &&
                Objects.equals(candidate, candidate1.candidate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fitness, candidate);
    }
}
