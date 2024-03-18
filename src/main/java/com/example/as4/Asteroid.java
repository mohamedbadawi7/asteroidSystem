package com.example.as4;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class Asteroid {


    //part 1

    private double x,y;

    //part 2
    private double radius;
    private double dx, dy, da;
    private double tranformationX, transformationY, tA = 0;
    private double angle;
    double[] xPoints;
    double[] yPoints;
    double[] homeXPoints;
    double[] homeYPoints;
    int sections;

    //part 4

    boolean onObject = false;
    double mx, my;
    PixelReader pixelReader;
    public WritableImage bitmap;
    double mainViewWidth, mainViewHeight;
    Canvas offScreenCanvas;
    GraphicsContext offScreenGC;


    public Asteroid(double x, double y, double radius, double mainViewWidth, double mainViewHeight) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.mainViewWidth = mainViewWidth;
        this.mainViewHeight = mainViewHeight;

        formPolygon();

        Random random = new Random();
        this.dx = random.nextDouble(0.0001, 0.0009);
        this.dy = random.nextDouble(0.0001, 0.0009);
        this.da = random.nextDouble(1, 3);

        double width = findMaxX(deNormalizeX(homeXPoints)) - findMinX(deNormalizeX(homeXPoints));
        double height = findMaxY(deNormalizeY(homeYPoints)) - findMinY(deNormalizeY(homeYPoints));

        offScreenCanvas = new Canvas(width, height);
        offScreenGC = offScreenCanvas.getGraphicsContext2D();
        bitmap = new WritableImage((int) (offScreenCanvas.getWidth()), (int) (offScreenCanvas.getHeight()));

        offScreenGC.setFill(Color.BLACK);
        offScreenGC.fillRect(0, 0, offScreenCanvas.getWidth(), offScreenCanvas.getHeight());

        offScreenGC.setFill(Color.WHITE);
        offScreenGC.translate(offScreenCanvas.getWidth()/2, offScreenCanvas.getHeight()/2);
        offScreenGC.fillPolygon(deNormalizeX(homeXPoints), deNormalizeY(homeYPoints), sections);

        offScreenCanvas.snapshot(null, bitmap);
        pixelReader = bitmap.getPixelReader();
    }
    public double getTranformationX() {
        return tranformationX;
    }

    public void setTransformationX(double tranformationX) {
        this.tranformationX = tranformationX;
    }

    public double getTransformationY() {
        return transformationY;
    }

    public void setTransformationY(double transformationY) {
        this.transformationY = transformationY;
    }

    public int getSections() {
        return sections;
    }


    public void formPolygon() {

        this.sections = splitCircletoGetRadius();

        xPoints = new double[sections];
        yPoints = new double[sections];

        this.angle = getAngle(sections);
        double theAngle = angle;


        for (int i = 1; i <= sections; i++) {

            Random random = new Random();

            double angle = 2 * Math.PI * i / sections;
            double newRadius = radius * random.nextDouble(0.5, 1);
            double newX = newRadius * Math.cos(angle);
            double newY = newRadius * Math.sin(angle);

            xPoints[i-1] = newX;
            yPoints[i-1] = newY;

            theAngle=theAngle + theAngle;

        }

        homeXPoints = Arrays.copyOf(xPoints, xPoints.length);
        homeYPoints = Arrays.copyOf(yPoints, yPoints.length);


    }

    private double findMaxX(double[] homeXPoints) {

        double max = -100;

        for (int i = 0; i < homeXPoints.length; i++) {
            if (homeXPoints[i] > max) {
                max = homeXPoints[i];
            }
        }
        return max;
    }

    private double findMinX(double[] homeXPoints) {

        double min = 100;

        for (int i = 0; i < homeXPoints.length; i++) {
            if (homeXPoints[i] < min) {
                min = homeXPoints[i];
            }
        }
        return min;
    }

    private double findMaxY(double[] homeYPoints) {

        double max = -100;

        for (int i = 0; i < homeYPoints.length; i++) {
            if (homeYPoints[i] > max) {
                max = homeYPoints[i];
            }
        }
        return max;
    }

    private double findMinY(double[] homeYPoints) {

        double min = 100;

        for (int i = 0; i < homeYPoints.length; i++) {
            if (homeYPoints[i] < min) {
                min = homeXPoints[i];
            }
        }
        return min;
    }

    public int splitCircletoGetRadius() {

        Random random = new Random();
        int sections = random.nextInt(4,8);

        return sections;
    }

    public double getAngle(int sections) {

        double angle = (2*PI) / sections;

        return angle;
    }

    public void moveAsteroid() {


        tranformationX += dx;
        transformationY += dy;

        if (tranformationX > 1) {
            tranformationX = 0;
            transformationY = 0;
        }

        if (transformationY > 1) {
            transformationY = 0;
            tranformationX = 0;
        }
    }

    public void spin() {

        tA += da;
        if (tA == 360) {
            tA = 0;
        }
    }

    public double getTA() {
        return tA;
    }

    public void setdX(double dx) {
        this.dx = dx;
    }

    public void setdY(double dy) {
        this.dy = dy;
    }


    public boolean contains(double mouseX, double mouseY) throws IndexOutOfBoundsException {

        mx = (int) mouseX - (tranformationX*800) + bitmap.getWidth()/2;
        my = (int) mouseY - (transformationY*800) + bitmap.getHeight()/2;

        onObject = check(mx, my);

        return onObject;
    }

    private boolean check(double x, double y) {

        int px = (int) x;
        int py = (int) y;

        if (px >= 0 && px < bitmap.getWidth() && py >= 0 && py < bitmap.getHeight()) {
            return pixelReader.getColor(px, py).equals(Color.WHITE);
        }
        else {
            return false;
        }
    }

    public boolean checkEndpoints(double mouseX, double mouseY, double radius) {

        double maxX = (findMaxX(homeXPoints) + tranformationX) * mainViewWidth;
        double minX = (findMinX(homeXPoints) + tranformationX) * mainViewWidth;
        double maxY = (findMaxY(homeYPoints) + transformationY) * mainViewHeight;
        double minY = (findMinY(homeYPoints) + transformationY) * mainViewHeight;


        if (((maxX < mouseX + radius/2) && (maxX > mouseX - radius/2)) && ((maxY < mouseY + radius/2) && (maxY > mouseY - radius/2))){
            return true;
        }

        if (((minX < mouseX + radius/2) && (minX > mouseX - radius/2)) && ((minY < mouseY + radius/2) && (minY > mouseY - radius/2))) {
            return true;
        }

        return false;
    }

    public double[] deNormalizeX(double[] pointsX) {

        double[] normalizedX = new double[pointsX.length];

        for (int i = 1; i <= pointsX.length; i++) {
            normalizedX[i-1] = (pointsX[i-1]) * 800;
        }

        return normalizedX;
    }

    public double[] deNormalizeY(double[] pointsY) {

        double[] normalizedY = new double[pointsY.length];

        for (int i = 1; i <= pointsY.length; i++) {

            normalizedY[i-1] = pointsY[i-1] * 800;
        }

        return normalizedY;
    }



}
