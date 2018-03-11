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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenAlg {

    private Logger log = LogManager.getLogger(GenAlg.class);

    private static final int POPULATION_SIZE = 1000;
    private static final int NONE_FOUND_PENALTY = -10000;
    private static final int NOT_MATCHED_PENALTY = -10;
    private static final int MATCH_BONUS = 1000;
    private static final int WORD_MATCH_BONUS = 10;
    public static final int COST_OF_THIS_ALGORITHM_BECOMING_SKYNET = 999999999;
    private static final int LOW_SPLIT_BONUS = 100;
    private static final int VARIANCE_PENALTY = 1;

    private char[] includedChar = null;
    private Solution solution = null;
    private Random rand = new Random();
    private List<Pattern>[][] patterns = null;
    private List<Pattern> wordPatterns = null;
    private HashSet<String> inclWords = new HashSet<>();

    public static void main(String[] args) {
        GenAlg gen = new GenAlg();
        gen.run();
    }

    private void run() {

        log.info("creating patterns");
        createPatterns(TimeNamesEnglish.getTimeStrings());

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
//				System.out.printf("worst: %4d: %s\n",solution.generation,solution.worst.toString());
            }

            if (getSolution().getGeneration() % 10000 == 0) {
                log.info("saving solution");
                saveSolution();

                try {
                    FileWriter out = new FileWriter("fittnes.txt", true);
                    out.write("" + getSolution().getGeneration());
                    out.write("," + getSolution().getFittest().getFittnes());
                    out.write("," + getSolution().getFittest().getCheckedTimesNOK());
                    out.write("\n");
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public boolean doAdd(Candidate candidate) {
        int max = getSolution().getFittest().getFittnes();
        int min = getSolution().getWorst().getFittnes();
        if (max == min) return true;
        if (candidate.getFittnes() < min) return getRand().nextInt(100) < 10;

        double prob = (candidate.getFittnes() - min) / (max - min) + 0.1;
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
            return getSolution().getCandidates().get(r);
        }
    }

    @SuppressWarnings("unchecked")
    void createPatterns(List<String>[][] strings) {
        HashSet<Character> inclChar = new HashSet<>();

        setPatterns(new List[strings.length][]);

        for (int hour = 0; hour < strings.length; hour++) {
            getPatterns()[hour] = new List[strings[hour].length];
            for (int minute = 0; minute < strings[hour].length; minute++) {
                getPatterns()[hour][minute] = new LinkedList<>();
                for (String time : strings[hour][minute]) {

                    StringBuilder regex = new StringBuilder();
                    String[] words = time.split(" ");
                    for (String word : words) {

                        if (regex.length() == 0) {
                            regex.append("(.*)");
                        } else {
                            regex.append("(.+)");
                        }

                        StringBuilder subRegex = new StringBuilder();
                        String[] subWords = word.split("\\+");
                        for (String string : subWords) {
                            if (subRegex.length() > 0) {
                                subRegex.append("(.*)");
                            }
                            subRegex.append("(");
                            subRegex.append(string);
                            subRegex.append(")");
                            getInclWords().add(string);

                            char[] chars = string.toCharArray();
                            for (char c : chars) {
                                inclChar.add(c);
                            }
                        }

                        regex.append(subRegex.toString());

                    }
                    regex.append("(.*)");
                    getPatterns()[hour][minute].add(Pattern.compile(regex.toString()));
                    //System.out.printf("%02d:%02d : %s\n", hour + 1, minute * 5, regex.toString());
                    //System.out.printf("\t/%s/,\n", regex.toString());
                }
            }
        }

        StringBuilder charSet = new StringBuilder();
        for (Character character : inclChar) {
            if (character != ' ') charSet.append(character);
        }
        setIncludedChar(charSet.toString().toCharArray());
        //System.out.println(charSet.toString());

        setWordPatterns(new LinkedList<>());
        for (String string : getInclWords()) {
            getWordPatterns().add(Pattern.compile(".*" + string + ".*"));
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
                calcFittness(cand);
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
                calcFittness(c);
                this.getSolution().getCandidates().add(c);
            }
            System.out.println();
        }
        sortSolution();
        this.getSolution().setFittest(this.getSolution().getCandidates().get(0));
        this.getSolution().setWorst(this.getSolution().getCandidates().get(this.getSolution().getCandidates().size() - 1));
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
            if (candidate.getFittnes() >= this.getSolution().getFittest().getFittnes()) this.getSolution().setFittest(candidate);
            if (candidate.getFittnes() <= this.getSolution().getWorst().getFittnes()) this.getSolution().setWorst(candidate);
            this.sortSolution();
        }
    }

    private void removeRandom() {
        this.getSolution().getCandidates().remove(getRandom(false));
    }

    private void sortSolution() {
        this.getSolution().getCandidates().sort((o1, o2) -> {
            Integer i = o1.getCheckedTimesNOK();
            return i.compareTo(o2.getCheckedTimesNOK());
        });
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

        Object[] words = getInclWords().toArray();
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
        int pos = getRand().nextInt(getIncludedChar().length);
        return getIncludedChar()[pos];
    }

    public void calcFittness(Candidate candidate) {
        int fittness = 0;
        int checkedOK = 0;
        int checkedNOK = 0;
        int checkedTimesOK = 0;
        int checkedTimesNOK = 0;

        HashSet<Integer> splitPos = new HashSet<>();
        for (List<Pattern>[] pattern : getPatterns()) {
            for (List<Pattern> aPattern : pattern) {
                boolean oneFound = false;
                for (Pattern timeRegEx : aPattern) {
                    Matcher m = timeRegEx.matcher(candidate.getCandidate());
                    if (m.matches()) {
                        oneFound = true;
                        fittness += MATCH_BONUS;
                        checkedOK++;
                        for (int i = 1; i <= m.groupCount(); i++) {
                            splitPos.add(m.start(i));
                        }
                    } else {
                        fittness += NOT_MATCHED_PENALTY;
                        checkedNOK++;
                    }
                }
                if (!oneFound) {
                    fittness += NONE_FOUND_PENALTY;
                    checkedTimesNOK++;
                } else {
                    checkedTimesOK++;
                }
            }
        }

        for (Pattern word : getWordPatterns()) {
            Matcher m = word.matcher(candidate.getCandidate());
            if (m.matches()) {
                fittness += WORD_MATCH_BONUS;
            }
        }

        Integer lastPos = null;
        double avgLen = 0;
        for (Integer pos : splitPos) {
            if (lastPos != null) {
                avgLen += pos - lastPos;
            }
            lastPos = pos;
        }
        avgLen /= (splitPos.size() - 1);

        double variance = 0;
        lastPos = null;
        for (Integer pos : splitPos) {
            if (lastPos != null) {
                variance += Math.pow(avgLen - (pos - lastPos), 2);
            }
            lastPos = pos;
        }
        variance = Math.sqrt(variance);

        candidate.setFittnes(fittness);
        candidate.setSplitPos(splitPos.size());
        candidate.setVariance(variance);
        candidate.setAvgLen(avgLen);
        candidate.setCheckedOK(checkedOK);
        candidate.setCheckedNOK(checkedNOK);
        candidate.setCheckedTimesOK(checkedTimesOK);
        candidate.setCheckedTimesNOK(checkedTimesNOK);

        if (checkedNOK == 0) {
            candidate.setFittnes(candidate.getFittnes() + (100 - candidate.getSplitPos()) * LOW_SPLIT_BONUS);
            candidate.setFittnes((int)(candidate.getFittnes() - variance * VARIANCE_PENALTY));
        }
    }

    public char[] getIncludedChar() {
        return includedChar;
    }

    public void setIncludedChar(char[] includedChar) {
        this.includedChar = includedChar;
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

    public List<Pattern>[][] getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern>[][] patterns) {
        this.patterns = patterns;
    }

    public List<Pattern> getWordPatterns() {
        return wordPatterns;
    }

    public void setWordPatterns(List<Pattern> wordPatterns) {
        this.wordPatterns = wordPatterns;
    }

    public HashSet<String> getInclWords() {
        return inclWords;
    }

    public void setInclWords(HashSet<String> inclWords) {
        this.inclWords = inclWords;
    }
}
