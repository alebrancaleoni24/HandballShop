<%@page session = "false"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page import="java.util.ArrayList"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    ArrayList<String> categorie = (ArrayList<String>) request.getAttribute("categorie");
    int numCat;
    if(categorie == null){
        numCat = 0;
    }else{
        numCat = categorie.size();
    }
    
    ArrayList<String> marche = (ArrayList<String>) request.getAttribute("marche");
    int numMarche;
    if(marche == null){
        numMarche = 0;
    }else{
        numMarche = marche.size();
    }
    
    ArrayList<String> generi = (ArrayList<String>) request.getAttribute("generi");
    int numGeneri;
    if(generi == null){
        numGeneri = 0;
    }else{
        numGeneri = generi.size();
    }
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Vista Prodotto";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            var logged = <%=loggedOn%>;
    
            function searchProdoctByStringSubmit(){
                var f = document.searchProdoctByString;
                f.submit();
                return;
            }
            
            function categoriaFormSubmit(index){
                var f = document.forms["categoriaForm" + index];
                f.submit();
                return;
            }
            
            function marcheFormSubmit(index){
                var f = document.forms["marcheForm" + index];
                f.submit();
                return;
            }
            
            function genereFormSubmit(index, genere){
                var f = document.forms["genereForm" + index];
                f.submit();
                return;
            }
            
            function carrelloSubmit(codiceProdotto){
                var f = document.carrelloForm;
                if(logged){
                    f.codiceProdotto.value = codiceProdotto;
                    //alert("Prodotto inserito nel carrello");
                    f.submit();
                }else{
                    alert("Per inserire un prodotto nel carrello bisogna eseguire l'accesso");
                }
                return;
            }
        </script>
        
        <style>
            
            #ordina{
                padding: 5px 10px;
                background-color: #228b22;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
                width: 100%;
            }

            table{
                width: 100%;
            }

            td{
                width: 50%;
                padding: 1%;
            }

            tr{
                height: 3%;
            }
        </style>
        
        <%@include file="/include/htmlHead.inc" %>
    </head>
    <body>
        <header>
            <%@include file="/include/headerUtente.inc" %>
        </header>
        
        <main>
            
            <!--BARRA LATERALE PER LA RICERCA DEI PRODOTTI-->
            <aside class="left_content">
                
                <section>
                    <h1>CATEGORIE</h1>
                    <ul>
                        <%for(i = 0; i < numCat; i++){%>
                            <li>
                                <form name="categoriaForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="categoria"/>
                                        <input type="hidden" name="searchName" value="<%=categorie.get(i)%>"/>
                                        <a href="javascript:categoriaFormSubmit(<%=i%>);"><%=categorie.get(i)%></a>
                                </form>
                            </li>
                        <%}%>
                    </ul>
                </section>
                   
                <section>
                    <h1>MARCHE</h1>
                    <ul>
                        <%for(i = 0; i < numMarche; i++){%>
                            <li>
                                <form name="marcheForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="marca"/>
                                        <input type="hidden" name="searchName" value="<%=marche.get(i)%>"/>
                                        <a href="javascript:marcheFormSubmit(<%=i%>);"><%=marche.get(i)%></a>
                                </form>
                            </li>
                        <%}%>
                    </ul>
                </section>
                    
                <section>
                    <h1>GENERI</h1>
                    <ul>
                        <%for(i = 0; i < numGeneri; i++){%>
                            <li>
                                <form name="genereForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="genere"/>
                                        <input type="hidden" name="searchName" value="<%=generi.get(i)%>"/>
                                        <a href="javascript:genereFormSubmit(<%=i%>);"><%=generi.get(i)%></a>
                                </form>
                            </li>
                        <%}%>
                    </ul>
                </section>
                            
            </aside>

            <div class='nome'>
                <%if(loggedOn){%>
                <p id="loggedOn">Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
                <%}else{%>
                <p id="notLoggedOn">Benvenuto, fai il login per procedere all'acquisto dei tuoi prodotti</p>
                <%}%>
            </div>
                    
            <!--BARRA DI RICERCA LIBERA-->
            <section id="search">
                <div class="searchBar">
                    <form name="searchProdoctByString" action="Dispatcher" method="post">
                        <input type="text" id="searchName" name="searchName" maxlength="100" placeholder="Cerca...">
                        <input type="hidden" name="searchType" value="searchString"/>
                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                        <a href="javascript:searchProdoctByStringSubmit();" style="font-size: medium" style="font-weight: bold">Trova</a>
                    </form>
                </div>
            </section>
            
            <!--INFORMAZIONI PRODOTTO-->
            <div class="right_content">
                <div style="float: left; width: 52%; padding: 2%; text-align: center;">
                    <img id="ProdImage" src="/images/<%=prodotto.getImage()%>" width="400px" height="400px" alt="Visualizza prodotto"/>
                </div>
                
                <div style="float: left; padding: 2%; width: 40%;">
            
                    <!--FORM DI INSERIMENTO PRODOTTO NEL CARRELLO-->
                    <section>
                        <table>
                            <tr><td><span style="font-size: large;"><b><%=prodotto.getMarca()%></b></span></td></tr>
                            <tr><td><span><b><%=prodotto.getModello()%></b></span></td></tr>
                            <tr><td><span><b>€<%=prodotto.getPrezzo()%></b></span></td></tr>
                            <form name="carrelloForm" action="Dispatcher" method="post">
                                <input type="hidden" name="controllerAction" value="Catalogo.insert"/>
                                <input type="hidden" name="codiceProdotto"/>
                                <tr>
                                    <td><label for="taglia">Scegli la taglia: </label></td>
                                    <td>
                                    <select id="taglia" name="taglia">
                                        <%if(!prodotto.getCategoria().equals("Scarpe")){%>
                                        <option value="XS">XS</option>
                                        <option value="S">S</option>
                                        <option value="M">M</option>
                                        <option value="L">L</option>
                                        <option value="XL">XL</option>
                                        <option value="XXL">XXL</option>
                                        <option value="XXXL">XXXL</option>
                                        <%}else{%>
                                        <option value="39">39</option>
                                        <option value="40">40</option>
                                        <option value="41">41</option>
                                        <option value="42">42</option>
                                        <option value="43">43</option>
                                        <option value="44">44</option>
                                        <option value="45">45</option>
                                        <%}%>
                                    </select> 
                                    </td>
                                </tr>
            
                                <tr>
                                    <td><label for="quantita">Quantit&agrave: </label></td>
                                    <td><input type="number" id="quantita" name="quantita" value="1" min="1" max="30" step="1"/></td>
                                </tr>
                        </table>

                        <div style="margin-top: 70px;">
                            <a href="javascript:carrelloSubmit(<%=prodotto.getCodiceProdotto()%>);" id="ordina">
                                Aggiungi al carello
                            </a>
                        </div>
                        </form>
                    </section>
                </div>
                
                <div class="descrizione">
                    <!--DESCRIZIONE PRODOTTO-->
                    <p><b>Descrizione prodotto</b></p>
                    </br>
                    <p><%=prodotto.getDescrizione()%></p>
                </div>
            </div>
            
            <div style="clear: both;">
            </div>
            
        </main>
        
            <%@include file="/include/footer.inc" %>
            
    </body>
</html>
