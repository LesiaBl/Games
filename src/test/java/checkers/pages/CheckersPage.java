package checkers.pages;

import checkers.utils.Driver;
import checkers.utils.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CheckersPage  {

    public CheckersPage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id="message")
    public WebElement message;

    @FindBy(css = "p.footnote > a:first-of-type")
    public WebElement restart;

    @FindBy(css="img[src='me1.gif']")
    public List<WebElement> bluePieces;


    public void makeMove(int[] from, int[] to){


        WebElement fromSpace = Driver.getDriver().findElement(By.name("space" + from[0] + from[1]));
        WebElement toSpace = Driver.getDriver().findElement(By.name("space" + to[0] + to[1]));

        fromSpace.click();
        toSpace.click();

        Waiter.pause(3);
    }

}
