package timepiece.watchface;

public class Fitness {
    int fitness;
    int checkedOK = 0;
    int checkedNOK = 0;
    int checkedTimesOK = 0;
    int checkedTimesNOK = 0;
    int splitPos = 0;
    double variance = 0;
    double avgLen = 0;

    public Fitness() {
        this.fitness = Integer.MIN_VALUE;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
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