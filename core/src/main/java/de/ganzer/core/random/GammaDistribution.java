package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random positive floating-point values.
 * <p>
 * For floating-point α, the value obtained is the sum of α independent
 * exponentially distributed random variables, each of which has a mean of β.
 */
public class GammaDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with shape and scale set to 1.0.
     */
    public GammaDistribution() {
        this(1.0, 1.0);
    }

    /**
     * Creates a new instance with scale set to 1.0.
     *
     * @param shape The shape.
     */
    public GammaDistribution(double shape) {
        this(shape, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param shape The shape.
     * @param scale The scale.
     */
    public GammaDistribution(double shape, double scale) {
        this.shape = shape;
        this.scale = scale;
    }

    /**
     * Creates the next random number according to the set shape and scale.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        double a = shape;
        double x;

        if (a == 1)
            x = expDist.next(random);
        else if (a > 1) {
            double b = a - 1;
            double c = 3 * a - 0.75;

            while (true) {
                double __u = random.nextDouble();
                double __v = random.nextDouble();
                double __w = __u * (1 - __u);

                if (__w != 0) {
                    double __y = StrictMath.sqrt(c / __w) * (__u - 0.5);
                    x = b + __y;

                    if (x >= 0) {
                        double __z = 64 * __w * __w * __w * __v * __v;

                        if (__z <= 1 - 2 * __y * __y / x)
                            break;

                        if (StrictMath.log(__z) <= 2 * (b * StrictMath.log(x / b) - __y))
                            break;
                    }
                }
            }
        } else {
            while (true) {
                double u = random.nextDouble();
                double es = expDist.next(random);

                if (u <= 1 - a) {
                    x = StrictMath.pow(u, 1 / a);

                    if (x <= es)
                        break;
                } else {
                    double e = -StrictMath.log((1 - u) / a);

                    x = StrictMath.pow(1 - a + a * e, 1 / a);

                    if (x <= e + es)
                        break;
                }
            }
        }

        return x * scale;
    }

    private final double shape;
    private final double scale;
    private final ExponentialDistribution expDist = new ExponentialDistribution();
}
