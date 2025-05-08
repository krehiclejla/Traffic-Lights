import java.util.concurrent.Semaphore;

public class Intersection {
    private TrafficLight[] trafficLights;
    private Semaphore semaphore;

    public Intersection() {
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight("NORTH");
        trafficLights[1] = new TrafficLight("SOUTH");
        trafficLights[2] = new TrafficLight("EAST");
        trafficLights[3] = new TrafficLight("WEST");
        semaphore = new Semaphore(1, true); // Fair semaphore for coordination
    }

    public void startSimulation() {
        new Thread(() -> {
            try {
                while (true) {
                    semaphore.acquire();
                    // Green for North-South
                    trafficLights[0].setState("GREEN");
                    trafficLights[1].setState("GREEN");
                    trafficLights[2].setState("RED");
                    trafficLights[3].setState("RED");
                    Thread.sleep(5000); // Green duration
                    trafficLights[0].setState("YELLOW");
                    trafficLights[1].setState("YELLOW");
                    Thread.sleep(2000); // Yellow duration
                    trafficLights[0].setState("RED");
                    trafficLights[1].setState("RED");
                    trafficLights[2].setState("GREEN");
                    trafficLights[3].setState("GREEN");
                    Thread.sleep(5000); // Green duration
                    trafficLights[2].setState("YELLOW");
                    trafficLights[3].setState("YELLOW");
                    Thread.sleep(2000); // Yellow duration
                    semaphore.release();
                    Thread.sleep(1000); // Delay before next cycle
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public TrafficLight[] getTrafficLights() {
        return trafficLights;
    }

    public boolean isGreen(String direction) {
        for (TrafficLight light : trafficLights) {
            if (light.getDirection().equals(direction) && light.getState().equals("GREEN")) {
                return true;
            }
        }
        return false;
    }
}
