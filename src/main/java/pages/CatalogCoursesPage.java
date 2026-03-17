package pages;

import annotations.Path;
import com.google.inject.Inject;
import data.CatalogCourseInfo;
import data.CatalogCoursesName;
import data.CourseName;
import io.qameta.allure.Step;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import helper.ParserDateHelper;
import waiters.Waiter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Path("/catalog/courses")
public class CatalogCoursesPage extends AbsBasePage<CatalogCoursesPage> {

  @Inject
  Waiter waiter;
  @Inject
  CoursePage coursePage;
  @Inject
  ParserDateHelper parserDateHelper;
  @FindBy(css = "h1 > div.sc-hrqzy3-1.jEGzDf")
  private WebElement title;
  @FindBy(css = ".sc-1817vyo-0")
  private WebElement search;

  public CatalogCoursesPage(WebDriver driver) {
    super(driver);
  }

  private List<WebElement> getFilterNames() {
    return driver.findElements(By.cssSelector(".sc-1x9oq14-0-label"));
  }

  private List<WebElement> getCourseNameDivs() {
    return driver.findElements(By.cssSelector("a.sc-zzdkm7-0 h6 > div"));
  }

  @Step("Проверить, что открылась страница 'Каталог'")
  public CatalogCoursesPage checkOpenPage() {
    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.textToBePresentInElement(title, "Каталог"));
    return this;
  }

  @Step("Навести курсор на строку поиска в внести текст")
  public CatalogCoursesPage searchWithText(CourseName courseName) {
    actions.moveToElement(search)
            .click()
            .sendKeys(courseName.getName())
            .sendKeys(Keys.ENTER)
            .perform();
    return this;
  }

  @Step("Кликнуть по курсу с названием {0}")
  public CoursePage clickCourseWithName(CourseName courseName) {
    WebElement course = Waiter.waitWebElement(
            () -> findCourseWithTitle(courseName.getName()), Duration.ofSeconds(10));
    course.click();
    return coursePage;
  }

  @Step("Проверить, что чекбокс фильтра {0} выбран")
  public CatalogCoursesPage checkSelectedFilter(CatalogCoursesName catalogName) {
    String idLabel = findFilterWithTitle(catalogName.getName()).getAttribute("for");
    WebElement checkbox = driver.findElement(By.id(idLabel));
    if (!checkbox.isSelected()) {
      throw new RuntimeException("Фильтр не найден");
    }
    return this;
  }

  @Step("Проверить, что на странице отображаются корректные названия и даты для самых ранних курсов")
  public CatalogCoursesPage verifyEarliestCourses() {
    Map<String, List<CatalogCourseInfo>> result = parserDateHelper.findEarliestCourses();
    for (CatalogCourseInfo course : result.get("earliest")) {
      Assertions.assertTrue(pageContainsCourseWithTitleAndDate(course.getTitle(), course.getStartDate()),
              "Курс '%s' с датой '%s' не отображаться на странице");
    }
    return this;
  }

  @Step("Проверить, что на странице отображаются корректные названия и даты для самых поздних курсов")
  public CatalogCoursesPage verifyLatestCourses() {
    Map<String, List<CatalogCourseInfo>> result = parserDateHelper.findLatestCourses();
    for (CatalogCourseInfo course : result.get("latest")) {
      Assertions.assertTrue(pageContainsCourseWithTitleAndDate(course.getTitle(), course.getStartDate()),
              "Курс '%s' с датой '%s' не отображаться на странице");
    }
    return this;
  }

  @Step("Проверяем, что название и дата старта курса соответствует ожидаемому")
  private boolean pageContainsCourseWithTitleAndDate(String title, LocalDate date) {
    String html = driver.getPageSource();
    Document doc = Jsoup.parse(html);
    Elements cards = doc.select("a[href^='/lessons/']");

    String expectedDateStr = date.format(DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru")));

    for (Element card : cards) {
      String cardTitle = card.select("h6").text().replace("######", "").trim();
      String cardDate = card.select("div.jIBTjx div.sc-hrqzy3-1.jEGzDf")
              .stream()
              .map(Element::text)
              .filter(t -> t.contains(expectedDateStr))
              .findFirst()
              .orElse("");

      if (cardTitle.equals(title) && !cardDate.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  @Step("Найти курс с названием {0}")
  private WebElement findCourseWithTitle(String titleCourse) {
    return getCourseNameDivs()
            .stream()
            .filter(course -> course.getText().trim().equals(titleCourse))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Курс с названием %s не найден".formatted(titleCourse)));
  }

  @Step("Найти фильтр с названием {0}")
  private WebElement findFilterWithTitle(String filter) {
    return getFilterNames()
            .stream()
            .filter(course -> course.getText().trim().equals(filter))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Фильтр %s не найден".formatted(filter)));
  }
}
