package timepiece;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TimeNamesEnglish {

    private static String[] getHours() {
        return new String[]{
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine",
                "ten",
                "eleven",
                "twelve"
        };
    }

    private static String[] getMinutes() {
        return new String[]{
                "zero",
                "five",
                "ten",
                "fifteen",
                "twenty",
                "twenty+five",
                "thirty",
                "thirty+five",
                "forty",
                "forty+five",
                "fifty",
                "fifty+five"
        };
    }

    public static List<String>[][] getTimeStrings() {
        List<String>[][] times = new List[12][];
        for (int i = 0; i < times.length; i++) {
            times[i] = new List[12];
            for (int j = 0; j < times[i].length; j++) {
                times[i][j] = new LinkedList<>();
            }
        }

        String[] hours = getHours();
        String[] minutes = getMinutes();

        //simple format    X uhr Y, Y = n * 5
        String simpleFormat = "%s %s";
        for (int hour = 0; hour < hours.length; hour++) {
            for (int minute = 0; minute < minutes.length; minute++) {
                String h = hours[hour];
                String m = minutes[minute];
//				if(hour == 0) h = "ein";
                if (minute == 0) m = "o times";

                if (minute != 0) times[hour][minute].add(String.format("%s past %s", m, h).trim());

                if (minute == 1) m = "o five";
                times[hour][minute].add(String.format(simpleFormat, h, m).trim());
            }
        }

        for (int hour = 0; hour < hours.length; hour++) {
            times[hour][3].add(String.format("quarter past %s", hours[hour]).trim());

            times[hour][6].add(String.format("half past %s", hours[hour]).trim());

            String nextHour = hour == hours.length - 1 ? hours[0] : hours[hour + 1];
            times[hour][9].add(String.format("quarter to %s", nextHour).trim());
        }

        return times;
    }

    public static List<String>[][] getTimeStrings2() {
        String[] hours = {
                "twelve",
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine",
                "ten",
                "eleven"
        };

        String[] minutes = {
                "%s o clock",
                "five past %s",
                "ten past %s",
                "quarter past %s",
                "twenty past %s",
                "twenty five past %s",
                "half past %s",
                "twenty five to %s",
                "twenty to %s",
                "quarter to %s",
                "ten to %s",
                "five to %s"
        };

        List<String>[][] times = new List[12][];
        for (int h = 0; h < times.length; h++) {
            times[h] = new List[12];
            for (int m = 0; m < times[h].length; m++) {
                String hour = hours[(h + m/7) % 12];
                String minute = minutes[m];
                times[h][m] = Collections.singletonList(String.format(minute, hour));
            }
        }

        return times;
    }

    public static void main(String[] args) {
        List<String>[][] ts = getTimeStrings();
        for (List<String>[] lists : ts) {
            for (List<String> list : lists) {
                for (String string : list) {
                    System.out.println(string);
                }
            }
        }
    }
}
