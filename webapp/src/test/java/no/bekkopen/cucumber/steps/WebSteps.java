package no.bekkopen.cucumber.steps;

import cuke4duke.annotation.I18n;
import cuke4duke.spring.StepDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertTrue;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
@StepDefinitions
public class WebSteps {
    private WebDriver driver;
    private WebElement element;

    public WebSteps() {
        this.driver = new HtmlUnitDriver();
    }

    @I18n.EN.Given("^I have accessed Google's home page$")
    public void iHaveAccessedGooglesHomePage() {
        // And now use this to visit Google
        driver.get("http://www.google.com");
    }


    @I18n.EN.When("^I enter the keyword of \"(.*)\"$")
    public void iEnterTheKeyword(String keyword) {
        // Find the text input element by its name
        element = driver.findElement(By.name("q"));
        // Enter something to search for
        element.sendKeys(keyword);
    }


    @I18n.EN.When("^click the Submit button$")
    public void clickTheSubmitButton() {
        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();
    }


    @I18n.EN.Then("^the page title returned should contain \"(.*)\"$")
    public void thePageTitleReturned(String expectedResults) {
        assertTrue(driver.getTitle().contains(expectedResults));
    }
}
