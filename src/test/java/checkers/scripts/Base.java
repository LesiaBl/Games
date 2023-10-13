package checkers.scripts;

import checkers.pages.CheckersPage;
import checkers.utils.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class Base {
    public static WebDriver driver;
    public static CheckersPage checkersPage;


    @BeforeMethod
    public void setUp(){
        driver = Driver.getDriver();
    }

    @AfterMethod
    public void tearDown(){
        Driver.quitDriver();
    }
}
