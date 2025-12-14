package pages;

import annotations.UrlTemplate;
import com.google.inject.Inject;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import support.GuiceScoped;
import java.time.Duration;
import java.util.List;

@UrlTemplate(value = "/lessons/{1}/")
public class CoursePage extends AbsBasePage<CoursePage> {

  @Inject
  GuiceScoped guiceScoped;
  @Inject
  public CoursePage(GuiceScoped guiceScoped) {
    super(guiceScoped);
  }

  @FindBy(css = "h1.sc-s2pydo-1")
  WebElement title;
  @FindBy(css = ".iXzxst")
  private WebElement titleCategory;
  @FindBy(xpath = "//p[text() = 'Подготовительный курс']")
  private List<WebElement> treningCourse;
  @FindBy(css = ".sc-153sikp-11")
  private WebElement amount;
  @FindBy(css = ".sc-153sikp-7")
  private WebElement titleCourseName;


  @Step("Проверить, что открылась страница курса")
  public CoursePage checkOpenPage(String courseName) {
    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.textToBePresentInElement(title, courseName));
    return this;
  }

  @Step("Проверить, что открылась страница подготовительного курса")
  public CoursePage checkOpenPreparatoryCourse(){
    new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.textToBePresentInElement(titleCategory, "Подготовительный онлайн курс"));
    return this;
  }

  @Step("Найти самый дешевый и самый дорогой курс")
  public CoursePage findMinAmount() {
    int maxSum = Integer.parseInt(amount.getText().split(" ")[0].trim());
    int minSum = Integer.parseInt(amount.getText().split(" ")[0].trim());
    backToPage();
    waitLoadAllElements(treningCourse);

    for (int i = 0; i < treningCourse.size(); i++) {
      treningCourse.get(i).click();
      checkOpenPreparatoryCourse();

      String amountCourse = amount.getText().split(" ")[0].trim();

      if (maxSum <= Integer.parseInt(amountCourse)) {
        maxSum = Integer.parseInt(amountCourse);
        guiceScoped.setStore("course max", titleCourseName.getText());
        guiceScoped.setStore("amount max", amountCourse);
      }
      if (minSum >= Integer.parseInt(amountCourse)) {
        minSum = Integer.parseInt(amountCourse);
        guiceScoped.setStore("course min", titleCourseName.getText());
        guiceScoped.setStore("amount min", amountCourse);
      }
      backToPage();
      waitLoadAllElements(treningCourse);
    }
    return this;
  }
}
