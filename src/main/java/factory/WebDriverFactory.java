package factory;

import exceptions.BrowserNotSupported;
import factory.settings.ChromeSettings;
import listeners.MouseListeners;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import java.util.Locale;


public class WebDriverFactory {

  private final String browser = System.getProperty("browser.name").toLowerCase(Locale.ROOT);

  public WebDriver create() {
    switch (browser) {
      case "chrome": {
        ChromeDriver originDriver = new ChromeDriver((ChromeOptions) new ChromeSettings().setting());
        MouseListeners listeners = new MouseListeners(originDriver);
        return new EventFiringDecorator<>(listeners).decorate(originDriver);
      }
    }
    throw new BrowserNotSupported(browser);
  }
}
