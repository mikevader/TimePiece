package timepiece.watchface;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import timepiece.TimeNamesGerman;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FitnessCalculatorTest {

    private static List<Pattern>[][] patterns;
    private static List<Pattern> wordPatterns;

    private FitnessCalculator fitnessCalculator;

    @BeforeAll
    static void beforeAll() {
        GenAlg genAlg = new GenAlg();
        genAlg.createPatterns(TimeNamesGerman.getTimeStrings());

        patterns = genAlg.getPatterns();
        wordPatterns = genAlg.getWordPatterns();
    }

    @BeforeEach
    void setUp() {
        fitnessCalculator = new FitnessCalculator();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculateDefault() {
        Candidate candidate = new Candidate();
        candidate.setCandidate(
                "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX");

        Fitness result = fitnessCalculator.calculate(candidate, patterns, wordPatterns);

        assertNotNull(result);
        assertEquals(-1443120, result.getFitness());
        assertEquals(0, result.getCheckedTimesOK());
        assertEquals(144, result.getCheckedTimesNOK());
        assertEquals(0, result.getCheckedOK());
        assertEquals(312, result.getCheckedNOK());
        assertEquals(0, result.getSplitPos());
        assertEquals(-0.0, result.getAvgLen());
        assertEquals(0.0, result.getVariance());
    }

    @Test
    void calculateOneTimeOk() {
        Candidate candidate = new Candidate();
        candidate.setCandidate(
                "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXeinXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXuhrXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX");

        Fitness result = fitnessCalculator.calculate(candidate, patterns, wordPatterns);

        assertNotNull(result);
        assertEquals(-1432090, result.getFitness());
        assertEquals(1, result.getCheckedTimesOK());
        assertEquals(143, result.getCheckedTimesNOK());
        assertEquals(1, result.getCheckedOK());
        assertEquals(311, result.getCheckedNOK());
        assertEquals(5, result.getSplitPos());
        assertEquals(19.75, result.getAvgLen());
        assertEquals(33.86369737639409, result.getVariance());
    }

    @Test
    void calculateOneTimeWithAlternativesOk() {
        Candidate candidate = new Candidate();
        candidate.setCandidate(
                "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXpunktXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXeinsXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXuhrXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX");

        Fitness result = fitnessCalculator.calculate(candidate, patterns, wordPatterns);

        assertNotNull(result);
        assertEquals(-1431060, result.getFitness());
        assertEquals(1, result.getCheckedTimesOK());
        assertEquals(143, result.getCheckedTimesNOK());
        assertEquals(2, result.getCheckedOK());
        assertEquals(310, result.getCheckedNOK());
        assertEquals(8, result.getSplitPos());
        assertEquals(11.285714285714286, result.getAvgLen());
        assertEquals(61.59081564185176, result.getVariance());
    }
}