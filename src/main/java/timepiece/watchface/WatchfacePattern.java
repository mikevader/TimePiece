package timepiece.watchface;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class WatchfacePattern {
    private List<Pattern>[][] patterns = null;
    private List<Pattern> wordPatterns = null;
    private HashSet<String> inclWords = new HashSet<>();
    private char[] includedChar;
    private Random rand = new Random();


    public WatchfacePattern(List<Pattern>[][] patterns, List<Pattern> wordPatterns, HashSet<String> inclWords, char[] includedChar) {
        this.patterns = patterns;
        this.wordPatterns = wordPatterns;
        this.inclWords = inclWords;
        this.includedChar = includedChar;
    }

    public List<Pattern>[][] getPatterns() {
        return patterns;
    }

    public List<Pattern> getWordPatterns() {
        return wordPatterns;
    }

    public Set<String> getInclWords() {
        return inclWords;
    }

    public char[] getIncludedChar() {
        return includedChar;
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

    char getRandomChar() {
        int pos = getRand().nextInt(getIncludedChar().length);
        return getIncludedChar()[pos];
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

    public Random getRand() {
        return rand;
    }

    void setRand(Random rand) {
        this.rand = rand;
    }
}
