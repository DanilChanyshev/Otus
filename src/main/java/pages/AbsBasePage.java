package pages;

import annotations.Path;
import annotations.UrlTemplate;
import annotations.Urls;
import commons.AbsCommons;
import exceptions.PathNotFoundException;
import org.openqa.selenium.WebDriver;
import java.util.Arrays;

public abstract class AbsBasePage<T> extends AbsCommons {

  private final String baseUrl = System.getProperty("base.url");

  public AbsBasePage(WebDriver driver) {
    super(driver);
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
}
