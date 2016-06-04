package printjobs;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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
    @Test
    public void testReport() throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertEquals(64.10, new PrintJobs().report(new FileInputStream("printjobs.csv"), outputStream), .005);

        String actual = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        System.out.print(actual);
        Scanner scanner = new Scanner(actual);
        checkLine(scanner, 4.75, "SS", 25, 15, 10);
        checkLine(scanner, 6.80, "DS", 55, 42, 13);
        checkLine(scanner, 52.40, "DS", 502, 480, 22);
        checkLine(scanner, .15, "SS", 1, 1, 0);
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
