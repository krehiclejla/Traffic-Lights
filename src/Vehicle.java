import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Vehicle extends Pane {
    protected final Rectangle body; // Changed from private to protected

    public Vehicle(String type, String direction) {
        super();

        // Create the main body of the vehicle
        body = new Rectangle(20, 20, Color.GRAY); // Default size and color
        getChildren().add(body);

        // Add a dash to indicate the front
        Line frontDash = new Line(10, 0, 10, 5); // A small line at the top center
        frontDash.setStroke(Color.WHITE);
        frontDash.setStrokeWidth(2);
        getChildren().add(frontDash);

        setDirection(direction);
    }

    private void setDirection(String direction) {
        switch (direction.toUpperCase()) {
            case "NORTH" -> setRotate(180);
            case "SOUTH" -> setRotate(0);
            case "EAST" -> setRotate(90);
            case "WEST" -> setRotate(270);
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        System.out.println("Created a " + direction + " vehicle with rotation: " + getRotate());
    }
}
