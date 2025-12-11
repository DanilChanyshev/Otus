package waiters;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.function.Supplier;

public class Waiter {

  private final WebDriver driver;

  public Waiter(WebDriver driver) {
    this.driver = driver;
  }

  public static WebElement waitWebElement(Supplier<WebElement> condition, Duration timeout) {
    return wait(condition, timeout, Duration.ofMillis(500));
  }

  public static WebElement wait(Supplier<WebElement> condition, Duration timeout, Duration pollingInterval) {
    long startTime = System.currentTimeMillis();
    long timeoutMs = timeout.toMillis();
    long pollingMs = pollingInterval.toMillis();

    while (System.currentTimeMillis() - startTime < timeoutMs) {
      try {
        WebElement result = condition.get();
        if (result != null) {
          return result;
        }
      } catch (RuntimeException e) {
        //Элемент не найден
      }
      try {
        Thread.sleep(pollingMs);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Ожидание прервано", ie);
      }
    }
    return condition.get();
  }

  public boolean waitForCondition(ExpectedCondition condition, int time) {
    try {
      new WebDriverWait(driver, Duration.ofSeconds(time)).until(condition);
      return true;
    } catch (TimeoutException ignored) {
      return false;
    }
  }
}
