package printjobs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Created by Larry on 6/1/16.
 */
public class PrintJobs {

    public PrintJobs() {
        Map<JobType, PageRates> rates = new HashMap<JobType, PageRates>();
        rates.put(JobType.SINGLE_SIDED, new PageRates(.15, .25));
        rates.put(JobType.DOUBLE_SIDED, new PageRates(.10, .20));


    }

    double report(InputStream inputStream) {
//        Collector<Job, ?, ?> collector;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        reader.lines().map(s -> new Job(s.split(", ")));
        return 0;
    }

    /**
     * Created by Larry on 6/1/16.
     */
    private static class Job {
        int totalPages;
        int colorPages;
        boolean doubleSided;

        Job(int totalPages, int colorPages, boolean doubleSided) {
            this.totalPages = totalPages;
            this.colorPages = colorPages;
            this.doubleSided = doubleSided;
        }

        Job(String... args) {
            this(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]));
        }
    }

    /**
     * Created by Larry on 6/1/16.
     */
    public static enum JobType {
        SINGLE_SIDED, DOUBLE_SIDED
    }

    /**
     * Created by Larry on 6/1/16.
     */
    public static class PageRates {
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
}
