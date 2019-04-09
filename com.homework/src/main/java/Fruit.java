import java.time.LocalDate;

public class Fruit {
    private TypeOfFruits type;
    private int shelfLife;
    private LocalDate dateOfDelivery;
    private double price;

    public Fruit(TypeOfFruits type, int shelfLife, LocalDate dateOfDelivery, double price) {
        this.type = type;
        this.shelfLife = shelfLife;
        this.dateOfDelivery = dateOfDelivery;
        this.price = price;
    }

    public Fruit() {
    }

    public TypeOfFruits getType() {
        return type;
    }

    public void setType(TypeOfFruits type) {
        this.type = type;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public LocalDate getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(LocalDate dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDateOfSpoiling() {
        return dateOfDelivery.plusDays(shelfLife);
    }
}
