package isa.handballshop.services.util;

import java.util.ArrayList;

public class Accounting{

    public static double calcoloPrezzo(ArrayList<Double> prezzi, ArrayList<Integer> quantita){
        double prezzo = 0;

        for(int i = 0; i < prezzi.size(); i++)
            prezzo += prezzi.get(i)*quantita.get(i);

        /*Arrotondo il prezzo alla seconda cifra decimale*/
        prezzo = Math.round(prezzo*100.0) / 100.0;

        return prezzo;
    }

    public static double calcoloIVA(double prezzo, int percentualeImposta){
        double iva = prezzo*percentualeImposta/100;

        /*Arrotondo l'iva alla seconda cifra decimale*/
        iva = Math.round(iva*100.0) / 100.0;

        return iva;
    }
}