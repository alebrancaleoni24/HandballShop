package isa.handballshop.model.valueObject;

public class Taglia {
    
    private long codiceProd, quantità;
    private String taglia;

    public long getCodiceProd() {
        return codiceProd;
    }

    public void setCodiceProd(long codiceProd) {
        this.codiceProd = codiceProd;
    }

    public long getQuantità() {
        return quantità;
    }

    public void setQuantità(long quantità) {
        this.quantità = quantità;
    }

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }
    
}