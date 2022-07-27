package isa.handballshop.model.valueObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Ordine {
    
    private String stato, nazione, città;
    private String via, numeroCivico;
    private java.util.Date dataOrdine, dataConsegna;
    private int CAP;
    private long codiceOrd;
    
    /*Mappo le relazioni*/
    private Pagamento pag;
    private Utente utente;
    private ArrayList<Contiene> con;
    
    public long getCodiceOrdine(){
        return codiceOrd;
    }
    
    public void setCodiceOrdine(long c){
        codiceOrd=c;
    }
    
    public java.util.Date getDataOrdine(){
        return dataOrdine;
    }
    
    public void setDataOrdine(java.util.Date dataOrdine){
        this.dataOrdine=dataOrdine;
    }
    
    public String getDataOrdineString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataOrdine);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataOrdine.toString() + "\"");
            return null;
        }
    }
    
    public String getStato(){
        return stato;
    }
    
    public void setStato(String s){
        stato=s;
    }
    
    public java.util.Date getDataConsegna(){
        return dataConsegna;
    }
    
    public void setDataConsegna(java.util.Date dataConsegna){
        this.dataConsegna=dataConsegna;
    }
    
    public String getDataConsegnaString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataConsegna);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataConsegna.toString() + "\"");
            return null;
        }
    }
    
    public String getNazione(){
        return nazione;
    }
    
    public void setNazione(String n){
        nazione=n;
    }
    
    public String getCittà(){
        return città;
    }
    
    public void setCittà(String c){
        città=c;
    }
    
    public String getVia(){
        return via;
    }
    
    public void setVia(String v){
        via=v;
    }
    
    public String getNumeroCivico(){
        return numeroCivico;
    }
    
    public void setNumeroCivico(String n){
        numeroCivico=n;
    }
    
    public int getCAP(){
        return CAP;
    }
    
    public void setCAP(int cap){
        CAP=cap;
    }
    
    public Pagamento getPagamento(){
        return pag;
    }
    
    public void setPagamento(Pagamento p){
        pag=p;
    }
    
    public Utente getUtente(){
        return utente;
    }
    
    public void setUtente(Utente u){
        utente=u;
    }
    
    /*Ritorna l'intera lista*/
    public ArrayList<Contiene> getContiene() {
        return con;
    }

    /*Setta l'intera lista*/
    public void setContiene(ArrayList<Contiene> con) {
       this.con = con;
    }
}