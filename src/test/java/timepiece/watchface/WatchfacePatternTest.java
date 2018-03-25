package timepiece.watchface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import timepiece.TimeNamesEnglish;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WatchfacePatternTest {

    private WatchfacePattern pattern;
    private Random random;

    @BeforeEach
    void setUp() {
        PatternGenerator patternGenerator = new PatternGenerator();
        random = mock(Random.class);
        pattern = patternGenerator.createPatterns(TimeNamesEnglish.getTimeStrings());
        pattern.setRand(random);
    }

    @Test
    void getRandomChar() {
        char[] includedChars = new char[]{
                'a', 'e', 'f', 'g', 'h', 'i', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'y'
        };
        when(random.nextInt(pattern.getIncludedChar().length)).thenReturn(1);
        char randomChar = pattern.getRandomChar();

        assertEquals('e', randomChar);
    }

    @Test
    void createRandom() {
        when(random.nextInt(pattern.getIncludedChar().length)).thenReturn(1); // for getRandomChar()
        when(random.nextInt(pattern.getInclWords().size())).thenReturn(2); // for addRandomWord()
        Candidate candidate = pattern.createRandom();

        String expCandiate = "halfeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee";
        assertEquals(expCandiate, candidate.getCandidate());
    }

    @Test
    void addRandomWord() {
        Candidate source = new Candidate();
        when(random.nextInt(pattern.getInclWords().size())).thenReturn(1); // random word
        when(random.nextInt(source.getCandidate().length())).thenReturn(2); // random location
        Candidate newCandidate = pattern.addRandomWord(source);

        String expCandidate = "XXnineXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";
        assertEquals(expCandidate, newCandidate.getCandidate());
    }
}