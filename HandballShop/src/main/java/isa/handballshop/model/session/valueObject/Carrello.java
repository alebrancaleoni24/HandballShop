package isa.handballshop.model.session.valueObject;

public class Carrello {
    
    private long codiceProd, quantita;
    private String taglia;
    
    @Override
    public String toString(){
        return (String) (getCodiceProd() + " " + getQuantita() + " " + getTaglia());
    }

    public long getCodiceProd() {
        return codiceProd;
    }

    public void setCodiceProd(long codiceProd) {
        this.codiceProd = codiceProd;
    }

    public long getQuantita() {
        return quantita;
    }

    public void setQuantità(long quantita) {
        this.quantita = quantita;
    }
    
    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }

}