package timepiece;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeNamesEnglishTest {

    @Test
    void getTimeStrings(TestReporter reporter) {
        List<String>[][] timeStrings = TimeNamesEnglish.getTimeStrings();

        assertEquals(12, timeStrings.length);
        assertTrue(Arrays.stream(timeStrings).allMatch((arr) -> arr.length == 12));

        reporter.publishEntry("first dimension", String.valueOf(timeStrings.length));
        reporter.publishEntry("second dimension", Arrays.stream(timeStrings).map((arr) -> String.valueOf(arr.length)).reduce("", (a, b) -> a + ", " + b));

        for (int i = 0; i < timeStrings.length; i++) {
            for (int j = 0; j < timeStrings[i].length; j++) {
                reporter.publishEntry("(" + i + "," + j + ")", String.valueOf(timeStrings[i][j]));
            }
        }
    }

    @Test
    void getTimeStrings_asdfasdf() {
        List<String>[][] timeStrings = TimeNamesEnglish.getTimeStrings2();

        assertEquals(12, timeStrings.length);
        assertTrue(Arrays.stream(timeStrings).allMatch((arr) -> arr.length == 12));

        String[][] expTimeStrings = {
                {"twelve o clock", "five past twelve", "ten past twelve", "quarter past twelve", "twenty past twelve", "twenty five past twelve", "half past twelve", "twenty five to one", "twenty to one", "quarter to one", "ten to one", "five to one"},
                {"one o clock", "five past one", "ten past one", "quarter past one", "twenty past one", "twenty five past one", "half past one", "twenty five to two", "twenty to two", "quarter to two", "ten to two", "five to two"},
                {"two o clock", "five past two", "ten past two", "quarter past two", "twenty past two", "twenty five past two", "half past two", "twenty five to three", "twenty to three", "quarter to three", "ten to three", "five to three"},
                {"three o clock", "five past three", "ten past three", "quarter past three", "twenty past three", "twenty five past three", "half past three", "twenty five to four", "twenty to four", "quarter to four", "ten to four", "five to four"},
                {"four o clock", "five past four", "ten past four", "quarter past four", "twenty past four", "twenty five past four", "half past four", "twenty five to five", "twenty to five", "quarter to five", "ten to five", "five to five"},
                {"five o clock", "five past five", "ten past five", "quarter past five", "twenty past five", "twenty five past five", "half past five", "twenty five to six", "twenty to six", "quarter to six", "ten to six", "five to six"},
                {"six o clock", "five past six", "ten past six", "quarter past six", "twenty past six", "twenty five past six", "half past six", "twenty five to seven", "twenty to seven", "quarter to seven", "ten to seven", "five to seven"},
                {"seven o clock", "five past seven", "ten past seven", "quarter past seven", "twenty past seven", "twenty five past seven", "half past seven", "twenty five to eight", "twenty to eight", "quarter to eight", "ten to eight", "five to eight"},
                {"eight o clock", "five past eight", "ten past eight", "quarter past eight", "twenty past eight", "twenty five past eight", "half past eight", "twenty five to nine", "twenty to nine", "quarter to nine", "ten to nine", "five to nine"},
                {"nine o clock", "five past nine", "ten past nine", "quarter past nine", "twenty past nine", "twenty five past nine", "half past nine", "twenty five to ten", "twenty to ten", "quarter to ten", "ten to ten", "five to ten"},
                {"ten o clock", "five past ten", "ten past ten", "quarter past ten", "twenty past ten", "twenty five past ten", "half past ten", "twenty five to eleven", "twenty to eleven", "quarter to eleven", "ten to eleven", "five to eleven"},
                {"eleven o clock", "five past eleven", "ten past eleven", "quarter past eleven", "twenty past eleven", "twenty five past eleven", "half past eleven", "twenty five to twelve", "twenty to twelve", "quarter to twelve", "ten to twelve", "five to twelve"},
        };

        for (int i = 0; i < timeStrings.length; i++) {
            for (int j = 0; j < timeStrings[i].length; j++) {
                assertEquals(1, timeStrings[i][j].size());
                assertEquals(expTimeStrings[i][j], timeStrings[i][j].get(0));
            }
        }
    }
}
