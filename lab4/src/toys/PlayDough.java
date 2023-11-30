package toys;

public class PlayDough extends Toy{
    static int productCode = 100;
    final static double wearMultiplier = .05;
    private final Color color;

    protected PlayDough(String name, Color color){
        super(productCode, name);
        this.color = color;
        productCode++;
    }
    @Override
    protected void specialPlay(int time){
        System.out.println("\tArts and crafting with "+getColor()+ " "+ getName());
        increaseWear(time*wearMultiplier);
    }
    
    @Override
    public String toString(){return super.toString() + ", PlayDough{C:" + getColor() + "}";}

    public Color getColor(){
        return this.color;
    }
    @Override
    public String getName() {
        return this.name;
    }
}
