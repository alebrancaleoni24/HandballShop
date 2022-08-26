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
                width: 70%; 
                margin-left: 15%;
            }

            table{
                width: 100%;
            }

            #sx{
                width: 38%;
                padding-bottom: 1%;
            }

            #dx{
                width: 62%;
                padding-bottom: 1%;
            }
        </style>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <div id="page-container">
            <header>
                <%@include file="/include/headerAdmin.inc"%>
            </header>
            
            <main>
                <div class="nome" style="margin-bottom: 15px;">
                    <p id="loggedOn">Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
                </div>

                <div style="clear: both"></div>

                <div style="width: 80%; margin-left: 10%">
            
                    <div class="content">
                        <%if(action.equals("modify")){%>
                            <div style="margin-top: 1%; margin-bottom: 3%;">
                                <h1>Modifica Prodotto</h1>
                            </div>
                        <%}else{%>
                            <div style="margin-top: 1%; margin-bottom: 3%;">
                                <h1>Aggiungi un prodotto</h1>
                            </div>
                        <%}%>
                    
                        <!--FORM PER L'INSERIMENTO O LA MODIFICA DI UN PRODOTTO-->
                        <form name="inserisciProd" action="Dispatcher" method="post">

                            <table>
                                <tr>
                                    <td id="sx"><label for="modello">Modello </label></td>
                                    <td id="dx"><input type="text" id="modello" name="modello" class="log" value="<%=(action.equals("modify")) ? prodotto.getModello() : ""%>" required maxlength="20"/></td>
                                </tr>
                                <tr>
                                    <td id="sx"><label for="categoria">Categoria </label></td>
                                    <td id="dx"><input type="text" id="categoria" name="categoria" class="log" value="<%=(action.equals("modify")) ? prodotto.getCategoria() : ""%>" required maxlength="20"/></td>
                                </tr>
                                <tr>
                                    <td id="sx"><label for="marca">Marca </label></td>
                                    <td id="dx"><input type="text" id="marca" name="marca" class="log" value="<%=(action.equals("modify")) ? prodotto.getMarca() : ""%>" required maxlength="20"/></td>
                                </tr>
                                <tr>
                                    <td id="sx"><label for="prezzo">Prezzo </label></td>
                                    <td id="dx"><input type="text" id="prezzo" name="prezzo" class="log" value="<%=(action.equals("modify")) ? prodotto.getPrezzo() : ""%>" required maxlength="20"/></td>
                                </tr>
                                
                                
                                <%if(action.equals("modify")){%>
                                    <tr>
                                        <td id="sx"><label for="genere">Genere </label></td>
                                        <td id="dx">
                                            <select id="genere" name="genere" class="log">
                                                <option value="Bambina" <%if(prodotto.getGenere().equals("Bambina")) {%>selected="selected"<%}%>>Bambina</option>
                                                <option value="Bambino" <%if(prodotto.getGenere().equals("Bambino")) {%>selected="selected"<%}%>>Bambino</option>
                                                <option value="Donna" <%if(prodotto.getGenere().equals("Donna")) {%>selected="selected"<%}%>>Donna</option>
                                                <option value="Uomo" <%if(prodotto.getGenere().equals("Uomo")) {%>selected="selected"<%}%>>Uomo</option>
                                            </select>
                                        </td>
                                    </tr>
                                <%}else{%>
                                    <tr>
                                        <td id="sx"><label for="genere">Genere </label></td>
                                        <td id="dx">
                                            <select id="genere" name="genere" class="log">
                                                <option value="Bambina">Bambina</option>
                                                <option value="Bambino">Bambino</option>
                                                <option value="Donna">Donna</option>
                                                <option value="Uomo">Uomo</option>
                                            </select>
                                        </td>
                                    </tr>
                                <%}%>
                                <tr>
                                    <td id="sx"><label for="immagine">Nome immagine </label></td>
                                    <td id="dx"><input type="text" id="immagine" name="immagine" class="log" value="<%=(action.equals("modify")) ? prodotto.getImage() : ""%>" required maxlength="100"/></td>
                                </tr>

                                <tr>
                                    <td id="sx"><label>Push </label></td>
                                    <td id="dx">
                                        <div style="float: left; width: 50%">
                                            <input type="radio" name="push" value="S"> S&iacute 
                                        </div>
                                        <div style="float: left; width: 50%">
                                            <input type="radio" name="push" value="N" checked> No
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <td id="sx"><label>Blocked </label></td>
                                    <td id="dx">
                                        <div style="float: left; width: 50%">
                                            <input type="radio" name="blocked" value="S"> S&iacute 
                                        </div>
                                        <div style="float: left; width: 50%">
                                            <input type="radio" name="blocked" value="N" checked> No
                                        </div>
                                    </td>
                                </tr>

                            </table>
                            
                            <div style="clear: both"></div>
                            
                            <div>
                                <label for="descrizione">Descrizione </label>
                                <textarea id="descrizione" name="descrizione" class="log" style="margin-top: 1%; margin-bottom: 1%; line-height: 1.5; font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;" cols="100" rows="10" wrap="soft" required><%=(action.equals("modify")) ? prodotto.getDescrizione() : ""%></textarea>
                            </div>
                            
                            <input type="hidden" name="controllerAction"/>
                            <%if(action.equals("modify")){%>
                                <input type="hidden" name="codiceProdotto" value="<%=prodotto.getCodiceProdotto()%>"/>
                            <%}%>
                            
                            <!--BOTTONI DI CONFERMA O DI ANNULLA-->
                            <div style="margin-bottom: 1%;">
                                <input type="button" name="submitButton" value="Ok" class="button">
                                <input type="button" name="backButton" value="Annulla" class="button">
                            </div>
                            
                        </form>

                        <!--FORM PER ANNULLARE-->
                        <form name="backForm" method="post" action="Dispatcher"> 
                            <input type="hidden" name="controllerAction"/>
                        </form>
                    </div>
                </div>

            </main>

            <div id="push"></div>

        </div>

        <%@include file="/include/footer.inc" %>

    </body>
</html>