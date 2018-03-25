package timepiece.watchface;

public class GenThread extends Thread {

    private GenAlg genAlg;

    GenThread(GenAlg genAlg) {
        this.genAlg = genAlg;
    }

    @Override
    public void run() {
        while (true) {
            genAlg.incGeneration();

            Candidate newCandidate;
            int action = genAlg.getRand().nextInt(16);

            if (action < 2) {
                newCandidate = genAlg.mixTogether(genAlg.getRandom(true), genAlg.getRandom(true));
            } else if (action < 10) {
                newCandidate = genAlg.getWatchfacePattern().addRandomWord(genAlg.getRandom(true));
            } else if (action < 11) {
                newCandidate = genAlg.changeRandom(genAlg.getRandom(true));
            } else {
                newCandidate = genAlg.getWatchfacePattern().createRandom();
            }

            genAlg.calcFitness(newCandidate);

            if (genAlg.doAdd(newCandidate)) {
                genAlg.addToSolution(newCandidate);
            }

        }
    }
}
