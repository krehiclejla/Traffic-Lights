import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BlueVehicle extends Pane {
    protected final Rectangle body;

    public BlueVehicle(String type, String direction) {
        super();

        body = new Rectangle(20, 20, Color.BLUE);
        getChildren().add(body);

        Line frontDash = new Line(10, 0, 10, 5);
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
        System.out.println("Created a " + direction + " Blue Vehicle with rotation: " + getRotate());
    }
}
