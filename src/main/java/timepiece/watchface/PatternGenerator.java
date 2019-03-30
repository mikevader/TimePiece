package timepiece.watchface;

import timepiece.TimeNamesEnglish;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatternGenerator {

    public WatchfacePattern createPatterns(List<String>[][] times) {
        List<Pattern>[][] patterns = new List[times.length][];
        List<Pattern> wordPatterns = new LinkedList<>();
        HashSet<String> inclWords = new HashSet<>();
        char[] includedChar;

        HashSet<Character> inclChar = new HashSet<>();

        for (int hour = 0; hour < times.length; hour++) {
            patterns[hour] = new List[times[hour].length];
            for (int minute = 0; minute < times[hour].length; minute++) {
                patterns[hour][minute] = new LinkedList<>();
                for (String time : times[hour][minute]) {

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
                            inclWords.add(string);

                            char[] chars = string.toCharArray();
                            for (char c : chars) {
                                inclChar.add(c);
                            }
                        }

                        regex.append(subRegex.toString());

                    }
                    regex.append("(.*)");
                    patterns[hour][minute].add(Pattern.compile(regex.toString()));
                }
            }
        }

        StringBuilder charSet = new StringBuilder();
        for (Character character : inclChar) {
            if (character != ' ') charSet.append(character);
        }
        includedChar = charSet.toString().toCharArray();
        Arrays.sort(includedChar);

        for (String string : inclWords) {
            wordPatterns.add(Pattern.compile(".*" + string + ".*"));
        }

        return new WatchfacePattern(patterns, wordPatterns, inclWords, includedChar);
    }


    public static void main(String[] args) {
        WatchfacePattern watchfacePattern = new PatternGenerator().createPatterns(TimeNamesEnglish.getTimeStrings2());
        List<Pattern>[][] patterns = watchfacePattern.getPatterns();

        System.out.println("times = [");
        for (int i = 0; i < patterns.length; i++) {
            System.out.println("    [");
            for (int j = 0; j < patterns[i].length; j++) {
                System.out.println("        [\"" + String.join("\", \"", patterns[i][j].stream().map(Pattern::toString).collect(Collectors.toList())) + "\"],");
            }
            System.out.println("    ],");
        }
        System.out.println("];");

        System.out.println("names = [\"" + String.join("\", \"", watchfacePattern.getInclWords()) + "\"];");

        for (int x = 0; x < watchfacePattern.getIncludedChar().length; x++) {
            System.out.println(watchfacePattern.getIncludedChar()[x]);
        }
    }
}
