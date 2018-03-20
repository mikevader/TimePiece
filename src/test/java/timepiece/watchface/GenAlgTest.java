package timepiece.watchface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import timepiece.TimeNamesEnglish;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenAlgTest {

    private GenAlg genAlg;
    private Random random;

    @BeforeEach
    void setUp() {
        PatternGenerator patternGenerator = new PatternGenerator();
        random = mock(Random.class);
        genAlg = new GenAlg();
        genAlg.setRand(random);
        genAlg.setWatchfacePattern(patternGenerator.createPatterns(TimeNamesEnglish.getTimeStrings()));
    }

    @Test
    void incGeneration() {
        Solution solution = new Solution();
        genAlg.setSolution(solution);

        genAlg.incGeneration();

        assertEquals(1, genAlg.getSolution().getGeneration());
    }

    @Test
    void doAdd_minmax_equal() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(best);
        solution.getCandidates().add(worst);
        genAlg.setSolution(solution);

        Candidate candidate = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(0)
                .build();

        when(random.nextInt(100)).thenReturn(1);
        when(random.nextDouble()).thenReturn(0.0);

        boolean doAdd = genAlg.doAdd(candidate);

        assertTrue(doAdd);
    }

    @Test
    void doAdd_below_min() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(80)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(worst);
        solution.getCandidates().add(best);
        genAlg.setSolution(solution);

        Candidate candidate = new Candidate();
        candidate.setFitness(10);

        when(random.nextInt(100)).thenReturn(1);
        when(random.nextDouble()).thenReturn(0.0);

        boolean doAdd = genAlg.doAdd(candidate);

        assertTrue(doAdd);
    }

    @Test
    void doAdd_fitness_between_minmax_high_prop() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(-100)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(worst);
        solution.getCandidates().add(best);
        genAlg.setSolution(solution);

        Candidate candidate = new Candidate();
        candidate.setFitness(0);

        when(random.nextInt(100)).thenReturn(1);
        when(random.nextDouble()).thenReturn(0.0);

        boolean doAdd = genAlg.doAdd(candidate);

        assertTrue(doAdd);
    }

    @Test
    void doAdd_fitness_between_minmax_low_prop() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(0)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(worst);
        solution.getCandidates().add(best);
        genAlg.setSolution(solution);

        Candidate candidate = new Candidate();
        candidate.setFitness(10);

        when(random.nextInt(100)).thenReturn(1);
        when(random.nextDouble()).thenReturn(1.0);

        boolean doAdd = genAlg.doAdd(candidate);

        assertFalse(doAdd);
    }

    @Test
    void getRandom() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(80)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(worst);
        solution.getCandidates().add(best);
        genAlg.setSolution(solution);

        when(random.nextGaussian()).thenReturn(-0.4);
        Candidate random = genAlg.getRandom(true);

        assertNotNull(random);
        assertSame(best, random);
    }

    @Test
    void getRandom_second() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(80)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(worst);
        solution.getCandidates().add(best);
        genAlg.setSolution(solution);

        when(random.nextGaussian()).thenReturn(-1.0);
        Candidate random = genAlg.getRandom(true);

        assertNotNull(random);
        assertSame(worst, random);
    }

    @Test
    void getRandomChar() {
        char[] includedChars = new char[]{
                'a', 'e', 'f', 'g', 'h', 'i', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'y'
        };
        when(random.nextInt(genAlg.getWatchfacePattern().getIncludedChar().length)).thenReturn(1);
        char randomChar = genAlg.getRandomChar();

        assertEquals('e', randomChar);
    }

    @Test
    void createRandom() {
        when(random.nextInt(genAlg.getWatchfacePattern().getIncludedChar().length)).thenReturn(1); // for getRandomChar()
        when(random.nextInt(genAlg.getWatchfacePattern().getInclWords().size())).thenReturn(2); // for addRandomWord()
        Candidate candidate = genAlg.createRandom();

        String expCandiate = "halfeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee|eeeeeeeeeee";
        assertEquals(expCandiate, candidate.getCandidate());
    }

    @Test
    void addToSolution() {
        Candidate worst = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(80)
                .build();
        Candidate best = new CandidateTestBuilder()
                .withSchemaEmpty()
                .withFitness(100)
                .build();

        Solution solution = new Solution();
        solution.getCandidates().add(worst);
        solution.getCandidates().add(best);
        genAlg.setSolution(solution);

        when(random.nextInt(100)).thenReturn(1);
        when(random.nextDouble()).thenReturn(0.0);

        Candidate candidate = new Candidate();
        candidate.setFitness(20);

        genAlg.addToSolution(candidate);

        assertEquals(3, genAlg.getSolution().getCandidates().size());
    }

    @Test
    void mixTogether() {
        Candidate left = new Candidate();
        left.setCandidate("aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX");
        Candidate right = new Candidate();
        right.setCandidate("XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb");
        when(random.nextInt(left.getCandidate().length())).thenReturn(20);
        Candidate mix = genAlg.mixTogether(left, right);

        String expCandiate = "aaaaXXXXXXX|aaaaXXXXbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb";
        assertEquals(expCandiate, mix.getCandidate());
    }

    @Test
    void changeRandom() {
        Candidate source = new Candidate();
        when(random.nextInt(source.getCandidate().length())).thenReturn(10); // for changeRandom()
        when(random.nextInt(genAlg.getWatchfacePattern().getIncludedChar().length)).thenReturn(1); // for getRandomChar()
        Candidate candidate = genAlg.changeRandom(source);

        String expCandidate = "XXXXXXXXXXe|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";
        assertEquals(expCandidate, candidate.getCandidate());
    }

    @Test
    void addRandomWord() {
        when(random.nextInt(genAlg.getWatchfacePattern().getInclWords().size())).thenReturn(1);
        Candidate newCandidate = genAlg.addRandomWord(new Candidate());

        String expCandidate = "nineXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";
        assertEquals(expCandidate, newCandidate.getCandidate());
    }
}
