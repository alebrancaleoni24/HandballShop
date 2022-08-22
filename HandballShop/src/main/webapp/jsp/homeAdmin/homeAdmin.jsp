<%@page session = "false"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Home amministratore";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function utentiManagementSubmit(){
                var f = document.utentiManagement;
                f.submit();
            }
            
            function ordiniSubmit(){
                var f = document.ordini;
                f.submit();
            }
            
            function magazzinoSubmit(){
                var f = document.magazzino;
                f.submit();
            }
            
        </script>
        <style>
            .content{
                margin-top: 5%;
                margin-left: 5%;
                width: 90%;
            }
            
            .image{
                width: 33%;
                float: left;
                text-align: center;
            }
            
            #linkImage{
                width: 220px;
                height: 220px;
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
                <div class='nome'>
                    <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
                </div>

                <div style="clear: both"></div>
                
                <div class="content">
                    
                    <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE ORDINI-->
                    <div class="image">
                        <form name="ordini" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="Ordini.view"/>
                            <a href="javascript:ordiniSubmit();">
                                <img id="linkImage" src="/images/Ordini.png" alt="Ordini"/></br><h3><b>Gestione ordini</b></h3>
                            </a>
                        </form>
                    </div>
                    
                    <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE UTENTI-->
                    <div class="image">
                        <form name="utentiManagement" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="UtentiManagement.view"/>
                            <a href="javascript:utentiManagementSubmit();">
                                <img id="linkImage" src="/images/Utenti.png" alt="Utenti"/></br><h3><b>Gestione utenti</b></h3>
                            </a>
                        </form>
                    </div>
                    
                    <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE PRODOTTI-->
                    <div class="image">
                        <form name="magazzino" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="ProdottoManagement.view"/>
                            <a id="magazzino" href="javascript:magazzinoSubmit();">
                                <img id="linkImage" src="/images/Magazzino.png" alt="Magazzino"/></br><h3><b>Gestione magazzino</b></h3>
                            </a>
                        </form>
                    </div>
                    
                </div>
                
            </main>

            <div id="push"></div>

        </div>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>