package support;

import factory.WebDriverFactory;
import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.WebDriver;
import java.util.HashMap;
import java.util.Map;

@ScenarioScoped
public class GuiceScoped {

  private WebDriver driver;
  private Map<String, Object> store = new HashMap<>();

  public WebDriver getDriver() {
    if (driver == null){
      driver = new WebDriverFactory().create(null);
    }
    return driver;
  }

  public WebDriver getDriver(String browser) {
    if (driver == null){
      driver = getBrowser(browser);
    }
    return driver;
  }

  public WebDriver getBrowser(String browserName){
    WebDriver driver = new WebDriverFactory().create(browserName);
    return driver;
  }

  public <T> void setStore(String key, T object){
    store.put(key, object);
  }

  public <T> T getStore(String key){
    return (T)store.get(key);
  }
}
