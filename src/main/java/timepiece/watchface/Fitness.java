package timepiece.watchface;

import java.util.Objects;

public class Fitness {
    private int fitness;
    private int checkedOK = 0;
    private int checkedNOK = 0;
    private int checkedTimesOK = 0;
    private int checkedTimesNOK = 0;
    private int splitPos = 0;
    private double variance = 0;
    private double avgLen = 0;

    private Fitness(int fitness, int checkedOK, int checkedNOK, int checkedTimesOK, int checkedTimesNOK, int splitPos, double variance, double avgLen) {
        this.fitness = fitness;
        this.checkedOK = checkedOK;
        this.checkedNOK = checkedNOK;
        this.checkedTimesOK = checkedTimesOK;
        this.checkedTimesNOK = checkedTimesNOK;
        this.splitPos = splitPos;
        this.variance = variance;
        this.avgLen = avgLen;
    }

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

    public int getCheckedNOK() {
        return checkedNOK;
    }

    public int getCheckedTimesOK() {
        return checkedTimesOK;
    }

    public int getCheckedTimesNOK() {
        return checkedTimesNOK;
    }

    public int getSplitPos() {
        return splitPos;
    }

    public double getVariance() {
        return variance;
    }

    public double getAvgLen() {
        return avgLen;
    }

    public static Fitness createFitness(int fitness, int checkedOK, int checkedNOK, int checkedTimesOK, int checkedTimesNOK, int splitPos, double variance, double avgLen) {
        return new Fitness(fitness, checkedOK, checkedNOK, checkedTimesOK, checkedTimesNOK, splitPos, variance, avgLen);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fitness fitness1 = (Fitness) o;
        return fitness == fitness1.fitness &&
                checkedOK == fitness1.checkedOK &&
                checkedNOK == fitness1.checkedNOK &&
                checkedTimesOK == fitness1.checkedTimesOK &&
                checkedTimesNOK == fitness1.checkedTimesNOK &&
                splitPos == fitness1.splitPos &&
                Double.compare(fitness1.variance, variance) == 0 &&
                Double.compare(fitness1.avgLen, avgLen) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(fitness, checkedOK, checkedNOK, checkedTimesOK, checkedTimesNOK, splitPos, variance, avgLen);
    }
}