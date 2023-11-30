package toys;

public class Doll extends Toy{
    static int productCode = 200;
    Color hairColor;
    int age;
    String catchphrase;
    protected Doll(int productCde, String name, Color hairColor, int age, String catchphrase){
        super(productCde, name);
        this.hairColor = hairColor;
        this.age = age;
        this.catchphrase = catchphrase;

    }
    protected Doll(String name, Color hairColor, int age, String catchphrase){
        super(productCode, name);
        this.productCode++;
        this.hairColor = hairColor;
        this.age = age;
        this.catchphrase = catchphrase;

    }
    public Color getHairColor(){
        return this.hairColor;
    }

    public int getAge(){
        return this.age;
    }

    public String getSpeak(){
        return this.catchphrase;
    }

    @Override
    public String toString(){return super.toString() + ", Doll{HC:" + getHairColor() + ", A:" + getAge() + ", S:" + getSpeak() + "}";}


    @Override
    protected void specialPlay(int time) {
        System.out.println("\t"+getName()+" brushes their "+ getHairColor()+" hair and says, \"" + getSpeak() + '"');
        increaseWear(age);
    }
}
