package com.example.as4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class CursorView extends StackPane implements Subscriber {

    Canvas canvas;

    GraphicsContext gc;

    double worldRotation = 0;

    double mouseX, mouseY, angle;

    SpaceView spaceView;

    public CursorView(double x, double y) {

        canvas = new Canvas(x, y);
        gc = canvas.getGraphicsContext2D();


        this.getChildren().add(canvas);
    }

    public void addView(SpaceView view) {
        spaceView = view;
    }

    public void setUpEvents(SpaceController controller) {

        setOnMouseMoved(controller::handleMouseMoved);

    }

    @Override
    public void modelChanged(List<Asteroid> asteroids, List<Star> stars, double x, double y, double a, double mouseX, double mouseY) {

        this.mouseX = rotateX(mouseX, mouseY, angle);
        this.mouseY = rotateY(mouseX, mouseY, angle);

        if (this.mouseX < 0) {
            this.mouseX = -this.mouseX;
        }

        if (this.mouseY < 0) {
            this.mouseY = -this.mouseY;
        }

        gc.clearRect(0, 0, 800, 800);
        gc.save();

        gc.translate(-(mouseX)-(canvas.getWidth()), -(mouseY)-(canvas.getHeight()));

        gc.scale(2,2);

        draw(asteroids, stars, x, y, a, this.mouseX, this.mouseY);

        gc.restore();


    }

    @Override
    public void worldChanged(double rotation) {

        worldRotation = rotation;
    }

    @Override
    public void selectedAsteroid(List<Asteroid> asteroids) {

    }

    @Override
    public void cursorSizeChanged(double areaCursorSize) {

    }

    private double rotateX(double x, double y, double radians) {

        return Math.cos(radians) * x - Math.sin(radians) * y;
    }

    private double rotateY(double x, double y, double radians) {

        return Math.sin(radians) * x - Math.cos(radians) * y;
    }


    public double[] deNormalizeX(double[] pointsX) {

        double[] normalizedX = new double[pointsX.length];

        for (int i = 1; i <= pointsX.length; i++) {
            normalizedX[i-1] = (pointsX[i-1]) * canvas.getWidth();
        }

        return normalizedX;
    }

    public double[] deNormalizeY(double[] pointsY) {

        double[] normalizedY = new double[pointsY.length];

        for (int i = 1; i <= pointsY.length; i++) {

            normalizedY[i-1] = pointsY[i-1] * canvas.getHeight();
        }

        return normalizedY;
    }



    public void draw(List<Asteroid> asteroids, List<Star> stars, double tX, double tY, double a, double mouseX, double mouseY) {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 800);

        for (Asteroid asteroid: asteroids) {

            gc.save();

            gc.translate(asteroid.getTranformationX() * 800, asteroid.getTransformationY() * 800);
            gc.rotate(asteroid.getTA());
            gc.scale(800, 800);

            gc.setFill(Color.GRAY);
            gc.fillPolygon(asteroid.xPoints, asteroid.yPoints, asteroid.getSections());

            gc.restore();

        }

     }

     public void drawSpaceView(double mouseX, double mouseY) {

        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
     }

}
