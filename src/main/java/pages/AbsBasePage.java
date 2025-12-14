package pages;

import annotations.Path;
import annotations.UrlTemplate;
import annotations.Urls;
import com.google.inject.Inject;
import commons.AbsCommons;
import exceptions.PathNotFoundException;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import support.GuiceScoped;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public abstract class AbsBasePage<T> extends AbsCommons {

  private final String baseUrl = System.getProperty("base.url");
  @Inject
  private static WebDriverWait wait;


  @Inject
  public AbsBasePage(GuiceScoped guiceScoped) {
    super(guiceScoped);
  }

  private String getPathWithData(String name, String[] data) {

    Class<T> clazz = (Class<T>) this.getClass();

    UrlTemplate urlTemplate = null;
    if (clazz.isAnnotationPresent(Urls.class)) {
      Urls urls = clazz.getDeclaredAnnotation(Urls.class);
      UrlTemplate[] urlTemplates = urls.urlTemplate();
      urlTemplate = Arrays.stream(urlTemplates)
              .filter((UrlTemplate urlTempalte) -> urlTempalte.name().equals(name))
              .findFirst()
              .get();
    }

    if (clazz.isAnnotationPresent(UrlTemplate.class)) {
      urlTemplate = clazz.getDeclaredAnnotation(UrlTemplate.class);
    }
    if (urlTemplate != null) {
      String template = urlTemplate.value();
      for (int i = 0; i < data.length; i++) {
        template = template.replace(String.format("{%d}", i + 1), data[i]);
      }
      return template;
    }
    return "";
  }

  private String getPath() {
    Class<T> clazz = (Class<T>) this.getClass();
    if (clazz.isAnnotationPresent(Path.class)) {
      Path pathObj = clazz.getDeclaredAnnotation(Path.class);
      return pathObj.value();
    }
    throw new PathNotFoundException();
  }

  public T open() {
    driver.get(baseUrl + getPath());
    return (T) this;
  }

  public T open(String name, String... data) {
    driver.get(baseUrl + getPathWithData(name, data));
    return (T) this;
  }

  @Step("Вернуться на страницу назад")
  public T backToPage(){
    driver.navigate().back();

    return (T) this;
  }

  @Step("Ожидание загрузки всех элементов")
  public void waitLoadAllElements(List<WebElement> element){
    new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.visibilityOfAllElements(element));
  }
  @Step("Ожидание загрузки всех элементов")
  public void waitLoadClikableElement(WebElement element){
    new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.elementToBeClickable(element));
  }
}
