<%@page session="false" %>
<!DOCTYPE HTML>
<html lang="it-IT">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="refresh" content="0; url=/Handball_Shop/Dispatcher">

        <script type="text/javascript">
            /*A pagina caricata chiamo la classe Dispatcher*/
            function onLoadHandler() {
                window.location.href = "/Handball_Shop/Dispatcher";
            }

            window.addEventListener("load", onLoadHandler);
        </script>

        <title>Page Redirection</title>
    </head>
    <body>
        If you are not redirected automatically, follow the <a href='/Handball_Shop/Dispatcher'>link</a>
    </body>
</html>