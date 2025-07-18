package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random non-negative integer values.
 * <p>
 * The value obtained is the probability of exactly i occurrences of a random
 * event if the expected mean number of its occurrence under the same
 * conditions (on the same time/space interval) is mean.
 */
public class PoissonDistribution implements Distribution<Long> {
    /**
     * Creates a new instance with mean set to 1.0.
     */
    public PoissonDistribution() {
        this(1.0);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param mean The mean (µ). The must be greater than 0.0.
     *
     * @throws IllegalArgumentException mean is less than or equal to 0.0.
     */
    public PoissonDistribution(double mean) {
        if (mean <= 0)
            throw new IllegalArgumentException("mean");

        this.mean = mean;

        if (mean < 10) {
            s = 0.0;
            d = 0.0;
            l = StrictMath.exp(-mean);
            omega = 0.0;
            c3 = 0.0;
            c2 = 0.0;
            c1 = 0.0;
            c0 = 0.0;
            c = 0.0;
        } else {
            normDist = new NormalDistribution();
            expDist = new ExponentialDistribution();
            s = StrictMath.sqrt(mean);
            d = 6 * mean * mean;
            l = mean - 1.1484;
            omega = 0.3989423 / s;

            double b1 = 0.4166667E-1 / mean;
            double b2 = 0.3 * b1 * b1;

            c3 = 0.1428571 * b1 * b2;
            c2 = b2 - 15.0 * c3;
            c1 = b1 - 6.0 * b2 + 45.0 * c3;
            c0 = 1.0 - b1 + 3.0 * b2 - 15.0 * c3;
            c = 0.1069 / mean;
        }
    }

    /**
     * Creates the next random number according to the set mean.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Long next(Random random) {
        long x = 0;

        if (mean < 10) {
            for (double p = random.nextDouble(); p > l; ++x)
                p *= random.nextDouble();
        } else {
            double difmuk = 0;
            double u = 0;
            double g = mean + s * normDist.next(random);

            if (g > 0) {
                x = (long)g;

                if (x >= l)
                    return x;

                difmuk = mean - x;
                u = random.nextDouble();

                if (d * u >= difmuk * difmuk * difmuk)
                    return x;
            }

            for (boolean usingExpDist = false; true; usingExpDist = true) {
                double e = 0;

                if (usingExpDist || g < 0) {
                    double t;

                    do {
                        e = expDist.next(random);
                        u = random.nextDouble();
                        u += u - 1;
                        t = 1.8 + (u < 0 ? -e : e);
                    } while (t <= -.6744);

                    x = (long)(mean + s * t);
                    difmuk = mean - x;
                    usingExpDist = true;
                }

                double px;
                double py;

                if (x < 10) {
                    px = -mean;
                    py = StrictMath.pow(mean, x) / fac[(int)x];
                } else {
                    double del = .8333333E-1 / x;
                    del -= 4.8 * del * del * del;
                    double v = difmuk / x;

                    if (StrictMath.abs(v) > 0.25)
                        px = x * StrictMath.log(1 + v) - difmuk - del;
                    else
                        px = x * v * v * (((((((.1250060 * v - .1384794) *
                                v + .1421878) * v - .1661269) * v + .2000118) *
                                v - .2500068) * v + .3333333) * v - .5) - del;

                    py = .3989423 / StrictMath.sqrt(x);
                }

                double r = (0.5 - difmuk) / s;
                double r2 = r * r;
                double fx = -0.5 * r2;
                double fy = omega * (((c3 * r2 + c2) * r2 + c1) * r2 + c0);

                if (usingExpDist) {
                    if (c * StrictMath.abs(u) <= py * StrictMath.exp(px + e) - fy * StrictMath.exp(fx + e))
                        break;
                } else {
                    if (fy - u * fy <= py * StrictMath.exp(px - fx))
                        break;
                }
            }
        }

        return x;
    }

    private final double mean;
    private final double s;
    private final double d;
    private final double l;
    private final double omega;
    private final double c0;
    private final double c1;
    private final double c2;
    private final double c3;
    private final double c;
    private NormalDistribution normDist;
    private ExponentialDistribution expDist;
    private static final long[] fac = new long[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880};

}
