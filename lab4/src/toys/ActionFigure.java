package toys;

public class ActionFigure extends Doll{

    static int productCode = 300;
    int age;
    String catchphrase;
    final static int minEnergyLevel = 1;
    final static Color hairColor = Color.ORANGE;
    int energyLevel;

    protected ActionFigure(String name, int age, String catchphrase) {
        super(productCode, name, Color.ORANGE, age, catchphrase);
        productCode++;
        this.energyLevel = minEnergyLevel;
        this.catchphrase = catchphrase;


    }

    public int getEnergyLevel() {
        return this.energyLevel;
    }

    @Override
    public void specialPlay(int time){
        System.out.println("\t" + getName() + " kung foo chops with "+ (time * getEnergyLevel()) +" energy!");
        this.energyLevel++;
        super.specialPlay(time);
    }

    @Override
    public String toString(){
        return super.toString() + ", ActionFigure{E:" + getEnergyLevel() + "}";
    }

}
