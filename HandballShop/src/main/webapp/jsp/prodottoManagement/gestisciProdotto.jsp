<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Gestisci prodotto";
    
    String action=(prodotto != null) ? "modify" : "insert";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            var action = "<%=action%>";
            
            function validateAndSubmit(){
                
                var modello = document.inserisciProd.modello.value;
                var categoria = document.inserisciProd.categoria.value;
                var marca = document.inserisciProd.marca.value;
                var prezzo = document.inserisciProd.prezzo.value;
                var immagine = document.inserisciProd.immagine.value;
                var descrizione = document.inserisciProd.descrizione.value;
                
                /*CONTROLLO IL CAMPO MODELLO*/
                if ((modello == "") || (modello == "undefined")) {
                    alert("Il campo Modello è obbligatorio");
                    document.inserisciProd.modello.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO CATEGORIA*/
                if ((categoria == "") || (categoria == "undefined")) {
                    alert("Il campo Categoria è obbligatorio");
                    document.inserisciProd.categoria.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO MARCA*/
                if ((marca == "") || (marca == "undefined")) {
                    alert("Il campo Marca è obbligatorio");
                    document.inserisciProd.marca.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO PREZZO*/
                if ((prezzo == "") || (prezzo == "undefined")) {
                    alert("Il campo Prezzo è obbligatorio");
                    document.inserisciProd.prezzo.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO IMMAGINE*/
                if ((immagine == "") || (immagine == "undefined")) {
                    alert("Il campo Nome immagine è obbligatorio");
                   document.inserisciProd.immagine.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO DESCRIZIONE*/
                if ((descrizione == "") || (descrizione == "undefined")) {
                    alert("Il campo Descrizione è obbligatorio");
                    document.inserisciProd.descrizione.focus();
                    return false;
                }
                
                else{
                    if(action === "insert"){
                        document.inserisciProd.controllerAction.value = "ProdottoManagement.inserisciProdotto";
                    }else{
                        document.inserisciProd.controllerAction.value = "ProdottoManagement.modificaProdotto";
                    }
                    document.inserisciProd.submit();
                }
            }
            
            function goBack(){
                document.backForm.controllerAction.value = "ProdottoManagement.view";
                document.backForm.submit();
            }

            function mainOnLoadHandler(){
                document.inserisciProd.backButton.addEventListener("click", goBack);
                document.inserisciProd.submitButton.addEventListener("click", validateAndSubmit);
            }
        </script>
        <style>
            .content{
                width: 61%;
                margin-left: 19%;
            }
            
            #modello{
                width: 75%;
                height: 22px;
                font-size: large;
            }
            
            #categoria{
                width: 70%;
                height: 22px;
                font-size: large;
            }
            
            #marca{
                width: 79%;
                height: 22px;
                font-size: large;
            }
            
            #prezzo{
                width: 78%;
                height: 22px;
                font-size: large;
            }
            
            #genere{
                width: 60%;
                height: 24px;
                font-size: large;
            }
            
            #immagine{
                width: 69%;
                height: 22px;
                font-size: large;
            }
        </style>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <header>
            <%@include file="/include/headerAdmin.inc"%>
        </header>
        
        <hr>
        
        <main>
            
            <div class="nome" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
            </div>
            
            <div class="content">
                <%if(action.equals("modify")){%>
                    <div>
                        <h2>MODIFICA PRODOTTO</h2>
                    </div>
                <%}else{%>
                    <div>
                        <h2>AGGIUNGI UN PRODOTTO</h2>
                    </div>
                <%}%>
            
                <!--FORM PER L'INSERIMENTO O LA MODIFICA DI UN PRODOTTO-->
                <form name="inserisciProd" action="Dispatcher" method="post">

                    <div class="form" id="left" style="width: 48%;">
                        <label for="modello">Modello: </label>
                        <input type="text" id="modello" name="modello" value="<%=(action.equals("modify")) ? prodotto.getModello() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label for="categoria">Categoria: </label>
                        <input type="text" id="categoria" name="categoria" value="<%=(action.equals("modify")) ? prodotto.getCategoria() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" id="left" style="width: 48%;">
                        <label for="marca">Marca: </label>
                        <input type="text" id="marca" name="marca" value="<%=(action.equals("modify")) ? prodotto.getMarca() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label for="prezzo">Prezzo: </label>
                        <input type="text" id="prezzo" name="prezzo" value="<%=(action.equals("modify")) ? prodotto.getPrezzo() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <%if(action.equals("modify")){%>
                        <div class="form" id="left" style="width: 27%;">
                            <label for="genere">Genere: </label>
                            <select id="genere" name="genere">
                                <option value="Bambina" <%if(prodotto.getGenere().equals("Bambina")) {%>selected="selected"<%}%>>Bambina</option>
                                <option value="Bambino" <%if(prodotto.getGenere().equals("Bambino")) {%>selected="selected"<%}%>>Bambino</option>
                                <option value="Donna" <%if(prodotto.getGenere().equals("Donna")) {%>selected="selected"<%}%>>Donna</option>
                                <option value="Uomo" <%if(prodotto.getGenere().equals("Uomo")) {%>selected="selected"<%}%>>Uomo</option>
                            </select>
                        </div>
                    <%}else{%>
                        <div class="form" id="left" style="width: 27%;">
                            <label for="genere">Genere: </label>
                            <select id="genere" name="genere">
                                <option value="Bambina">Bambina</option>
                                <option value="Bambino">Bambino</option>
                                <option value="Donna">Donna</option>
                                <option value="Uomo">Uomo</option>
                            </select>
                        </div>
                    <%}%>
                    
                    <div class="form" style="float: left; width: 68%;">
                        <label for="immagine">Nome immagine: </label>
                        <input type="text" id="immagine" name="immagine" value="<%=(action.equals("modify")) ? prodotto.getImage() : ""%>" required maxlength="100"/>
                    </div>
                    
                    <div class="form" id="left" style="width: 48%;">
                        <label>Push: </label>
                        <input type="radio" name="push" value="S">S&iacute;
                        <input type="radio" name="push" value="N" checked>No
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label>Blocked: </label>
                        <input type="radio" name="blocked" value="S" >S&iacute;
                        <input type="radio" name="blocked" value="N" checked>No
                    </div>
                    
                    <div style="clear: both"></div>
                    
                    <div class="form">
                        <label for="descrizione">Descrizione: </label>
                        <textarea id="descrizione" name="descrizione" cols="100" rows="10" wrap="soft" required><%=(action.equals("modify")) ? prodotto.getDescrizione() : ""%></textarea>
                    </div> 
                    
                    <input type="hidden" name="controllerAction"/>
                    <%if(action.equals("modify")){%>
                        <input type="hidden" name="codiceProdotto" value="<%=prodotto.getCodiceProdotto()%>"/>
                    <%}%>
                    
                    <!--BOTTONI DI CONFERMA O DI ANNULLA-->
                    <input type="button" name="submitButton" value="Ok" class="button">
                    <input type="button" name="backButton" value="Annulla" class="button">
                    
                </form>
            </div>
                    
            <!--FORM PER ANNULLARE-->
            <form name="backForm" method="post" action="Dispatcher"> 
                <input type="hidden" name="controllerAction"/>
            </form>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>