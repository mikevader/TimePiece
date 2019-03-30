package timepiece.watchface;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

class WatchfacePattern {
    private List<Pattern>[][] patterns = null;
    private List<Pattern> wordPatterns = null;
    private HashSet<String> inclWords = new HashSet<>();
    private char[] includedChar;
    private Random rand = new Random();


    WatchfacePattern(List<Pattern>[][] patterns, List<Pattern> wordPatterns, HashSet<String> inclWords, char[] includedChar) {
        this.patterns = patterns;
        this.wordPatterns = wordPatterns;
        this.inclWords = inclWords;
        this.includedChar = includedChar;
    }

    List<Pattern>[][] getPatterns() {
        return patterns;
    }

    List<Pattern> getWordPatterns() {
        return wordPatterns;
    }

    Set<String> getInclWords() {
        return inclWords;
    }

    char[] getIncludedChar() {
        return includedChar;
    }

    Candidate createRandom() {
        Candidate cand = new Candidate();
        char[] c = cand.getWatchFace().toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '|') {
                c[i] = getRandomChar();
            }
        }
        cand.setWatchFace(new String(c));
        for (int j = 0; j < 100; j++) {
            cand = addRandomWord(cand);
        }
        return cand;
    }

    char getRandomChar() {
        int pos = getRand().nextInt(getIncludedChar().length);
        return getIncludedChar()[pos];
    }

    Candidate addRandomWord(Candidate source) {
        Candidate res = new Candidate();

        Object[] words = getInclWords().toArray();
        char[] word = words[getRand().nextInt(words.length)].toString().toCharArray();
        char[] cand = source.getWatchFace().toCharArray();

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

        res.setWatchFace(new String(cand));
        return res;
    }

    private Random getRand() {
        return rand;
    }

    void setRand(Random rand) {
        this.rand = rand;
    }
}
