package isa.handballshop.model.dao;

import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Ordine;
import isa.handballshop.model.valueObject.Pagamento;
import isa.handballshop.model.valueObject.Utente;

public interface PagamentoDAO {
    
    public Pagamento creaPagamento(String stato, String carta, java.util.Date dataRichiestaPagamento, java.util.Date
                            dataPagamento, float importo, Utente utente, Ordine ordine) throws DuplicatedObjectException;
    
    public long getUltimoCodice();
    
    /*Recupera l'importo del pagamento specificato*/
    public float getImporto(long codicePagamento);
    
}