package printjobs;

import org.junit.Test;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Created by Larry on 6/2/16.
 */
public class PrintJobsTest {
    @Test
    public void testReport() throws Exception {
        PrintJobs printJobs = new PrintJobs();
        assertEquals(64.1, printJobs.report(new FileInputStream("printjobs.csv")), .005);
    }

}