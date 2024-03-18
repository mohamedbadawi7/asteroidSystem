package com.example.as4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceModel {

    List<Subscriber> subscribers;
    List<Asteroid> asteroids;
    List<Star> stars;

    private double mouseX, mouseY = 0;
    private double translationX, translationY, translationA;

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public SpaceModel() {

        subscribers = new ArrayList<>();
        asteroids = new ArrayList<>();
        stars = new ArrayList<>();

    }

    public void notifySubscribers() {
        for (Subscriber subscriber: subscribers) {
            subscriber.modelChanged(asteroids, stars, translationX, translationY, translationA, mouseX, mouseY);
        }
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void createAsteroid(Asteroid asteroid) {
        asteroid.formPolygon();
        asteroids.add(asteroid);
        notifySubscribers();
    }

    public void createStar(Star star) {
        stars.add(star);
        notifySubscribers();
    }

    public void moveAsteroids(List<Asteroid> selectedAsteroids) {

        for (Asteroid asteroid: asteroids) {

            if (!selectedAsteroids.contains(asteroid)) {
                asteroid.moveAsteroid();
            }
        }
        notifySubscribers();
    }

    public void spinAsteroids() {

        for (Asteroid asteroid: asteroids) {
            asteroid.spin();
        }
        notifySubscribers();
    }

    public void drawZoomedIn(double x, double y) {

        this.mouseX = x;
        this.mouseY = y;

        notifySubscribers();
    }

    public boolean contains(double x, double y) {

        for (Asteroid asteroid: asteroids) {
            if (asteroid.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public Asteroid containsAsteroid(double x, double y) {

        for (Asteroid asteroid: asteroids) {
            if (asteroid.contains(x, y)) {
                return asteroid;
            }
        }
        return null;
    }

    public List<Asteroid> findAsteroidsInCircle(double mouseX, double mouseY, double radius) {

        List<Asteroid> asteroidsInside = new ArrayList<>();

        for (Asteroid asteroid: asteroids) {

            if (asteroid.checkEndpoints(mouseX, mouseY, radius)) {
                asteroidsInside.add(asteroid);
            }
        }

        return asteroidsInside;
    }

    public void dragAsteroid(double x, double y, List<Asteroid> selectedAsteroids, double mouseX, double mouseY) {

        for (Asteroid asteroid: selectedAsteroids) {

            asteroid.setTransformationX(x/800);
            asteroid.setTransformationY(y/800);
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        notifySubscribers();
    }

    public void changeSpeeds(double x, double y, List<Asteroid> selectedAsteroids) {

        for (Asteroid asteroid: selectedAsteroids) {

            asteroid.setdX(x/80000);
            asteroid.setdY(y/80000);
        }
        notifySubscribers();
    }

}
