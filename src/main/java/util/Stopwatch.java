package util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Provides utilities for displaying time in the application.
 */
public class Stopwatch {

    private LongProperty seconds = new SimpleLongProperty();
    private StringProperty hhmmss = new SimpleStringProperty();

    private Timeline timeline;

    /**
     * Creates an instance of the {@code Stopwatch} class.
     */
    public Stopwatch() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> seconds.set(seconds.get() + 1)),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        hhmmss.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(seconds.get() * 1000, "HH:mm:ss"), seconds));
    }

    /**
     * Returns the property of the previous lasting time.
     *
     * @return the property of the previous lasting time
     */
    public LongProperty secondsProperty() {
        return seconds;
    }

    /**
     * Returns the property of the previous lasting time in the specified format.
     *
     * @return the property of the previous lasting time in the specified format
     */
    public StringProperty hhmmssProperty() {
        return hhmmss;
    }

    /**
     * Starts the stopwatch.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the stop watch.
     */
    public void stop() {
        timeline.pause();
    }

    /**
     * Resets the stopwatch.
     */
    public void reset() {
        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }
        seconds.set(0);
    }

    /**
     * Returns the status of the stopwatch.
     *
     * @return the status of the stopwatch
     */
    public Animation.Status getStatus() {
        return timeline.getStatus();
    }

}
