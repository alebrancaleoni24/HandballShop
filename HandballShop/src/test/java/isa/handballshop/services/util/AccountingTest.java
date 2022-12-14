package isa.handballshop.services.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AccountingTest{

    @Test
    public void testCalcoloPrezzo(){
        ArrayList<Double> prezzo = new ArrayList<Double>();
        ArrayList<Long> quantita = new ArrayList<Long>();

        prezzo.add((double) 3);
        prezzo.add((double) 5);

        quantita.add((long) 2);
        quantita.add((long) 4);

        double result = Accounting.calcoloPrezzo(prezzo, quantita);
        assertEquals(26, result);
    }

    @Test
    public void testCakcoloIVA(){
        double result = Accounting.calcoloIVA(100, 22);
        assertEquals(22, result);
    }

}