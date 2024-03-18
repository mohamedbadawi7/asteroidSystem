package com.example.as4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteractionModel {

    private double worldRotation;
    private double increment;
    List<Subscriber> subscribers;

    private List<Asteroid> selectedAsteroids = null;

    private double areaCursorSize = 0;


    public InteractionModel() {

        subscribers = new ArrayList<>();
        Random random = new Random();
        selectedAsteroids = new ArrayList<>();

        increment = random.nextDouble(0.00001, 0.0001);
    }

    public void addSubscribers(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifySubscribers() {

        for (Subscriber subscriber: subscribers) {
            subscriber.worldChanged(worldRotation);
            subscriber.selectedAsteroid(selectedAsteroids);
            subscriber.cursorSizeChanged(areaCursorSize);
        }
    }

    public void rotateWorld() {

        worldRotation += increment;
        notifySubscribers();
    }

    public void setWorldRotation(double worldRotation) {

        this.worldRotation = worldRotation;
        notifySubscribers();
    }

    public void addSelected(Asteroid asteroid) {

        selectedAsteroids.add(asteroid);
        notifySubscribers();
    }

    public List<Asteroid> getSelected() {
        return selectedAsteroids;
    }

    public void clearSelected() {
        selectedAsteroids.clear();
        notifySubscribers();
    }


    public double getAreaCursorSize() {
        return areaCursorSize;
    }

    public void changeAreaCursorSize(double areaCursorSize) {
        this.areaCursorSize += areaCursorSize;
        if (this.areaCursorSize > 150) {
            this.areaCursorSize = 150;
        }
        notifySubscribers();
    }
}
