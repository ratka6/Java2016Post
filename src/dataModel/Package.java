package dataModel;

/**
 * Created by krzysiek on 18.01.2017.
 */
public class Package {
    Double height;
    Double width;
    Double length;
    Double weight;
    Double cost;
    String sourceAddress;
    String destinationAddres;
    String date;

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public void setDestinationAddres(String destinationAddres) {
        this.destinationAddres = destinationAddres;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWidth() {
        return width;
    }

    public Double getLength() {
        return length;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getCost() {
        return cost;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddres() {
        return destinationAddres;
    }

    public String getDate() {
        return date;
    }

    public Package(Double height, Double width, Double length, Double weight, Double cost) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.weight = weight;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Package{" +
                "height=" + height +
                ", width=" + width +
                ", length=" + length +
                ", weight=" + weight +
                ", cost=" + cost +
                ", sourceAddress='" + sourceAddress + '\'' +
                ", destinationAddres='" + destinationAddres + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
