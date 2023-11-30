package toys;

public class Robot extends BatteryPowered{
    static int productCode = 500;
    final static int flySpeed = 25;
    final static int runSpeed = 10;
    final static int initialSpeed = 0;
    int speed;
    boolean flying;
    int distance = 0;


    protected Robot(String name, int numBatteries, boolean flying) {
        super(productCode, name, numBatteries);
        productCode++;
        this.speed = initialSpeed;
        this.flying = flying;
    }

    public boolean isFlying() {
        return flying;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    protected void specialPlay(int time) {
        if(isFlying()){
            this.distance += (time * flySpeed);
            System.out.println("\t" + getName() + " is flying around with total distance: " + getDistance());
            increaseWear(flySpeed);
        }
        else{
            this.distance += (time * runSpeed);
            System.out.println("\t" + getName() + " is running around with total distance: " + getDistance());
            increaseWear(runSpeed);
        }
        useBatteries(time);
    }
    @Override
    public String toString(){
        return super.toString() + ", Robot{F:" + isFlying() + ", D:" + getDistance() + "}";
    }
}
