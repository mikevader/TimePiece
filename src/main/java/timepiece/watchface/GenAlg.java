package timepiece.watchface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import timepiece.TimeNamesEnglish;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GenAlg {

    private Logger log = LogManager.getLogger(GenAlg.class);

    private static final int POPULATION_SIZE = 1000;

    private final PatternGenerator patternGenerator = new PatternGenerator();
    private final FitnessCalculator fitnessCalculator = new FitnessCalculator();
    private WatchfacePattern watchfacePattern;
    private Solution solution = null;
    private Random rand = new Random();

    public static void main(String[] args) {
        GenAlg gen = new GenAlg();
        gen.run();
    }

    private void run() {

        log.info("creating patterns");
        this.watchfacePattern = patternGenerator.createPatterns(TimeNamesEnglish.getTimeStrings());

        log.info("loading solution");
        loadSolution();

        List<GenThread> threads = new LinkedList<>();
        for (int i = 0; i < 25; i++) {
            GenThread gt = new GenThread(this);
            gt.start();
            threads.add(gt);
        }

        for (GenThread genThread : threads) {
            try {
                genThread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void incGeneration() {
        synchronized (getSolution()) {
            getSolution().setGeneration(getSolution().getGeneration() + 1);

            if (getSolution().getGeneration() % 1000 == 0) {
                log.info(String.format("best:  %4d: %s\n", getSolution().getGeneration(), getSolution().getFittest().toString()));
            }

            if (getSolution().getGeneration() % 10000 == 0) {
                log.info("saving solution");
                saveSolution();

                try {
                    FileWriter out = new FileWriter("fittnes.txt", true);
                    out.write("" + getSolution().getGeneration());
                    out.write("," + getSolution().getFittest().getFitness());
                    out.write("," + getSolution().getFittest().getFitnessResult().getCheckedTimesNOK());
                    out.write("\n");
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public boolean doAdd(Candidate candidate) {
        int max = getSolution().getFittest().getFitness();
        int min = getSolution().getWorst().getFitness();
        if (max == min) return true;
        if (candidate.getFitness() < min) return getRand().nextInt(100) < 10;

        double prob = (candidate.getFitness() - min) / (max - min) + 0.1;
        return getRand().nextDouble() < prob;
    }

    public Candidate getRandom(boolean good) {
        synchronized (getSolution()) {
            int r;
            do {
                r = (int) (Math.abs(getRand().nextGaussian() * 0.5) * getSolution().getCandidates().size());
                if (!good) r = getSolution().getCandidates().size() - r + 1;
            } while (r < 0 || r >= getSolution().getCandidates().size());
            if (!good && r == 0) r++;
            return new ArrayList<>(getSolution().getCandidates()).get(r);
        }
    }

    public Candidate createRandom() {
        Candidate cand = new Candidate();
        char[] c = cand.getCandidate().toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '|') {
                c[i] = getRandomChar();
            }
        }
        cand.setCandidate(new String(c));
        for (int j = 0; j < 100; j++) {
            cand = addRandomWord(cand);
        }
        return cand;
    }

    void loadSolution() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Solution.class);
            Unmarshaller um = jc.createUnmarshaller();
            this.setSolution((Solution) um.unmarshal(new File("solution.xml")));
            for (int i = getSolution().getCandidates().size(); i < POPULATION_SIZE; i++) {
                this.getSolution().getCandidates().add(createRandom());
            }
            for (Candidate cand : getSolution().getCandidates()) {
                calcFitness(cand);
            }
        } catch (Exception e) {
            log.error("solution not found", e);
            this.setSolution(new Solution());

            for (int i = 0; i < POPULATION_SIZE; i++) {

                if (i % 10 == 0) System.out.print(".");
                Candidate c = createRandom();
                if (i < 0 /*POPULATION_SIZE / 2*/) {
//					c.candidate = "fünffzehnX|dreiXnachX|vorelfhalb|uhrviertel|siebenacht|dreisechsX|neundzwölf|zweinsuhrX|elfünfzehn|vierXXXXXX";
//					c.candidate = "fünffzehnu|dreihnacht|vorelfhalb|uhrviertel|siebenacht|dreisechsz|neundzwölf|zweinsluhr|elfünfzehn|undvierzig";

                    //11x11
                    //c.candidate = "XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";

                    //10x10
                    //c.candidate = "XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX";

                    //circle
                    c.setCandidate("ONER|SETHIRTY|FIFTYPTYHALF|FTENETWENTYR|FIVENTPASTNINE|TIELEVENTHREEH|XSIXTFIFTEENTWOEN|TWELVEEIGHTSEVENR|YWEQUARTERFOURTYE|FOFIVENTCLOCKTENY|NINEEPASTENTOU|FIFTEENFOURTHY|TWOHFIFTYEWR|THIRTYTWENTY|FIVENONE|YTEN");
                }
                calcFitness(c);
                this.getSolution().getCandidates().add(c);
            }
            System.out.println();
        }
    }

    private void saveSolution() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Solution.class);
            Marshaller ma = jc.createMarshaller();
            ma.marshal(this.getSolution(), new File("solution.xml"));
        } catch (Exception ignored) {
        }
    }

    public void addToSolution(Candidate candidate) {
        synchronized (getSolution()) {
            if (this.getSolution().getCandidates().size() == POPULATION_SIZE) this.removeRandom();
            this.getSolution().getCandidates().add(candidate);
        }
    }

    private void removeRandom() {
        this.getSolution().getCandidates().remove(getRandom(false));
    }

    public Candidate mixTogether(Candidate left, Candidate right) {
        Candidate res = new Candidate();
        int pos = getRand().nextInt(left.getCandidate().length());
        res.setCandidate(left.getCandidate().substring(0, pos) + right.getCandidate().substring(pos));
        return res;
    }

    public Candidate changeRandom(Candidate source) {
        Candidate res = new Candidate();

        char[] cand = source.getCandidate().toCharArray();

        int pos;
        do {
            pos = getRand().nextInt(cand.length);
        } while (cand[pos] == '|');

        cand[pos] = getRandomChar();

        res.setCandidate(new String(cand));
        return res;
    }

    public Candidate addRandomWord(Candidate source) {
        Candidate res = new Candidate();

        Object[] words = watchfacePattern.getInclWords().toArray();
        char[] word = words[getRand().nextInt(words.length)].toString().toCharArray();
        char[] cand = source.getCandidate().toCharArray();

        int pos;
        boolean possible;
        do {
            possible = true;
            pos = getRand().nextInt(cand.length);
            for (int i = pos; i < pos + word.length && possible; i++) {
                if (i >= cand.length) {
                    possible = false;
                } else if (cand[i] == '|') {
                    possible = false;
                }
            }
        } while (!possible);

        System.arraycopy(word, 0, cand, pos, word.length);

        res.setCandidate(new String(cand));
        return res;
    }

    char getRandomChar() {
        int pos = getRand().nextInt(watchfacePattern.getIncludedChar().length);
        return watchfacePattern.getIncludedChar()[pos];
    }

    public void calcFitness(Candidate candidate) {
        Fitness fitness = fitnessCalculator.calculate(
                candidate,
                watchfacePattern);
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public WatchfacePattern getWatchfacePattern() {
        return this.watchfacePattern;
    }

    public void setWatchfacePattern(WatchfacePattern watchfacePattern) {
        this.watchfacePattern = watchfacePattern;
    }
}
