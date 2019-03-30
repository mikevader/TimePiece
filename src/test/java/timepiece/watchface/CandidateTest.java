package timepiece.watchface;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CandidateTest {

    @Test
    void mixWith() {
        Candidate left = new CandidateTestBuilder()
                .withSchema("aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX|aaaaXXXXXXX")
                .build();
        Candidate right = new CandidateTestBuilder()
                .withSchema("XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb")
                .build();

        Candidate mix = left.mixWith(right, 20);

        String expCandiate = "aaaaXXXXXXX|aaaaXXXXbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb|XXXXXXXbbbb";
        assertEquals(expCandiate, mix.getWatchFace());
    }

    @Test
    void changeChar() {
        Candidate source = new CandidateTestBuilder()
                .withSchemaEmpty()
                .build();
        Candidate candidate = source.changeChar('e', 10);

        String expCandidate = "XXXXXXXXXXe|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";
        assertEquals(expCandidate, candidate.getWatchFace());
    }

    @Test
    void changeChar_atDelimiter() {
        Candidate source = new CandidateTestBuilder()
                .withSchemaEmpty()
                .build();
        Candidate candidate = source.changeChar('q', 11);

        String expCandidate = "XXXXXXXXXXX|qXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";
        assertEquals(expCandidate, candidate.getWatchFace());
    }

    @Test
    void compareTo() {
    }

    @Test
    void equals() {
    }

    @Test
    void hashCode_test() {
    }
}