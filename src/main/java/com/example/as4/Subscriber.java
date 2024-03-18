package com.example.as4;

import java.util.List;

public interface Subscriber {


    public void modelChanged(List<Asteroid> asteroids, List<Star> stars, double x, double y, double a, double mouseX, double mouseY);

    public void worldChanged(double rotation);

    public void selectedAsteroid(List<Asteroid> asteroid);

    public void cursorSizeChanged(double areaCursorSize);
}
