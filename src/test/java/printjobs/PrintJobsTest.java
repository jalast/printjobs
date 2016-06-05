package printjobs;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.COMMENTS;
import static java.util.regex.Pattern.DOTALL;
import static org.junit.Assert.*;

/**
 * Created by Larry on 6/2/16.
 */
public class PrintJobsTest {

    public static final OutputStream DEVNULL = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }
    };

    @Test
    public void testReport() throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertEquals(64.10, new PrintJobs().report(new FileInputStream("printjobs.csv"), outputStream), .005);

        Scanner scanner = new Scanner(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        checkLine(scanner, 4.75, "SS", 25, 15, 10);
        checkLine(scanner, 6.80, "DS", 55, 42, 13);
        checkLine(scanner, 52.40, "DS", 502, 480, 22);
        checkLine(scanner, .15, "SS", 1, 1, 0);
    }

    @Test
    public void testReportWithError() throws Exception {
        testInput("25, 10 false\n", false);
        testInput("25.0, 10, false\n", false);
        testInput("25.0, X10, false\n", false);
        testInput("25 10 false\n", false);
        testInput("25, 10, false, true\n", false);
    }

    @Test
    public void testReportWithFunkyButValidInput() throws Exception {
        testInput("25,10,false\n", true);
        testInput(" 25 , 10 , false \n", true);
        testInput("25, 10, what?\n", true);
    }

    private void testInput(String s, boolean isValid) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(s.getBytes());
        try {
            new PrintJobs().report(inputStream, DEVNULL);
            if (!isValid) fail();
        } catch (PrintJobs.JobFormatException ignore) {
            if (isValid) fail();
        }
    }

    private void checkLine(Scanner scanner, double charge, String type, int total, int bw, int color) {
        scanner.useDelimiter("[^.\\d]+");
        assertEquals(charge, scanner.nextDouble(), .005);
        scanner.useDelimiter("\\W+");
        assertEquals(type, scanner.next());
        scanner.useDelimiter("\\D+");
        assertEquals(total, scanner.nextInt());
        assertEquals(bw, scanner.nextInt());
        assertEquals(color, scanner.nextInt());
    }

}
