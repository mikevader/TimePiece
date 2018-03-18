package timepiece.watchface;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class WatchfacePattern {
    private List<Pattern>[][] patterns = null;
    private List<Pattern> wordPatterns = null;
    private HashSet<String> inclWords = new HashSet<>();
    private char[] includedChar;


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

    public HashSet<String> getInclWords() {
        return inclWords;
    }

    public char[] getIncludedChar() {
        return includedChar;
    }
}
