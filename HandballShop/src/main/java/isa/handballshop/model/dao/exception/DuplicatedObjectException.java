package isa.handballshop.model.dao.exception;

public class DuplicatedObjectException extends Exception{
    
    /* Eccezione pinvocata nel caso in cui si tenti di inserire nel DB un elemento già esistente */
    
    public DuplicatedObjectException(){
        super();
    }
    
    public DuplicatedObjectException(String s){
        super(s);
    }
    
}