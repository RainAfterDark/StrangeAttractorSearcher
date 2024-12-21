package io.github.rainafterdark.strangeattractorsearcher.Physics;

public interface StrangeAttractorCallback {
    void onAttractorFound(StrangeAttractor attractor, int attempts);
    void onAttractorSearchFinished(boolean success, int attempts);
}
