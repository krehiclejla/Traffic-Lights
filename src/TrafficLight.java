public class TrafficLight {
    private String state;
    private String direction;

    public TrafficLight(String direction) {
        this.direction = direction;
        this.state = "RED";
    }

    public void setState(String newState) {
        this.state = newState;
        System.out.println(direction + " traffic light is now " + state);
    }

    public String getState() {
        return state;
    }

    public String getDirection() {
        return direction;
    }
}
