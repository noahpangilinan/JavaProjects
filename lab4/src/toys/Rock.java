package toys;

public class Rock extends Toy{
    static int productCode = 700;
    int weight;

    protected Rock(String name, int weight) {
        super(productCode, name);
        productCode++;
        this.weight = weight;

    }

    public int getWeight() {
        return weight;
    }

    @Override
    protected void specialPlay(int time) {
        System.out.println("\tYou lift "+getName()+ "(" + getWeight() +"lbs) and throw it " + time + " times.");
        increaseWear(this.weight*time*.25);
    }

    @Override
    public String toString() {
        return super.toString() + ", Rock{W:" + getWeight() + '}';
    }
}
