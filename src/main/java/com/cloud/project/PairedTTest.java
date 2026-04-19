package com.cloud.project;

public class PairedTTest {
    public static void main(String[] args) {
        double[] before = {120.0, 95.0, 0.0833, 10.0, 60000.0};
        double[] after = {93.1, 86.5, 0.1074, 23.0, 36000.0};

        if (before.length != after.length || before.length < 2) {
            System.out.println("Error: Arrays must have same length and at least 2 values.");
            return;
        }

        int n = before.length;

        double meanBefore = mean(before);
        double meanAfter = mean(after);

        double[] differences = new double[n];
        for (int i = 0; i < n; i++) {
            differences[i] = before[i] - after[i];
        }

        double meanDiff = mean(differences);
        double stdDevDiff = sampleStandardDeviation(differences, meanDiff);

        double tValue = meanDiff / (stdDevDiff / Math.sqrt(n));

        // t-critical for two-tailed test at alpha = 0.05 with df = n - 1 = 4
        double tCritical = 2.776;
        boolean significant = Math.abs(tValue) > tCritical;

        System.out.println("Paired T-Test Results (alpha = 0.05)");
        System.out.println("------------------------------------");
        System.out.printf("Mean Before: %.4f%n", meanBefore);
        System.out.printf("Mean After : %.4f%n", meanAfter);
        System.out.printf("T-value    : %.6f%n", tValue);

        if (significant) {
            System.out.println("Interpretation: Difference is statistically significant.");
            System.out.println("Final Conclusion: Reject H0");
        } else {
            System.out.println("Interpretation: Difference is not statistically significant.");
            System.out.println("Final Conclusion: Accept H0");
        }
    }

    private static double mean(double[] values) {
        double sum = 0.0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    private static double sampleStandardDeviation(double[] values, double mean) {
        double sumSquares = 0.0;
        for (double v : values) {
            double diff = v - mean;
            sumSquares += diff * diff;
        }
        return Math.sqrt(sumSquares / (values.length - 1));
    }
}
