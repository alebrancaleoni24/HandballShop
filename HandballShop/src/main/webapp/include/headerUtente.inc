<header class="clearfix"><!-- Defining the header section of the page -->

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="LogOn.logout"/>
    </form>
    
    <div id="navigation">
        <nav class="clearfix">
            <div class="logo">
                <a href="Dispatcher?controllerAction=Catalogo.view">
                    <img id="LogoWeb" src="/images/HandballVectorialWeb.png" height="35%" width="30%" alt="Torna alla Home Page"/>
                </a>
            </div>
            <div class="barra">
                <%if(loggedOn) {/*Se non sono loggato faccio vedere i tatsi carrello, ordini e logout*/%>
                    <div class="menu">
                        <a id= "carrello" href="Dispatcher?controllerAction=Acquisto.viewCarrello">Carrello</a>
                    </div>
                    <div class="menu">
                        <a id= "ordini" href="Dispatcher?controllerAction=Ordini.view">Ordini</a>
                    </div>
                    <div class="menu">
                        <a id= "logout" href="javascript:logoutForm.submit()">Logout</a>
                    </div>
                <%}else{%>
                    <div class="menu">
                        <a id= "login" href="Dispatcher?controllerAction=LogOn.view&opzione=L">Login</a>
                    </div>
                    <div class="menu">
                        <a id= "registrati" href="Dispatcher?controllerAction=LogOn.view&opzione=R">Registrati</a>
                    </div>
                <%}%>
            </div>
        </nav>
    </div>
    
</header>
