package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.StrangeConfig;

public class StrangeAttractorSearcher implements Runnable {
    private final StrangeConfig config = ConfigSingleton.getInstance().getStrange();
    private volatile boolean stopRequested = false;
    private final StrangeAttractorCallback callback;
    private final AttractorType attractorType;

    public StrangeAttractorSearcher(StrangeAttractorCallback callback, AttractorType attractorType) {
        this.callback = callback;
        this.attractorType = attractorType;
    }

    public void terminate() {
        stopRequested = true;
    }

    private StrangeAttractor createAttractor() {
        int numCoefficients = switch (attractorType) {
            case StrangeQuadratic -> 30;
            case StrangeCubic -> 60;
            default -> 0;
        };
        float[] coefficients = new float[numCoefficients];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = MathUtils.random(-config.getSearchRadius(), config.getSearchRadius());
        }
        return switch (attractorType) {
            case StrangeQuadratic -> new StrangeQuadraticAttractor(coefficients);
            case StrangeCubic -> new StrangeCubicAttractor(coefficients);
            default -> null;
        };
    }

    private boolean isStrange(StrangeAttractor attractor) {
        Vector3 point = new Vector3(
            (float) Math.random() * 20 - 10,
            (float) Math.random() * 20 - 10,
            (float) Math.random() * 20 - 10);
        float lyapunovSum = 0.0f;
        float prevDistance = 0.0f;

        for (int i = 0; i < 100; i++) {
            point = attractor.step(point, 1f / 120f);
        }

        for (int i = 0; i < config.getMaxIterations(); i++) {
            Vector3 nextPoint = attractor.step(point, 1f / 120f);

            if (nextPoint.len() > config.getDivergenceThreshold()) {
                return false;
            }
            if (nextPoint.len() < config.getConvergenceThreshold()) {
                return false;
            }

            if (i > 0) {
                float distance = point.dst(nextPoint);
                lyapunovSum += (float) Math.log(Math.abs(distance / prevDistance));
                prevDistance = distance;
            } else {
                prevDistance = point.dst(nextPoint);
            }
            point = nextPoint;
        }

        float lyapunovExponent = lyapunovSum / config.getMaxIterations();
        return lyapunovExponent > config.getLyapunovThreshold();
    }

    @Override
    public void run() {
        int attempts = 0;
        while (!stopRequested && attempts < 1e8 / config.getMaxIterations()) {
            StrangeAttractor attractor = createAttractor();
            if (attractor != null) {
                attempts++;
                if (isStrange(attractor)) {
                    callback.onAttractorFound(attractor, attempts);
                    callback.onAttractorSearchFinished(true, attempts);
                    return;
                }
            } else {
                callback.onAttractorSearchFinished(false, attempts);
                return;
            }
        }
        callback.onAttractorSearchFinished(false, attempts);
    }
}
