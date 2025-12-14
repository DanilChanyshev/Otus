package cucumber.hooks;

import com.google.inject.Inject;
import io.cucumber.java.After;
import support.GuiceScoped;

public class Hooks {

  @Inject
  private GuiceScoped guiceScoped;

  @After
  public void afterScenario(){
    if (guiceScoped.getDriver() != null){
      guiceScoped.getDriver().quit();
    }
  }
}
