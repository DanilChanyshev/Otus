package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import support.GuiceScoped;

public abstract class AbsCommons {

  protected WebDriver driver;
  protected Actions actions;

  public AbsCommons(GuiceScoped guiceScoped) {
    this.driver = guiceScoped.getDriver();
    PageFactory.initElements(driver, this);
    this.actions = new Actions(driver);
  }
}
