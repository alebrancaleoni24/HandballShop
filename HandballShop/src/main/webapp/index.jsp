<%@page session="false" %>
<!DOCTYPE HTML>
<html lang="it-IT">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="refresh" content="0; url=/HandballShop/Dispatcher">

        <script type="text/javascript">
            /*A pagina caricata chiamo la classe Dispatcher*/
            function onLoadHandler() {
                window.location.href = "/HandballShop/Dispatcher";
            }

            window.addEventListener("load", onLoadHandler);
        </script>

        <title>Page Redirection</title>
    </head>
    <body>
        If you are not redirected automatically, follow the <a href='/HandballShop/Dispatcher'>link</a>
    </body>
</html>