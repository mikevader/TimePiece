package timepiece.watchface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import timepiece.TimeNamesEnglish;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PatternGeneratorTest {

    PatternGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new PatternGenerator();
    }

    @Test
    void createPatterns() {
        List[][] hello = {
                {
                        Arrays.asList("eis", "genau eis"),
                        Arrays.asList("föif ab eis")
                },
                {
                        Arrays.asList("zwöi", "genau zwöi"),
                        Arrays.asList("föif ab zwöi")
                }
        };

        WatchfacePattern patterns = generator.createPatterns(hello);

        assertNotNull(patterns);
        assertEquals(2, patterns.getPatterns().length);
        assertEquals(5, patterns.getWordPatterns().size());
        assertEquals(5, patterns.getInclWords().size());
        assertEquals(12, patterns.getIncludedChar().length);

        char[] expChars = {'a', 'b', 'e', 'f', 'g', 'i', 'n', 's', 'u', 'w', 'z', 'ö'};
        assertArrayEquals(expChars, patterns.getIncludedChar());

        Set<String> expWords = new HashSet(Arrays.asList("ab", "eis", "föif", "genau", "zwöi"));
        assertEquals(expWords, patterns.getInclWords());

        List<String> expWordPatterns = Arrays.asList(".*ab.*", ".*zwöi.*", ".*eis.*", ".*genau.*", ".*föif.*");
        assertEquals(expWordPatterns, convertToStringPatterns(patterns.getWordPatterns()));

        List[][] expPatterns = {
                {
                        asLinkedList(Pattern.compile("(.*)(eis)(.*)"), Pattern.compile("(.*)(genau)(.+)(eis)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(föif)(.+)(ab)(.+)(eis)(.*)")),
                },
                {
                        asLinkedList(Pattern.compile("(.*)(zwöi)(.*)"), Pattern.compile("(.*)(genau)(.+)(zwöi)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(föif)(.+)(ab)(.+)(zwöi)(.*)")),
                }
        };

        List<String>[][] expPatternsString = convertToStringPatterns(expPatterns);
        List<String>[][] actPatternsString = convertToStringPatterns(patterns.getPatterns());

        assertArrayEquals(expPatternsString, actPatternsString);
    }

    @Test
    void createPatterns_fullEnglishTimeNames() {
        WatchfacePattern watchfacePattern = generator.createPatterns(TimeNamesEnglish.getTimeStrings());


        HashSet<String> expInclWords = new HashSet(Arrays.asList("fifteen", "nine", "half", "six", "past", "fifty", "one", "seven", "two", "three", "o", "eight", "times", "four", "twelve", "eleven", "to", "ten", "five", "quarter", "twenty", "thirty", "forty"));
        assertIterableEquals(expInclWords, watchfacePattern.getInclWords());

        List[][] expPatterns = {
                {
                        asLinkedList(Pattern.compile("(.*)(one)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(one)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(one)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(two)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(one)(.*)"), Pattern.compile("(.*)(one)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(two)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(two)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(two)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(three)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(two)(.*)"), Pattern.compile("(.*)(two)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(three)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(three)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(three)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(four)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(three)(.*)"), Pattern.compile("(.*)(three)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(four)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(four)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(four)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(four)(.*)"), Pattern.compile("(.*)(four)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(six)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(five)(.*)"), Pattern.compile("(.*)(five)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(six)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(six)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(six)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(seven)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(six)(.*)"), Pattern.compile("(.*)(six)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(seven)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(seven)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(seven)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(eight)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(seven)(.*)"), Pattern.compile("(.*)(seven)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(eight)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(eight)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(eight)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(nine)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(eight)(.*)"), Pattern.compile("(.*)(eight)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(nine)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(nine)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(nine)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(nine)(.*)"), Pattern.compile("(.*)(nine)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(eleven)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(ten)(.*)"), Pattern.compile("(.*)(ten)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(eleven)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(eleven)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(eleven)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(twelve)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(eleven)(.*)"), Pattern.compile("(.*)(eleven)(.+)(fifty)(.*)(five)(.*)"))
                },
                {
                        asLinkedList(Pattern.compile("(.*)(twelve)(.+)(o)(.+)(times)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(five)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(o)(.+)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(ten)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(ten)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifteen)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(fifteen)(.*)"), Pattern.compile("(.*)(quarter)(.+)(past)(.+)(twelve)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(twenty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(twenty)(.*)(five)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(twenty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(thirty)(.*)"), Pattern.compile("(.*)(half)(.+)(past)(.+)(twelve)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(thirty)(.*)(five)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(thirty)(.*)(five)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(forty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(forty)(.*)(five)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(forty)(.*)(five)(.*)"), Pattern.compile("(.*)(quarter)(.+)(to)(.+)(one)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(fifty)(.*)")),
                        asLinkedList(Pattern.compile("(.*)(fifty)(.*)(five)(.+)(past)(.+)(twelve)(.*)"), Pattern.compile("(.*)(twelve)(.+)(fifty)(.*)(five)(.*)"))
                }
        };

        List<String>[][] expPatternsString = convertToStringPatterns(expPatterns);
        List<String>[][] actPatternsString = convertToStringPatterns(watchfacePattern.getPatterns());

        assertArrayEquals(expPatternsString, actPatternsString);
        //assertEquals(expPatterns, genAlg.patterns);

        char[] expIncludedChar = {'a', 'e', 'f', 'g', 'h', 'i', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y'};
        assertArrayEquals(expIncludedChar, watchfacePattern.getIncludedChar());

        List<String> expWordPatterns = Arrays.asList(".*fifteen.*", ".*nine.*", ".*half.*", ".*six.*", ".*past.*", ".*fifty.*", ".*one.*", ".*seven.*", ".*two.*", ".*three.*", ".*o.*", ".*eight.*", ".*times.*", ".*four.*", ".*twelve.*", ".*eleven.*", ".*to.*", ".*ten.*", ".*five.*", ".*quarter.*", ".*twenty.*", ".*thirty.*", ".*forty.*");
        assertEquals(expWordPatterns, convertToStringPatterns(watchfacePattern.getWordPatterns()));
    }

    private List<String>[][] convertToStringPatterns(List<Pattern>[][] expPatterns) {
        List<String>[][] newPatterns = new List[12][12];

        for (int i = 0; i < expPatterns.length; i++) {

            for (int j = 0; j < expPatterns[i].length; j++) {
                newPatterns[i][j] = convertToStringPatterns(expPatterns[i][j]);
            }

        }
        return newPatterns;
    }

    private List<String> convertToStringPatterns(List<Pattern> patternList) {
        return patternList.stream().map(Pattern::pattern).collect(Collectors.toList());
    }

    @SafeVarargs
    private final <T> List<T> asLinkedList(T... elements) {
        LinkedList<T> linkedList = new LinkedList<>();

        for (T element : elements) {
            linkedList.add(element);
        }

        return linkedList;
    }

}