package data;

import java.time.LocalDate;

public class CatalogCourseInfo {
  private final String title;
  private final LocalDate startDate;

  public CatalogCourseInfo(String title, LocalDate startDate) {
    this.title = title;
    this.startDate = startDate;
  }

  public String getTitle() {
    return title;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  @Override
  public String toString() {
    return "%s (%s)".formatted(title, startDate);
  }
}
