package isa.handballshop.services.util;

public class Conversion {
    
    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
    
}