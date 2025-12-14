package waiters;

import com.google.inject.Inject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AbsBasePage;
import support.GuiceScoped;
import java.time.Duration;
import java.util.function.Supplier;

public class Waiter extends AbsBasePage<Waiter> {

  @Inject
  public Waiter(GuiceScoped guiceScoped) {
    super(guiceScoped);
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
}
