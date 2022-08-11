<%@page session = "false"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
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
    
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    int numProd;
    if(prodotti == null){
        numProd = 0;
    }else{
        numProd = prodotti.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Catalogo";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function prodottoFormSubmit(index, codice){
                var f = document.forms["prodottoForm" + index];
                f.codiceProdotto.value = codice;
                f.submit();
                return;
            }
            
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
        </script>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        <header>
            <%@include file="/include/headerUtente.inc" %>
        </header>
        
        <hr>
        
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
                
            <div style="clear: right;"></div>
            
            <!--LISTA PRODOTTI DA VISUALIZZARE-->
            <%if(numProd == 0){%>
            <div style='margin-top: 20px; margin-left: 163px;'>
                <h2>Attenzione</h2>
                <p>La ricerca effettuata non ha prodotto risultati. Per visionare il catalogo degli articoli naviga su <a href="Dispatcher?controllerAction=Catalogo.view">Handball Shop</a> e, quando trovi un articolo che ti interessa, clicca su "Aggiungi al carrello"</p>
            </div>
            <%}else{%>
            <section class="clearfix">
                <div class="right_content">
                    <%for (i = 0; i < numProd; i++) {%>           
                        <article>
                            <div class="prodotto">
                                <form name="prodottoForm<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="Catalogo.viewProd"/>
                                    <input type="hidden" name="codiceProdotto"/>
                                    <a id="<%=i%>" href="javascript:prodottoFormSubmit(<%=i%>, <%=prodotti.get(i).getCodiceProdotto()%>);">
                                        <img src="/images/<%=prodotti.get(i).getImage()%>" width="220px" height="220px" alt="Visualizza prodotto"/>
                                    </a>
                                </form>
                                <span style="font-size: medium; line-height: 1.5;"><b><%=prodotti.get(i).getMarca()%></b></span>
                                <br/>
                                <span style="font-size: small; line-height: 1.5;"><%=prodotti.get(i).getModello()%></span>
                                <br/>
                                <span style="font-size: small; line-height: 1.5;">€<%=prodotti.get(i).getPrezzo()%></span>
                            </div>
                        </article>
                    <%}%>
                </div>
            </section>
            <%}%>
               
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
    
</html>
