package factory;

import exceptions.BrowserNotSupported;
import factory.settings.ChromeSettings;
import listeners.MouseListeners;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class WebDriverFactory {

  public WebDriver create(String browser) {
    if (browser == null) {
      browser = System.getProperty("browser.name", "chrome").toLowerCase();
    }
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
