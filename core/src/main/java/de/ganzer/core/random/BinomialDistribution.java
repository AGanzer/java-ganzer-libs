package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random non-negative integer values.
 * <p>
 * The value obtained is the number of successes in a sequence of t yes/no
 * experiments, each of which succeeds with probability p.
 */
@SuppressWarnings("unused")
public class BinomialDistribution implements Distribution<Long> {
    /**
     * Creates a new instance with t set to 1 and p set to 0.5.
     */
    public BinomialDistribution() {
        this(1, 0.5);
    }

    /**
     * Creates a new instance with p set to 0.5.
     *
     * @param t The t distribution parameter (number of trial).
     */
    public BinomialDistribution(long t) {
        this(t, 0.5);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param t The t distribution parameter (number of trial).
     * @param p The p distribution parameter (probability of a trial generating
     *          true).
     * @throws IllegalArgumentException p is less than 0.0 or greater than 1.0.
     */
    public BinomialDistribution(long t, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("p");

        this.t = t;
        this.p = p;

        if (0 < p && p < 1) {
            r0 = (long)((t + 1) * p);
            pr = StrictMath.exp(logGamma(t + 1.) - logGamma(r0 + 1.) -
                    logGamma(t - r0 + 1.) + r0 * StrictMath.log(p) +
                    (t - r0) * StrictMath.log(1 - p));

            oddsRatio = p / (1 - p);
        }
    }

    /**
     * Creates the next random number according to the set t and p.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Long next(Random random) {
        if (t == 0 || p == 0)
            return 0L;

        if (p == 1)
            return t;

        double u = random.nextDouble() - pr;

        if (u < 0)
            return r0;

        double pu = pr;
        double pd = pu;
        long ru = r0;
        long rd = ru;

        while (true) {
            if (rd >= 1) {
                pd *= rd / (oddsRatio * (t - rd + 1));
                u -= pd;

                if (u < 0)
                    return rd - 1;
            }

            --rd;
            ++ru;

            if (ru <= t) {
                pu *= (t - ru + 1) * oddsRatio / ru;
                u -= pu;

                if (u < 0)
                    return ru;
            }
        }
    }

    private final long t;
    private final double p;
    private double pr;
    private double oddsRatio;
    private long r0;

    private static double logGamma(double z)
    {
        // This implementation is from
        // https://visualstudiomagazine.com/articles/2022/08/02/logbeta-loggamma-functions-csharp.aspx

        // Lanczos approximation g=5, n=7
        double[] coef = new double[] { 1.000000000190015,
                76.18009172947146, -86.50532032941677,
                24.01409824083091, -1.231739572450155,
                0.1208650973866179e-2, -0.5395239384953e-5 };

        double LogSqrtTwoPi = 0.91893853320467274178;

        if (z < 0.5)
            return StrictMath.log(Math.PI / StrictMath.sin(Math.PI * z)) - logGamma(1.0 - z);

        double zz = z - 1.0;
        double b = zz + 5.5; // g + 0.5
        double sum = coef[0];

        for (int i = 1; i < coef.length; ++i)
            sum += coef[i] / (zz + i);

        return (LogSqrtTwoPi + StrictMath.log(sum) - b) + (StrictMath.log(b) * (zz + 0.5));
    }
}
