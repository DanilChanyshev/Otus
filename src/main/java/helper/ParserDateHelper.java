package helper;

import data.CatalogCourseInfo;
import io.qameta.allure.Step;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserDateHelper {

  private final WebDriver driver;

  public ParserDateHelper(WebDriver driver) {
    this.driver = driver;
  }

  @Step("Перевод строки с датой в LocalDate")
  public LocalDate formatStartDate(String date) {
    String dateClean = date.split("·")[0].trim();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
    try {
      return LocalDate.parse(dateClean, format);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Не удалось распарсить дату %s".formatted(date));
    }
  }

  @Step("Поиск названия и даты начала курса")
  public List<CatalogCourseInfo> findCourses() {
    String html = driver.getPageSource();
    Document doc = Jsoup.parse(html);

    Elements courseLinks = doc.select("a[href^='/lessons/']");

    List<CatalogCourseInfo> courses = new ArrayList<>();
    for (Element link : courseLinks) {
      Element titleElement = link.selectFirst("h6");
      if (titleElement == null) continue;
      String title = titleElement.text().trim();

      Element dateElement = link.selectFirst("div.jIBTjx div.sc-hrqzy3-1.jEGzDf");
      if (dateElement == null) continue;

      String dateText = dateElement.text().trim();

      try {
        LocalDate startDate = formatStartDate(dateText);
        courses.add(new CatalogCourseInfo(title, startDate));
      } catch (Exception ignored){
        // продолжаем поиск
      }
    }
    if (courses.isEmpty()) {
      throw new RuntimeException("Не найдено ни одного курса с датой");
    }
    return courses;
  }

  @Step("Найти курсы с самой ранней датой начала")
  public Map<String, List<CatalogCourseInfo>> findEarliestCourses() {

    LocalDate earliest = findCourses().stream()
            .map(CatalogCourseInfo::getStartDate)
            .reduce((d1, d2) -> d1.isBefore(d2) ? d1 : d2)
            .orElseThrow();

    List<CatalogCourseInfo> earliestCourses = findCourses().stream()
            .filter(c -> c.getStartDate().isEqual(earliest))
            .collect(Collectors.toList());

    Map<String, List<CatalogCourseInfo>> result = new LinkedHashMap<>();
    result.put("earliest", earliestCourses);
    return result;
  }

  @Step("Найти курсы с самой ранней датой начала")
  public Map<String, List<CatalogCourseInfo>> findLatestCourses() {

    LocalDate latest = findCourses().stream()
            .map(CatalogCourseInfo::getStartDate)
            .reduce((d1, d2) -> d1.isAfter(d2) ? d1 : d2)
            .orElseThrow();

    List<CatalogCourseInfo> latestCourses = findCourses().stream()
            .filter(c -> c.getStartDate().isEqual(latest))
            .collect(Collectors.toList());

    Map<String, List<CatalogCourseInfo>> result = new LinkedHashMap<>();
    result.put("latest", latestCourses);
    return result;
  }
}
