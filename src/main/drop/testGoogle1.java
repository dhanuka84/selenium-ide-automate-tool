package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class Test {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test() throws Exception {
    
    
    
    
 // @pageobject(name='GooglePO1' )  
	  driver.get(baseUrl + "");
    // @function(name='login1' )
    String nameParam1 = "dhanuka";
    String nameParam3 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam1);
    driver.findElement(By.name("btnG")).click();

    // @endFunction(name='login1')
    
 // @function(name='loginM',manualClass='GooglePOManual')
    String nameParam6 = "dhanuka";
 // @endFunction(name='loginM')
    
 // @function(name='login3' )
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.name("btnG")).click();

    // @endFunction(name='login3')
    // @endPage
    
 
    driver.get(baseUrl + "");
    
 // @pageobject(name='GooglePO2' )
    // @function(name='login2' )
    String nameParam4 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam4);
    driver.findElement(By.name("btnG")).click();
    
 
    // @endFunction(name='login2')
 // @endPage
    
 // @pageobject(name='GooglePO2' )
    // @function(name='login2' )
    String nameParam2 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam2);
    driver.findElement(By.name("btnG")).click();
    
 
    // @endFunction(name='login2')
 // @endPage
  }
  
  @Test
  public void test2() throws Exception {
    
    driver.get(baseUrl + "");
    
    
 // @pageobject(name='GooglePO1' )   
    // @function(name='login1' )
    String nameParam1 = "dhanuka";
    String nameParam3 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam1);
    driver.findElement(By.name("btnG")).click();

    // @endFunction(name='login1')
    
 // @function(name='login3' )
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.name("btnG")).click();

    // @endFunction(name='login3')
    
 // @function(name='login4' )
    String nameParam4 = "dhanuka";
    String nameParam5 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam4);
    driver.findElement(By.name("btnG")).click();

    // @endFunction(name='login4')
    
 // @function(name='login1' )
    String nameParam6 = "dhanuka";
    String nameParam7 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam4);
    driver.findElement(By.name("btnG")).click();

    // @endFunction(name='login1')
    
    // @endPage
    
 
    driver.get(baseUrl + "");
    
 // @pageobject(name='GooglePO2' )
    // @function(name='login2' )
    String nameParam2 = "dhanuka";
    
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam2);
    driver.findElement(By.name("btnG")).click();
    
 
    // @endFunction(name='login2')
 // @endPage
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}