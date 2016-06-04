package printjobs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Larry on 6/1/16.
 */
public class PrintJobs {

    private final Map<JobType, PageRates> rates;

    public PrintJobs() {
        rates = new HashMap<JobType, PageRates>();
        rates.put(JobType.SINGLE_SIDED, new PageRates(.15, .25));
        rates.put(JobType.DOUBLE_SIDED, new PageRates(.10, .20));
    }

    double report(InputStream inputStream, OutputStream outputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintStream printStream = new PrintStream(outputStream);
        return reader.lines()
                .map(s -> new Job(rates, s.split(", ")))
                .map((job) -> { printStream.println(job); return job;})
                .mapToDouble(Job::getCharge)
                .sum();
    }

    /**
     * Created by Larry on 6/1/16.
     */
    private static class Job {
        int totalPages;
        int colorPages;
        JobType jobType;
        private double charge;

        Job(Map<JobType, PageRates> rates, int totalPages, int colorPages, boolean doubleSided) {
            this.totalPages = totalPages;
            this.colorPages = colorPages;
            this.jobType = doubleSided ? JobType.DOUBLE_SIDED : JobType.SINGLE_SIDED;
            charge = reckon(rates);
        }

        private double reckon(Map<JobType, PageRates> rates) {
            PageRates r = rates.get(jobType);
            return r.bwRate * (totalPages - colorPages) + r.getColorRate() * colorPages;
        }

        Job(Map<JobType, PageRates> rates, String... args) {
            this(rates, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]));
        }

        double getCharge() {
            return charge;
        }

        @Override
        public String toString() {
            return String.format("%.2f %s %d Pages %d B&W %d Color",
                    charge, jobType.abbr(), totalPages, totalPages - colorPages, colorPages);
        }
    }

    /**
     * Created by Larry on 6/1/16.
     */
    private enum JobType {
        SINGLE_SIDED, DOUBLE_SIDED;
        public String abbr() {
            return this == SINGLE_SIDED ? "SS" : "DS";
        }
    }

    /**
     * Created by Larry on 6/1/16.
     */
    private static class PageRates {
        double bwRate;
        double colorRate;

        public double getBwRate() {
            return bwRate;
        }

        public double getColorRate() {
            return colorRate;
        }

        public PageRates(double bw, double color) {
            this.bwRate = bw;
            this.colorRate = color;
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length > 2) throw new Exception("Usage: printjobs [ input-stream | - [ output-stream | -] ]");
            InputStream in = (args.length > 0 || args[0].equals("-")) ? System.in : new FileInputStream(args[0]);
            OutputStream out = (args.length > 1 || args[1].equals("-")) ? System.out : new FileOutputStream(args[0]);
            double totalCharge = new PrintJobs().report(in, out);
            new PrintStream(out).printf("Total charges: %.2f%n", totalCharge);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

}
