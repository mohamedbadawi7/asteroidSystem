package com.example.as4;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ControlPanelView extends StackPane implements Subscriber {

    Canvas canvas;

    GraphicsContext gc;

    Label label;
    Slider slider;
    CheckBox checkBox1;
    CheckBox checkBox2;

    public ControlPanelView(double x, double y) {

        this.setStyle("-fx-base: #191919; -fx-background-color: #191919");

        canvas = new Canvas(x, y);

        VBox options = new VBox();
        label = new Label("Rotation Speed");
        slider = new Slider(0, 1, 0);
        checkBox1 = new CheckBox("Asteroid Movement");
        checkBox2 = new CheckBox("Asteroid Span");

        options.getChildren().addAll(label, slider, checkBox1, checkBox2);

        checkBox1.setSelected(true);
        checkBox2.setSelected(true);

        this.getChildren().addAll(canvas, options);
    }

    public void setUpEvents(SpaceController controller) {

        slider.valueProperty().addListener(observable -> {
            controller.handleSliderUsed(slider);
        });

        checkBox1.setOnAction(event -> {
            controller.handleAsteroidMovement();
        });

        checkBox2.setOnAction(event -> {
            controller.handleAsteroidSpin();
        });

    }

    @Override
    public void modelChanged(List<Asteroid> asteroids, List<Star> stars, double x, double y, double a,
                             double mouseX, double mouseY) {

    }

    @Override
    public void worldChanged(double rotation) {

    }

    @Override
    public void selectedAsteroid(List<Asteroid> asteroid) {

    }

    @Override
    public void cursorSizeChanged(double areaCursorSize) {

    }

}
