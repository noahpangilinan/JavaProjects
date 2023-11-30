package toys;
/*
This class is the basis for all the toys

Implements IToy
States
-Happiness
-Wear
-ProductCode
-Name

Abstract Class
play()
- implements special play and increases happiness

Does not implement special play



 */
public abstract class Toy implements IToy{
    final static int initialHappiness = 0;
    final static int maxHappiness = 100;
    final static double initialWear = 0.0;
    final int productCode;
    final String name;

    int happiness = 0;
    double wear = 0.0;



    protected Toy(int productCode, String name){
        this.productCode = productCode;
        this.name = name;
    }

    @Override
    public int getProductCode() {return productCode;}

    @Override
    public String getName() {return name;}

    @Override
    public int getHappiness() {return happiness;}

    @Override
    public boolean isRetired() {
        if(this.happiness >= maxHappiness) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public double getWear() {return this.wear;}

    @Override
    public void increaseWear(double amount) {this.wear += amount;}

    @Override
    public void play(int time) {
        System.out.println("PLAYING("+ time +"): " +  toString());
        specialPlay(time);
        this.happiness += time;
        if(isRetired()){
            System.out.println("RETIRED: " + toString());
        }

    }

    abstract protected void specialPlay(int time);
    public String toString(){
        return ("Toy{PC:"+ this.productCode + ", N:"+getName()+", H:"+getHappiness()+", R:"+isRetired()+", W:"+getWear()+"}");}
}
