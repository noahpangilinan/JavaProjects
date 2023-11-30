package toys;

public abstract class BatteryPowered extends Toy{
    final static int fullyCharged = 100;
    final static int depleted = 0;
    int batteryLevel;
    int numBatteries;

    protected BatteryPowered(int productCode, String name, int numBatteries) {
        super(productCode, name);
        this.batteryLevel = fullyCharged;
        this.numBatteries = numBatteries;

    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void useBatteries(int time){
        this.batteryLevel -= (time + numBatteries);
        if(batteryLevel < depleted){
            this.batteryLevel = 0;
            System.out.println("\tDEPLETED:" + toString());
            this.batteryLevel = fullyCharged;
            System.out.println("\tRECHARGED:" + toString());
        }
    }

    @Override
    public String toString(){
        return super.toString() + ", BatteryPowered{BL:" + getBatteryLevel() + ", NB:" + getNumBatteries() + "}";
    }

}
