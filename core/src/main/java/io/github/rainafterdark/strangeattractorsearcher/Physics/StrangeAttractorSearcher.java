package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.StrangeConfig;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeCubicAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeQuadraticAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeQuarticAttractor;

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
        int numCoefficients;
        switch (attractorType) {
            case StrangeQuadratic:
                numCoefficients = 30;
                break;
            case StrangeCubic:
                numCoefficients = 60;
                break;
            case StrangeQuartic:
                numCoefficients = 105;
                break;
            default:
                numCoefficients = 0;
                break;
        }
        float[] coefficients = new float[numCoefficients];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = MathUtils.random(-config.getSearchRadius(), config.getSearchRadius());
        }
        switch (attractorType) {
            case StrangeQuadratic:
                return new StrangeQuadraticAttractor(coefficients);
            case StrangeCubic:
                return new StrangeCubicAttractor(coefficients);
            case StrangeQuartic:
                return new StrangeQuarticAttractor(coefficients);
            default:
                return null;
        }
    }

    private boolean isStrange(StrangeAttractor attractor) {
        float offset = 0.001f;
        float stepSize = 1f / 120f;
        Vector3 point = new Vector3(
            MathUtils.random(-offset, offset),
            MathUtils.random(-offset, offset),
            MathUtils.random(-offset, offset));
        float lyapunovSum = 0.0f;
        float prevDistance = 0.0f;

        for (int i = 0; i < 100; i++) {
            point = attractor.step(point, stepSize);
        }

        Vector3 point2 = point.cpy();
        point2.add(MathUtils.random(-offset, offset),
                   MathUtils.random(-offset, offset),
                   MathUtils.random(-offset, offset));

        for (int i = 0; i < config.getMaxIterations(); i++) {
            Vector3 nextPoint = attractor.step(point, stepSize);

            if (nextPoint.len() > config.getDivergenceThreshold()) {
                return false;
            }
            if (nextPoint.len() < config.getConvergenceThreshold()) {
                return false;
            }

            point = nextPoint;
            point2 = attractor.step(point2, stepSize);
            float distance = point.dst(point2);

            if (i > 0) {
                lyapunovSum += (float) Math.log(Math.abs(distance / prevDistance));
            }
            prevDistance = distance;
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
