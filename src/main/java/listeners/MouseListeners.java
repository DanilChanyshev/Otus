package listeners;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class MouseListeners implements WebDriverListener {

  private final WebDriver driver;

  public MouseListeners(WebDriver driver) {
    this.driver = driver;
  }

  @Override
  public void beforeClick(WebElement element) {
    if (driver instanceof JavascriptExecutor js) {
      js.executeScript("""
                      arguments[0].style.border='3px solid red';
                      arguments[0].style.backgroundColor='yellow';
                      """,
                      element
      );

      js.executeScript("""
                      setTimeout(() => {
                        arguments[0].style.border = '';
                        arguments[0].style.backgroundColor = '';
                      }, 1000);
                      """,
              element
      );
    }
  }

  @Override
  public void afterClick(WebElement element) {
  }
}
