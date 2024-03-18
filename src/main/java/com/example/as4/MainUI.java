package com.example.as4;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Random;

public class MainUI extends BorderPane {

    SpaceModel model;

    AnimationTimer animationTimer;

    public MainUI() {

        this.setStyle("-fx-background-color: black");

        SpaceController controller = new SpaceController();
        SpaceView view = new SpaceView(800, 800);
        model = new SpaceModel();
        InteractionModel iModel = new InteractionModel();

        VBox sideViews = new VBox();

        SpaceView minView = new SpaceView(200, 200);
        CursorView cursorView = new CursorView(200, 200);

        cursorView.addView(view);
        ControlPanelView controlPanelView = new ControlPanelView(200, 400);


        controller.addModel(model);
        controller.addiModel(iModel);

        model.addSubscriber(view);
        model.addSubscriber(minView);
        model.addSubscriber(cursorView);
        iModel.addSubscribers(view);

        view.setUpEvents(controller);
        controlPanelView.setUpEvents(controller);
        view.addiModel(iModel);


        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                controller.handleAnimationTick();
            }
        };

        animationTimer.start();

        setUpAsteroids();
        setUpStars();

        sideViews.getChildren().addAll(minView, cursorView, controlPanelView);


        this.setCenter(view);
        this.setLeft(sideViews);

    }

    public void setUpAsteroids() {

        for (int i = 1; i <= 10; i++) {

            Random random = new Random();
            double newX = random.nextDouble();
            double newY = random.nextDouble();
            double radius = random.nextDouble(0.05, 0.1);


            Asteroid asteroid = new Asteroid(newX, newY, radius, 800, 800);

            model.createAsteroid(asteroid);
        }
    }

    public void setUpStars() {

        for (int i = 1; i <= 100; i++) {

            Random random = new Random();
            double newX = random.nextDouble();
            double newY = random.nextDouble();

            Star star = new Star(newX, newY);
            model.createStar(star);
        }
    }
}
