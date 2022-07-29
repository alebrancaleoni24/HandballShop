package isa.handballshop.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class ControllerTest{
    public WebDriver driver;

    /* Metodo di test per il login di un utente */
    @Test
    public void loginTest(){
        driver = new ChromeDriver();

        driver.get("/Handball_Shop/Dispatcher");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement link = driver.findElement(By.linkText("LOGIN"));
        link.click();

        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        emailBox.sendKeys("mario.rossi@gmail.com");
        passwordBox.sendKeys("mr");
        submit.click();

        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Mario Rossi", value);

        driver.quit();
    }

    /* Metodo di test per la registrazione di un utente */
    @BeforeAll
    @Test
    public void registrazioneTest(){
        driver = new ChromeDriver();

        driver.get("/Handball_Shop/Dispatcher");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement link = driver.findElement(By.linkText("REGISTRATI"));
        link.click();

        WebElement nomeBox = driver.findElement(By.id("nome"));
        WebElement cognomeBox = driver.findElement(By.id("cognome"));
        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement passwordConfBox = driver.findElement(By.id("passwordConf"));
        WebElement nazioneBox = driver.findElement(By.id("nazione"));
        WebElement cittaBox = driver.findElement(By.id("citta"));
        WebElement viaBox = driver.findElement(By.id("via"));
        WebElement numeroCivicoBox = driver.findElement(By.id("numeroCivico"));
        WebElement capBox = driver.findElement(By.id("CAP"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        nomeBox.sendKeys("Mario");
        cognomeBox.sendKeys("Rossi");
        emailBox.sendKeys("mario.rossi@gmail.com");
        passwordBox.sendKeys("mr");
        passwordConfBox.sendKeys("mr");
        nazioneBox.sendKeys("Italia");
        cittaBox.sendKeys("Roma");
        viaBox.sendKeys("via Appia");
        numeroCivicoBox.sendKeys("1");
        capBox.sendKeys("12345");
        submit.click();

        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Mario Rossi", value);

        driver.quit();
    }

    /* Metodo di test per l'acquisto di un prodotto */
    @AfterAll
    @Test
    public void AcquistoTest(){
        driver = new ChromeDriver();

        driver.get("/Handball_Shop/Dispatcher");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        /* Faccio il login con l'utente di prova Mario Rossi */
        WebElement link = driver.findElement(By.linkText("LOGIN"));
        link.click();

        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        emailBox.sendKeys("mario.rossi@gmail.com");
        passwordBox.sendKeys("mr");
        submit.click();

        /* Seleziono il prodotto da acquistare */
        WebElement prodotto = driver.findElement(By.id("0"));
        prodotto.click();

        /* Aggiungo il prodotto al carrello */
        WebElement carrello = driver.findElement(By.id("ordina"));
        carrello.click();

        /* Vado alla pagina carrello */
        WebElement carrelloLink = driver.findElement(By.id("carrello"));
        carrelloLink.click();

        /* Clicco per procedere all'ordine */
        WebElement ordineButton = driver.findElement(By.id("ordina"));
        ordineButton.click();

        /* Inserisco i dati per il pagamento e la consegna */
        WebElement nazioneBox = driver.findElement(By.id("nazione"));
        WebElement cittaBox = driver.findElement(By.id("citta"));
        WebElement viaBox = driver.findElement(By.id("via"));
        WebElement numeroCivicoBox = driver.findElement(By.id("numeroCivico"));
        WebElement capBox = driver.findElement(By.id("CAP"));
        WebElement cartaBox = driver.findElement(By.id("carta"));
        WebElement submitPagamento = driver.findElement(By.name("submitButton"));

        nazioneBox.sendKeys("Italia");
        cittaBox.sendKeys("Roma");
        viaBox.sendKeys("via Appia");
        numeroCivicoBox.sendKeys("1");
        capBox.sendKeys("12345");
        cartaBox.sendKeys("1234567890123456");
        submitPagamento.click();

        /* Do l'ok dalla pagina di riepilogo */
        WebElement submitOrdine = driver.findElement(By.name("submitButton"));
        submitOrdine.click();


        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Mario Rossi", value);

        driver.quit();
    }

    @Test
    public void inserimentoProdottoTest(){

        driver = new ChromeDriver();

        driver.get("/Handball_Shop/Dispatcher");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        /* Faccio il login con l'utente admin Alessandro Brancaleoni */
        WebElement link = driver.findElement(By.linkText("LOGIN"));
        link.click();

        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        emailBox.sendKeys("ale@gmail.com");
        passwordBox.sendKeys("ab");
        submit.click();

        /* Entro nella pagina della giacenza di magazzino */
        WebElement magazzinoLink = driver.findElement(By.id("magazzino"));
        magazzinoLink.click();

        /* Entro nella pagina per inserire un nuovo prodotto */
        WebElement inserisciProdottoButton = driver.findElement(By.id("inserisciProdotto"));
        inserisciProdottoButton.click();

        /* Inserisco i dati del nuovo prodotto */
        WebElement modelloBox = driver.findElement(By.id("modello"));
        WebElement categoriaBox = driver.findElement(By.id("categoria"));
        WebElement marcaBox = driver.findElement(By.id("marca"));
        WebElement prezzoBox = driver.findElement(By.id("prezzo"));
        Select genere = new Select(driver.findElement(By.id("genere")));
        WebElement immagineBox = driver.findElement(By.id("immagine"));
        WebElement descrizioneBox = driver.findElement(By.id("descrizione"));
        WebElement submitProdotto = driver.findElement(By.name("submitButton"));

        modelloBox.sendKeys("modello");
        categoriaBox.sendKeys("categoria");
        marcaBox.sendKeys("marca");
        prezzoBox.sendKeys("prezzo");
        genere.selectByValue("Uomo");
        immagineBox.sendKeys("immagine");
        descrizioneBox.sendKeys("descrizione");
        submitProdotto.click();

        /* Inserisco la disponibilità del nuovo prodotto */
        WebElement xs = driver.findElement(By.name("xs"));
        WebElement s = driver.findElement(By.name("s"));
        WebElement m = driver.findElement(By.name("m"));
        WebElement l = driver.findElement(By.name("l"));
        WebElement xl = driver.findElement(By.name("xl"));
        WebElement xxl = driver.findElement(By.name("xxl"));
        WebElement xxxl = driver.findElement(By.name("xxxl"));
        WebElement submitTaglie = driver.findElement(By.name("submitButton"));

        xs.sendKeys("10");
        s.sendKeys("10");
        m.sendKeys("10");
        l.sendKeys("10");
        xl.sendKeys("10");
        xxl.sendKeys("10");
        xxxl.sendKeys("10");
        submitTaglie.click();

        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Alessandro Brancaleoni", value);

        driver.quit();

    }
}