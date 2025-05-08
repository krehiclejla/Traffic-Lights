import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;

public class TrafficLightSimulator extends Application {
    private Intersection intersection;

    private Queue<RedVehicle> redNorthQueue = new LinkedList<>();
    private Queue<BlueVehicle> blueNorthQueue = new LinkedList<>();
    private Queue<GreenVehicle> greenNorthQueue = new LinkedList<>();
    private Queue<YellowVehicle> yellowNorthQueue = new LinkedList<>();

    private Queue<RedVehicle> redSouthQueue = new LinkedList<>();
    private Queue<BlueVehicle> blueSouthQueue = new LinkedList<>();
    private Queue<GreenVehicle> greenSouthQueue = new LinkedList<>();
    private Queue<YellowVehicle> yellowSouthQueue = new LinkedList<>();

    private Queue<RedVehicle> redEastQueue = new LinkedList<>();
    private Queue<BlueVehicle> blueEastQueue = new LinkedList<>();
    private Queue<GreenVehicle> greenEastQueue = new LinkedList<>();
    private Queue<YellowVehicle> yellowEastQueue = new LinkedList<>();

    private Queue<RedVehicle> redWestQueue = new LinkedList<>();
    private Queue<BlueVehicle> blueWestQueue = new LinkedList<>();
    private Queue<GreenVehicle> greenWestQueue = new LinkedList<>();
    private Queue<YellowVehicle> yellowWestQueue = new LinkedList<>();

    @Override
    public void start(Stage primaryStage) {
        intersection = new Intersection();
        intersection.startSimulation();

        Pane pane = new Pane();

        // Set background color to dirt brown
        pane.setStyle("-fx-background-color: #9b7653;");

        // Draw wider roads
        Rectangle verticalRoad = new Rectangle(370, 0, 80, 800);
        verticalRoad.setFill(Color.DARKGRAY);
        Rectangle horizontalRoad = new Rectangle(0, 370, 800, 80);
        horizontalRoad.setFill(Color.DARKGRAY);

        pane.getChildren().addAll(verticalRoad, horizontalRoad);

        // Draw dashed lines in the center of the roads
        for (int i = 0; i < 800; i += 50) {
            Line dashedLineV = new Line(410, i, 410, i + 30);
            dashedLineV.setStroke(Color.WHITE);
            dashedLineV.setStrokeWidth(4);

            Line dashedLineH = new Line(i, 410, i + 30, 410);
            dashedLineH.setStroke(Color.WHITE);
            dashedLineH.setStrokeWidth(4);

            pane.getChildren().addAll(dashedLineV, dashedLineH);
        }

        // Add traffic lights
        Circle[] lightCircles = {
            new Circle(350, 350, 10, Color.RED),  // North light
            new Circle(470, 470, 10, Color.RED),  // South light
            new Circle(470, 350, 10, Color.RED),  // East light
            new Circle(350, 470, 10, Color.RED)   // West light
        };

        pane.getChildren().addAll(lightCircles);

        // Assume trafficLights is an array of light controllers for each direction
        TrafficLight[] trafficLights = intersection.getTrafficLights(); // Assuming a getter for TrafficLight objects

        // Update light states continuously
        for (int i = 0; i < trafficLights.length; i++) {
            final int index = i;
            new Thread(() -> {
                while (true) {
                    Platform.runLater(() -> {
                        String state = trafficLights[index].getState();
                        lightCircles[index].setFill(switch (state) {
                            case "GREEN" -> Color.GREEN;
                            case "YELLOW" -> Color.YELLOW;
                            default -> Color.RED;
                        });
                    });
                    try {
                        Thread.sleep(500); // Update every 500 ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        spawnVehicles(pane);

        Scene scene = new Scene(pane, 800, 800);
        primaryStage.setTitle("Traffic Light Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        startVehicleMovement();
    }

    private void spawnVehicles(Pane pane) {
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    String[] directions = {"NORTH", "SOUTH", "EAST", "WEST"};
                    String direction = directions[(int) (Math.random() * directions.length)];

                    int vehicleType = (int) (Math.random() * 4); // Random vehicle type
                    switch (vehicleType) {
                        case 0 -> { // RedVehicle
                            RedVehicle vehicle = new RedVehicle("REGULAR", direction);
                            addVehicleToQueue(vehicle, direction, redNorthQueue, redSouthQueue, redEastQueue, redWestQueue);
                            pane.getChildren().add(vehicle);
                        }
                        case 1 -> { // BlueVehicle
                            BlueVehicle vehicle = new BlueVehicle("REGULAR", direction);
                            addVehicleToQueue(vehicle, direction, blueNorthQueue, blueSouthQueue, blueEastQueue, blueWestQueue);
                            pane.getChildren().add(vehicle);
                        }
                        case 2 -> { // GreenVehicle
                            GreenVehicle vehicle = new GreenVehicle("REGULAR", direction);
                            addVehicleToQueue(vehicle, direction, greenNorthQueue, greenSouthQueue, greenEastQueue, greenWestQueue);
                            pane.getChildren().add(vehicle);
                        }
                        case 3 -> { // YellowVehicle
                            YellowVehicle vehicle = new YellowVehicle("REGULAR", direction);
                            addVehicleToQueue(vehicle, direction, yellowNorthQueue, yellowSouthQueue, yellowEastQueue, yellowWestQueue);
                            pane.getChildren().add(vehicle);
                        }
                    }
                });

                try {
                    Thread.sleep((int) (Math.random() * 2000) + 1000); // Delay between spawns
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private <T extends Pane> void addVehicleToQueue(T vehicle, String direction, Queue<T> northQueue, Queue<T> southQueue, Queue<T> eastQueue, Queue<T> westQueue) {
        switch (direction) {
            case "NORTH" -> {
                vehicle.setTranslateX(380);
                vehicle.setTranslateY(-40);
                northQueue.add(vehicle);
            }
            case "SOUTH" -> {
                vehicle.setTranslateX(420);
                vehicle.setTranslateY(840);
                southQueue.add(vehicle);
            }
            case "EAST" -> {
                vehicle.setTranslateX(840);
                vehicle.setTranslateY(380);
                eastQueue.add(vehicle);
            }
            case "WEST" -> {
                vehicle.setTranslateX(-40);
                vehicle.setTranslateY(420);
                westQueue.add(vehicle);
            }
        }
    }

    private void startVehicleMovement() {
        new Thread(() -> {
            while (true) {
                moveQueue(redNorthQueue, "NORTH", 310);
                moveQueue(blueNorthQueue, "NORTH", 310);
                moveQueue(greenNorthQueue, "NORTH", 310);
                moveQueue(yellowNorthQueue, "NORTH", 310);

                moveQueue(redSouthQueue, "SOUTH", 490);
                moveQueue(blueSouthQueue, "SOUTH", 490);
                moveQueue(greenSouthQueue, "SOUTH", 490);
                moveQueue(yellowSouthQueue, "SOUTH", 490);

                moveQueue(redEastQueue, "EAST", 490);
                moveQueue(blueEastQueue, "EAST", 490);
                moveQueue(greenEastQueue, "EAST", 490);
                moveQueue(yellowEastQueue, "EAST", 490);

                moveQueue(redWestQueue, "WEST", 310);
                moveQueue(blueWestQueue, "WEST", 310);
                moveQueue(greenWestQueue, "WEST", 310);
                moveQueue(yellowWestQueue, "WEST", 310);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private <T extends Pane> void moveQueue(Queue<T> currentQueue, String direction, double stopLine) {
        final double MIN_DISTANCE = 50; // Minimum distance to maintain between vehicles
    
        // Combine all queues for the given direction
        Queue<Pane> combinedQueue = new LinkedList<>();
        combinedQueue.addAll(redNorthQueue);
        combinedQueue.addAll(blueNorthQueue);
        combinedQueue.addAll(greenNorthQueue);
        combinedQueue.addAll(yellowNorthQueue);
    
        combinedQueue.addAll(redSouthQueue);
        combinedQueue.addAll(blueSouthQueue);
        combinedQueue.addAll(greenSouthQueue);
        combinedQueue.addAll(yellowSouthQueue);
    
        combinedQueue.addAll(redEastQueue);
        combinedQueue.addAll(blueEastQueue);
        combinedQueue.addAll(greenEastQueue);
        combinedQueue.addAll(yellowEastQueue);
    
        combinedQueue.addAll(redWestQueue);
        combinedQueue.addAll(blueWestQueue);
        combinedQueue.addAll(greenWestQueue);
        combinedQueue.addAll(yellowWestQueue);
    
        if (!currentQueue.isEmpty()) {
            for (T vehicle : currentQueue) {
                Platform.runLater(() -> {
                    TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), vehicle);
                    double vehicleY = vehicle.getTranslateY();
                    double vehicleX = vehicle.getTranslateX();
    
                    boolean canMove = true;
    
                    // Check against all vehicles in the combined queue
                    for (Pane otherVehicle : combinedQueue) {
                        if (otherVehicle == vehicle) continue; // Skip current vehicle
                        double otherY = otherVehicle.getTranslateY();
                        double otherX = otherVehicle.getTranslateX();
    
                        boolean tooClose = switch (direction) {
                            case "NORTH" -> vehicleY < otherY && Math.abs(vehicleY - otherY) < MIN_DISTANCE;
                            case "SOUTH" -> vehicleY > otherY && Math.abs(vehicleY - otherY) < MIN_DISTANCE;
                            case "EAST" -> vehicleX > otherX && Math.abs(vehicleX - otherX) < MIN_DISTANCE;
                            case "WEST" -> vehicleX < otherX && Math.abs(vehicleX - otherX) < MIN_DISTANCE;
                            default -> false;
                        };
    
                        if (tooClose) {
                            canMove = false; // Stop if too close to any vehicle
                            break;
                        }
                    }
    
                    boolean atStopLine = switch (direction) {
                        case "NORTH" -> vehicleY >= stopLine - 10;
                        case "SOUTH" -> vehicleY <= stopLine + 10;
                        case "EAST" -> vehicleX <= stopLine + 10;
                        case "WEST" -> vehicleX >= stopLine - 10;
                        default -> false;
                    };
    
                    if (canMove) {
                        if (atStopLine) {
                            if (intersection.isGreen(direction)) {
                                currentQueue.poll(); // Remove from current queue when passing the intersection
                                switch (direction) {
                                    case "NORTH" -> transition.setByY(800);
                                    case "SOUTH" -> transition.setByY(-800);
                                    case "EAST" -> transition.setByX(-800);
                                    case "WEST" -> transition.setByX(800);
                                }
                                transition.setDuration(Duration.seconds(2));
                                transition.setOnFinished(event -> vehicle.setVisible(false));
                            }
                        } else {
                            switch (direction) {
                                case "NORTH" -> transition.setByY(40);
                                case "SOUTH" -> transition.setByY(-40);
                                case "EAST" -> transition.setByX(-40);
                                case "WEST" -> transition.setByX(40);
                            }
                        }
                        transition.play();
                    }
                });
            }
        }
    }
    
    
    
    

    public static void main(String[] args) {
        launch(args);
    }
}