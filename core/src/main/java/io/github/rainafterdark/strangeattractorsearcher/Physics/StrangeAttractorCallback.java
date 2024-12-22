package io.github.rainafterdark.strangeattractorsearcher.Physics;

import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeAttractor;

public interface StrangeAttractorCallback {
    void onAttractorFound(StrangeAttractor attractor, int attempts);
    void onAttractorSearchFinished(boolean success, int attempts);
}
