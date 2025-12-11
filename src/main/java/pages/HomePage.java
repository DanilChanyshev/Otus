package pages;

import annotations.Path;
import com.google.inject.Inject;
import data.CatalogCoursesName;
import data.MenuItemData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import waiters.Waiter;
import java.time.Duration;
import java.util.List;

@Path("/")
public class HomePage extends AbsBasePage<HomePage> {

  @Inject
  CatalogCoursesPage catalogCoursesPage;
  @Inject
  Waiter waiter;

  private String buttonEducation = "//span[text() = '%s']";

  public HomePage(WebDriver driver) {
    super(driver);
  }

  private List<WebElement> catalogName() {
    return driver.findElements(By.cssSelector(".sc-4zz0i4-0"));
  }

  @Step("Навестись на кнопку {0}")
  public HomePage moveToButton(MenuItemData data) {
    WebElement element = driver.findElement(By.xpath(buttonEducation.formatted(data.getName())));
    actions.moveToElement(element)
            .build()
            .perform();
    return this;
  }

  @Step("Выбрать каталог курсов с названием {0} ")
  public CatalogCoursesPage choseCatalogCourses(CatalogCoursesName catalogName) {
    WebElement element = Waiter.waitWebElement(
            () -> findCatalogWithName(catalogName.getName()),
            Duration.ofSeconds(15)
    );
    element.click();
    return catalogCoursesPage;
  }

  @Step("Поиск каталога курса по названию {0}")
  private WebElement findCatalogWithName(String name) {
    return catalogName().stream()
            .filter(catalog -> catalog.getText().contains(name))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Каталог с названием %s не найден".formatted(name)));
  }
}
