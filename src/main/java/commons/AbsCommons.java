package commons;

import com.google.inject.Guice;
import modules.PagesModule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public abstract class AbsCommons {

  protected WebDriver driver;
  protected Actions actions;

  public AbsCommons(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
    this.actions = new Actions(driver);
    Guice.createInjector(new PagesModule(driver)).injectMembers(this);
  }
}
