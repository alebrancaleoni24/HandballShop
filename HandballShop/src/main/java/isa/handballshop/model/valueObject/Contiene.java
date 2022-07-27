package isa.handballshop.model.valueObject;

public class Contiene {
    
    /*Entità di relazione M:N tra Prodotto e Ordine*/
    
    private long quantità;
    
    /*Mappo le relazioni con Ordine e Prodotto*/
    private Ordine ordine;
    private Prodotto prodotto;
    private Taglia taglia;
    
    public long getQuantità(){
        return quantità;
    }
    
    public void setQuantità(long q){
        quantità=q;
    }
    
    public Taglia getTaglia(){
        return taglia;
    }
    
    public void setTaglia(Taglia t){
        taglia=t;
    }
    
    public Ordine getOrdine(){
        return ordine;
    }
    
    public void setOrdine(Ordine ordine){
        this.ordine=ordine;
    }
    
    public Prodotto getProdotto(){
        return prodotto;
    }
    
    public void setProdotto(Prodotto prodotto){
        this.prodotto=prodotto;
    }
}