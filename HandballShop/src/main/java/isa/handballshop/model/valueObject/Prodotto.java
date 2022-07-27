package isa.handballshop.model.valueObject;

public class Prodotto {
    
    private String categoria, marca, modello;
    private String genere, img, descrizione;
    private long codiceProd;
    private float prezzo;
    private boolean blocked, push;
    
    private Contiene[] con;
    
    public long getCodiceProdotto(){
        return codiceProd;
    }
    
    public void setCodiceProdotto(long c){
        codiceProd=c;
    }
    
    public String getCategoria(){
        return categoria;
    }
    
    public void setCategoria(String c){
        categoria=c;
    }
    
    public String getMarca(){
        return marca;
    }
    
    public void setMarca(String m){
        marca=m;
    }
    
    public float getPrezzo(){
        return prezzo;
    }
    
    public void setPrezzo(float p){
        prezzo=p;
    }
    
    public String getModello(){
        return modello;
    }
    
    public void setModello(String m){
        modello=m;
    }
    
    public String getGenere(){
        return genere;
    }
    
    public void setGenere(String g){
        genere=g;
    }
    
    public String getImage(){
        return img;
    }
    
    public void setImage(String i){
        img=i;
    }
    
    public String getDescrizione(){
        return descrizione;
    }
    
    public void setDescrizione(String d){
        descrizione=d;
    }
    
    public boolean isBlocked(){
        return blocked;
    }
    
    public void setBlocked(boolean b){
        blocked=b;
    }
    
    public boolean isPush(){
        return push;
    }
    
    public void setPush(boolean p){
        push=p;
    }
    
    /*Ritorna l'intero array*/
    public Contiene[] getContiene() {
        return con;
    }

    /*Setta l'intero array*/
    public void setContiene(Contiene[] con) {
       this.con = con;
    }
    
    /*Ritorna l'elemento della posizione specificata*/
    public Contiene getContiene(int index) {
        return this.con[index];
    }
  
    /*Setta la posizione specificata con l'elemento specificato*/
    public void setContiene(int index, Contiene con) {
        this.con[index] = con;
    }
}