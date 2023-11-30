package toys;

public class RCCar extends BatteryPowered{
    static int productCode = 400;
    final static int startingSpeed = 10;
    final static int speedIncrease = 5;
    int speed;

    protected RCCar(String name, int numBatteries) {
        super(productCode, name, numBatteries);
        productCode++;
        this.speed = startingSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    protected void specialPlay(int time) {
        System.out.println("\t"+getName()+" races around at "+getSpeed()+"mph!");
        useBatteries(time);
        increaseWear(this.speed);
        this.speed += speedIncrease;
    }
    @Override
    public String toString(){
        return super.toString() + ", RCCar{S:" + getSpeed() + "}";
    }
}
