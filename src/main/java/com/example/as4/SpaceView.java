package com.example.as4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SpaceView extends StackPane implements Subscriber {

    Canvas canvas;

    GraphicsContext gc;

    double rotationSpeed;

    double angle;

    double mouseX, mouseY;

    InteractionModel iModel;

    List<Asteroid> selectedAsteroids;

    double cursorRadius = 0;

    public SpaceView(double x, double y) {

        canvas = new Canvas(x, y);
        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        this.getChildren().addAll(canvas);

    }
    public void setUpEvents(SpaceController controller) {

        setOnMouseMoved(controller::handleMouseMoved);
        setOnMousePressed(controller::handleMousePressed);
        setOnMouseDragged(controller::handleMouseDragged);
        setOnMouseReleased(controller::handleMouseReleased);
        setOnScroll(controller::handleWheel);

    }

    public void addiModel(InteractionModel iModel) {
        this.iModel = iModel;
    }


    @Override
    public void modelChanged(List<Asteroid> asteroids, List<Star> stars, double x, double y, double a,
                             double mouseX, double mouseY) {


        this.mouseX = mouseX;
        this.mouseY = mouseY;
        //draw(asteroids, stars, x, y, a);
        draw(asteroids, stars);
    }

    @Override
    public void worldChanged(double rotation) {

        this.rotationSpeed = rotation;
    }

    @Override
    public void selectedAsteroid(List<Asteroid> asteroids) {
        this.selectedAsteroids = new ArrayList<>();

        selectedAsteroids.addAll(asteroids);
    }

    @Override
    public void cursorSizeChanged(double areaCursorSize) {

        this.cursorRadius = areaCursorSize;
    }

    public void draw(List<Asteroid> asteroids, List<Star> stars) {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLUE);
        gc.fillOval(mouseX - cursorRadius / 2, mouseY - cursorRadius / 2, cursorRadius, cursorRadius);

        for (Asteroid asteroid : asteroids) {

            gc.save();

            gc.translate(asteroid.getTranformationX() * canvas.getWidth(), asteroid.getTransformationY() * canvas.getHeight());
            gc.rotate(asteroid.getTA());
            gc.scale(canvas.getWidth(), canvas.getHeight());


            if (selectedAsteroids != null) {
                if (!selectedAsteroids.isEmpty()) {
                    if (selectedAsteroids.contains(asteroid)) {
                        gc.setFill(Color.YELLOW);
                        gc.fillPolygon(asteroid.xPoints, asteroid.yPoints, asteroid.sections);
                    } else {
                        gc.setFill(Color.GRAY);
                        gc.fillPolygon(asteroid.xPoints, asteroid.yPoints, asteroid.sections);
                    }
                } else {
                    gc.setFill(Color.GRAY);
                    gc.fillPolygon(asteroid.xPoints, asteroid.yPoints, asteroid.sections);
                }

                gc.restore();
            }
            else {
                gc.setFill(Color.GRAY);
                gc.fillPolygon(asteroid.xPoints, asteroid.yPoints, asteroid.sections);
            }
            gc.restore();

        }
        for (Star star : stars) {

            gc.save();

            gc.translate(canvas.getWidth()/2, canvas.getHeight()/2);
            gc.rotate(angle);
            gc.translate(-canvas.getWidth()/2, -canvas.getHeight()/2);

            gc.setFill(Color.WHITE);
            gc.fillOval(star.getX() * canvas.getWidth(), star.getY() * canvas.getHeight(), star.getRadius() * canvas.getWidth(), star.getRadius() * canvas.getHeight());

            gc.restore();
        }

        angle += rotationSpeed;

    }

}
