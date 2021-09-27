package com.sapiensacademytest.firstsuite;

import java.awt.List;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


public class CCartItems {

	public static void main(String[] args) {
		  
         // Variables del caso de prueba
         
        int cantidadLista;
        int numeroAlAzar;
        List <Integer> productosAlAzar = new ArrayList<Integer>();
        double totalProductos = 0;
        String mensajeDeVentana = new String();
        String mensajeCarrito = new String();        
        
        
        //C001 AÑADIR ARTICULOS AL CARRO
        System.setProperty("webdriver.chrome.driver", "D:\\driversParaNavegadores\\chromedriver.exe");
        WebDriver customDriver = new ChromeDriver();
        
        //Espera
        WebDriverWait espera = new WebDriverWait(customDriver, 30);
        
        customDriver.get("http://automationpractice.com/index.php");
        Dimension driverSize = new Dimension(1170,700);
        customDriver.manage().window().setSize(driverSize);
        Assert.assertEquals(customDriver.getTitle(), "My Store");
        WebElement contenedorProductos = customDriver.findElement(By.id("homefeatured"));
        List<WebElement> listaDeProductos = contenedorProductos.findElements(By.tagName("li"));
        System.out.println("Este es el tamaño de la lista: " + listaDeProductos.size());
        
        if (listaDeProductos.size()<3) {
            cantidadLista = listaDeProductos.size();
        } else {
            cantidadLista = 2;
        }
        for (int i=0; i<cantidadLista; i++) {
            numeroAlAzar = (int) (Math.random()* listaDeProductos.size() + 1);
            if (productosAlAzar.contains(numeroAlAzar)) {
                i--;
            } else {
                productosAlAzar.add(numeroAlAzar);
            }
        }
        
        productosAlAzar.forEach(System.out::println);
        
        for (int i=0; i<cantidadLista; i++) {
            customDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            String elementXpath = String.format("//*[@id=\"homefeatured\"]/li[%s]/div/div[2]/div[1]/span", productosAlAzar.get(i));
            String precioUnitarioDolar = listaDeProductos.get(productosAlAzar.get(i)-1).findElement(By.xpath(elementXpath)).getText();
            String precioUnitario = precioUnitarioDolar.replace("$", "");
            Double precio = Double.valueOf(precioUnitario);
            System.out.println("Este es el valor unitario del producto: " + precio);
            totalProductos += precio;
            System.out.println("Este es el total de los valores: " + totalProductos);
            listaDeProductos.get(productosAlAzar.get(i)-1).findElement(By.linkText("Add to cart")).click();
            if (i==0) {
                mensajeDeVentana = "There is 1 item in your cart.";
                mensajeCarrito = "Cart 1 Product";
            } else {
                mensajeDeVentana = String.format("There are %s items in your cart.", i+1);
                mensajeCarrito = String.format("Cart %s Products", i+1);                
            }
            WebElement ventanaDeProducto = espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[1]/h2")));
            Assert.assertEquals(customDriver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[1]/h2")).getText(), "Product successfully added to your shopping cart");
            
            Assert.assertEquals(customDriver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/h2")).getText() , mensajeDeVentana);
            Assert.assertEquals(customDriver.findElement(By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/a")).getText() , mensajeCarrito);
            customDriver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/span")).click();
            Boolean ventanaDesaparecida = espera.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[1]/h2")));
        }
        
        //Verficaciones
        Actions mouseAction = new Actions(customDriver);
        WebElement cartOptions = customDriver.findElement(By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/a"));
        mouseAction.moveToElement(cartOptions).perform();
        totalProductos += 2.00;
        //Dos cifras
        DecimalFormat df2 = new DecimalFormat("#.00");
        
        String totalEnString = String.format("$%s", totalProductos);
        totalEnString = totalEnString.replace("," , ".");
        
        WebElement ventanaTotal = espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/div/div/div/div/div[2]/span[1]")));
        Assert.assertEquals(customDriver.findElement(By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/div/div/div/div/div[2]/span[1]")).getText(), totalEnString);
        WebElement botoneckout = espera.until(ExpectedConditions.visibilityOfElementLocated(By.id("button_order_cart")));
        //customDriver.close();
		
        
        //CASO 2
        customDriver.findElement(By.id("button_order_cart")).click();
        //Verificación 
        Assert.assertEquals(customDriver.getTitle(), "Order - My Store");
        Assert.assertEquals(customDriver.findElement(By.className("page-heading")).getText().substring(0,21), "SHOOPING-CART SUMMARY");
        mensajeCarrito = mensajeCarrito.replace("Cart", "Your shooping cart contains:");
        Assert.assertEquals(customDriver.findElement(By.className("heading-counter")).getText(), mensajeCarrito);
        Assert.assertEquals(customDriver.findElement(By.xpath("//*[@id=\"total_price\"]")).getText(), totalEnString);
        customDriver.findElement(By.xpath("//*[@id=\"center_column\"]/p[2]/a[1]")).click();
        
		
	}

}
