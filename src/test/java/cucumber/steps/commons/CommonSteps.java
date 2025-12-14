package cucumber.steps.commons;

import com.google.inject.Inject;
import io.cucumber.java.ru.Дано;
import support.GuiceScoped;

public class CommonSteps {

  @Inject
  private GuiceScoped guiceScoped;

  private String baseUrl = System.getProperty("base.url");

  @Дано("Открыть браузер")
  public void openDefaultBrowser(){
    guiceScoped.getDriver().get(baseUrl);
  }

  @Дано("^Открыть браузер (.*)$")
  public void openBrowser(String browserName){
    guiceScoped.getDriver(browserName).get(baseUrl);
  }
}
