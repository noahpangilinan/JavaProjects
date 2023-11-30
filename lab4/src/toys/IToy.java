package toys;

public interface IToy {
    int getProductCode();
    String getName();
    int getHappiness();
    boolean isRetired();
    double getWear();
    void increaseWear(double amount);
    void play(int time);

}
