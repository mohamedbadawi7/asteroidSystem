package com.example.as4;

import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.List;

public class SpaceController {


    SpaceModel model;

    InteractionModel iModel;

    boolean box1 = true;
    boolean box2 = true;

    double mouseX, mouseY, radius = 0;

    public enum State {READY, DRAGGING};

    public State currentState = State.READY;

    double dx, dy, startX, startY;

    public void addModel(SpaceModel model) {
        this.model = model;
    }

    public void addiModel(InteractionModel iModel) {
        this.iModel = iModel;
    }


    public void handleAnimationTick() {

        if (box1) {
            model.moveAsteroids(iModel.getSelected());
        }
        if (box2) {
            model.spinAsteroids();
        }

        if (!(currentState == State.DRAGGING)) {
            if (model.contains(mouseX, mouseY)) {;
                iModel.addSelected(model.containsAsteroid(mouseX, mouseY));
            } else {
                iModel.clearSelected();
            }
        }
        checkforPointsInside(mouseX, mouseY, radius);
    }


    public void handleMouseMoved(MouseEvent event) {

        model.drawZoomedIn(event.getX(), event.getY());
        mouseX = event.getX();
        mouseY = event.getY();

        if (model.contains(event.getX(), event.getY())) {;
            iModel.addSelected(model.containsAsteroid(event.getX(), event.getY()));
        }

        checkforPointsInside(mouseX, mouseY, radius);
    }

    public void handleMousePressed(MouseEvent event) {

        switch (currentState) {
            case READY -> {
                this.startX = event.getX();
                this.startY = event.getY();

                currentState = State.DRAGGING;
            }
        }
    }

    public void handleMouseDragged(MouseEvent event) {

        switch (currentState) {
            case DRAGGING -> {

                this.dx = (event.getX() - startX);
                this.dy = (event.getY() - startY);

                model.dragAsteroid(event.getX(), event.getY(), iModel.getSelected(), event.getX(), event.getY());
                checkforPointsInside(event.getX(), event.getY(), radius);
                currentState = State.DRAGGING;
            }
        }
    }

    public void handleMouseReleased(MouseEvent event) {

        switch (currentState) {
            case DRAGGING -> {
                model.changeSpeeds(dx, dy, iModel.getSelected());
                currentState = State.READY;
            }
        }
        iModel.clearSelected();
        checkforPointsInside(mouseX, mouseY, radius);
    }

    public void handleSliderUsed(Slider slider) {

        iModel.setWorldRotation(slider.getValue());
    }

    public void handleAsteroidMovement() {

        if (!box1) {
            box1 = true;
        }
        else {
            box1 = false;
        }
    }

    public void handleAsteroidSpin() {

        if (!box2) {
            box2 = true;
        }
        else {
            box2 = false;
        }
    }

    public void handleWheel(ScrollEvent event) {


        iModel.changeAreaCursorSize(event.getDeltaY());
        radius = iModel.getAreaCursorSize();
        mouseX = event.getX();
        mouseY = event.getY();
        checkforPointsInside(mouseX, mouseY, radius);

    }


    public void checkforPointsInside(double mouseX, double mouseY, double radius) {

        List<Asteroid> asteroidInside = model.findAsteroidsInCircle(mouseX, mouseY, radius);


        if (asteroidInside != null) {
            //iModel.clearSelected();
            for (Asteroid asteroid : asteroidInside) {
                iModel.addSelected(asteroid);
            }
        }
    }

}
