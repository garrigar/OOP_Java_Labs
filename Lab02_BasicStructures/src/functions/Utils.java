package functions;

public class Utils {
    public static final double EPS = machineEps();

    private static double machineEps() {
        double eps = 1.0;
        while (1.0 + 0.5 * eps != 1.0) {
            eps *= 0.5;
        }
        return eps;
    }
}
