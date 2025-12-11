package pages;

import annotations.UrlTemplate;
import data.CourseName;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

@UrlTemplate(value = "/lessons/{1}/")
public class CoursePage extends AbsBasePage<CoursePage> {

  @FindBy(css = "h1.sc-s2pydo-1")
  WebElement title;

  public CoursePage(WebDriver driver) {
    super(driver);
  }

  @Step("Проверить, что открылась страница 'Каталог'")
  public CoursePage checkOpenPage(CourseName courseName) {
    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.textToBePresentInElement(title, courseName.getName()));
    return this;
  }
}
