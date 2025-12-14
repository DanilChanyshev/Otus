package pages;

import annotations.Path;
import com.google.inject.Inject;
import data.CatalogCourseInfo;
import io.qameta.allure.Step;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import support.GuiceScoped;
import waiters.Waiter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

@Path("/catalog/courses")
public class CatalogCoursesPage extends AbsBasePage<CatalogCoursesPage> {

  @Inject
  CoursePage coursePage;
  @Inject
  GuiceScoped guiceScoped;
  @Inject
  public CatalogCoursesPage(GuiceScoped guiceScoped) {
    super(guiceScoped);
  }

  private static final Random RANDOM = new Random();

  @FindBy(css = "h1 > div.sc-hrqzy3-1.jEGzDf")
  private WebElement title;
  @FindBy(css = ".sc-sbwx8a-0")
  private WebElement search;
  @FindBy(xpath = "//p[text() = 'Подготовительный курс']")
  private List<WebElement> treningCourse;
  @FindBy(css = ".sc-1x9oq14-0-label")
  private List<WebElement> getFilterNames;
  @FindBy(css = "a.sc-zzdkm7-0 h6 > div")
  private List<WebElement> getCourseNameDivs;
  @FindBy(css = ".sc-18q05a6-0")
  private WebElement gridCourses;
  private String cardElement = "//div[text() = '%s']/../..";

  @Step("Проверить, что открылась страница 'Каталог'")
  public CatalogCoursesPage checkOpenPage() {
    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.textToBePresentInElement(title, "Каталог"));
    return this;
  }

  @Step("Навести курсор на строку поиска в внести текст")
  public CatalogCoursesPage searchWithText(String courseName) {
    actions.moveToElement(search)
            .click()
            .sendKeys(courseName)
            .sendKeys(Keys.ENTER)
            .build()
            .perform();
    return this;
  }

  @Step("Кликнуть по курсу с названием {0}")
  public CoursePage clickCourseWithName(String courseName) {
    WebElement course = Waiter.waitWebElement(
            () -> findCourseWithTitle(courseName), Duration.ofSeconds(10));
    course.click();
    return coursePage;
  }

  @Step("Проверить, что чекбокс фильтра {0} выбран")
  public CatalogCoursesPage checkSelectedFilter(String catalogName) {
    String idLabel = findFilterWithTitle(catalogName).getAttribute("for");
    WebElement checkbox = driver.findElement(By.id(idLabel));
    if (!checkbox.isSelected()) {
      throw new RuntimeException("Фильтр не найден");
    }
    return this;
  }

  @Step("Найти фильтр с названием {0}")
  private WebElement findFilterWithTitle(String filter) {
    return getFilterNames
            .stream()
            .filter(course -> course.getText().trim().equals(filter))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Фильтр %s не найден".formatted(filter)));
  }

  @Step("Выбрать фильтр {0}")
  public CatalogCoursesPage clickFilter(String title){
    String idLabel = findFilterWithTitle(title).getAttribute("for");
    WebElement checkbox = driver.findElement(By.id(idLabel));
    if (!checkbox.isSelected()){
      checkbox.click();
    }
    waitLoadAllElements(treningCourse);
    return this;
  }

  @Step("Найти курс по дате его начала")
  public CatalogCoursesPage findCourseWithDate(String date){
    findCourses(date).forEach(c -> System.out.println(c));
    return this;
  }

  @Step("Поиск самого дешевого и самого дорогого подготовительного курса")
  public CoursePage findMaxAndMinAmountCourses(){
    waitLoadAllElements(treningCourse);
    clickFilter("Программирование");
    treningCourse.get(0).click();
    coursePage.findMinAmount();
    return coursePage;
  }

  @Step("Вывод в консоль самый дорогой подготовительный курс")
  public CatalogCoursesPage printMaxAmountCourse(){
    String maxCourseName = guiceScoped.getStore("course max");
    String maxAmount = guiceScoped.getStore("amount max");
    System.out.println("Самый дорогой подготовительный курс: \"%s\", стоимость: %s руб.".formatted(maxCourseName, maxAmount));
    return this;
  }

  @Step("Вывод в консоль самый дешевый подготовительный курс")
  public CatalogCoursesPage printMinAmountCourse(){
    String minCourseName = guiceScoped.getStore("course min");
    String minAmount = guiceScoped.getStore("amount min");
    System.out.println("Самый дешевый подготовительный курс: \"%s\", стоимость: %s руб.".formatted(minCourseName, minAmount));
    return this;
  }

  @Step("Найти курс с названием {0}")
  private WebElement findCourseWithTitle(String titleCourse) {
    return getCourseNameDivs
            .stream()
            .filter(course -> course.getText().trim().contains(titleCourse))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Курс с названием %s не найден".formatted(titleCourse)));
  }

  @Step("Перевод даты в string")
  private String formatStartDate(String date) {
    LocalDate parseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    try {
      return parseDate.format(DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru")));
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Не удалось распарсить дату %s".formatted(date));
    }
  }

  @Step("Поиск названия и даты начала курса")
  private List<CatalogCourseInfo> findCourses(String date) {
    String html = guiceScoped.getDriver().getPageSource();
    Document doc = Jsoup.parse(html);

    Elements courseLinks = doc.select("a[href^='/lessons/']");

    List<CatalogCourseInfo> courses = new ArrayList<>();
    for (Element link : courseLinks) {
      Element titleElement = link.selectFirst("h6");
      if (titleElement == null) continue;
      String title = titleElement.text().trim();

      Element dateElement = link.selectFirst("div.jIBTjx div.sc-hrqzy3-1.jEGzDf");
      if (dateElement == null) continue;

      String dateCourse = dateElement.text().trim();
      String dateTrue = formatStartDate(date);
      if (dateCourse.contains(dateTrue)){
        dateTrue = dateCourse;
      } else continue;
      try {
        courses.add(new CatalogCourseInfo(title, dateTrue));
      } catch (Exception ignored){
        // продолжаем поиск
      }
    }
    if (courses.isEmpty()) {
      throw new RuntimeException("Не найдено ни одного курса с датой");
    }
    return courses;
  }
}
