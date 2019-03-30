package timepiece.watchface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import timepiece.TimeNamesEnglish;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
        this.watchfacePattern = patternGenerator.createPatterns(TimeNamesEnglish.getTimeStrings2());

        Path solutionFile = Paths.get("solution.xml");
        if (Files.exists(solutionFile)) {
            log.info("loading solution");
            loadSolution(solutionFile);
        } else {
            log.info("create empty solution");
            setSolution(createNewSolution());
        }

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
                Thread.currentThread().interrupt();
            }
        }
    }

    void incGeneration() {
        synchronized (getSolution()) {
            getSolution().setGeneration(getSolution().getGeneration() + 1);

            if (getSolution().getGeneration() % 1000 == 0) {
                log.info(String.format("best:  %4d: %s\n", getSolution().getGeneration(), getSolution().getFittest().toString()));
            }

            if (getSolution().getGeneration() % 10000 == 0) {
                log.info("saving solution");
                saveSolution();

                try (FileWriter out = new FileWriter("fittnes.txt", true)) {
                    out.write("" + getSolution().getGeneration());
                    out.write("," + getSolution().getFittest().getFitnessScore());
                    out.write("," + getSolution().getFittest().getFitnessResult().getCheckedTimesNOK());
                    out.write("\n");
                } catch (IOException e) {
                    log.error("Couldn't write to fitness.txt file", e);
                }
            }
        }
    }

    boolean doAdd(Candidate candidate) {
        int max = getSolution().getFittest().getFitnessScore();
        int min = getSolution().getWorst().getFitnessScore();
        if (max == min) return true;
        if (candidate.getFitnessScore() < min) return getRand().nextInt(100) < 10;

        double prob = (candidate.getFitnessScore() - min) / (double)(max - min) + 0.1;
        return getRand().nextDouble() < prob;
    }

    Candidate getRandom(boolean good) {
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

    private Solution createNewSolution() {
        Solution newSolution = new Solution();

        for (int i = 0; i < POPULATION_SIZE; i++) {

            if (i % 10 == 0) log.info(".");
            Candidate c = watchfacePattern.createRandom();
            calcFitness(c);
            newSolution.getCandidates().add(c);
        }

        return newSolution;
    }

    void loadSolution(Path solutionFile) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Solution.class);
            Unmarshaller um = jc.createUnmarshaller();
            this.setSolution((Solution) um.unmarshal(solutionFile.toFile()));
            for (int i = getSolution().getCandidates().size(); i < POPULATION_SIZE; i++) {
                this.getSolution().getCandidates().add(watchfacePattern.createRandom());
            }
            for (Candidate cand : getSolution().getCandidates()) {
                calcFitness(cand);
            }
        } catch (Exception e) {
            log.error("solution not readable", e);
        }
    }

    private void saveSolution() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Solution.class);
            Marshaller ma = jc.createMarshaller();
            ma.marshal(this.getSolution(), new File("solution.xml"));
        } catch (JAXBException e) {
            log.error("Solution could not be serialized", e);
        }
    }

    void addToSolution(Candidate candidate) {
        synchronized (getSolution()) {
            if (this.getSolution().getCandidates().size() == POPULATION_SIZE) this.removeRandom();
            this.getSolution().getCandidates().add(candidate);
        }
    }

    private void removeRandom() {
        this.getSolution().getCandidates().remove(getRandom(false));
    }

    Candidate mixTogether(Candidate left, Candidate right) {
        int pos = getRand().nextInt(left.getWatchFace().length());
        return left.mixWith(right, pos);
    }

    Candidate changeRandom(Candidate source) {
        int pos = getRand().nextInt(source.getWatchFace().length());
        char randomChar = getWatchfacePattern().getRandomChar();

        return source.changeChar(randomChar, pos);
    }

    void calcFitness(Candidate candidate) {
        fitnessCalculator.calculate(
                candidate,
                watchfacePattern);
    }

    Solution getSolution() {
        return solution;
    }

    void setSolution(Solution solution) {
        this.solution = solution;
    }

    Random getRand() {
        return rand;
    }

    void setRand(Random rand) {
        this.rand = rand;
    }

    WatchfacePattern getWatchfacePattern() {
        return this.watchfacePattern;
    }

    void setWatchfacePattern(WatchfacePattern watchfacePattern) {
        this.watchfacePattern = watchfacePattern;
    }
}
