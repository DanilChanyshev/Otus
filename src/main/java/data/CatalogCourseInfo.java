package data;

public class CatalogCourseInfo {
  private final String title;
  private final String startDate;

  public CatalogCourseInfo(String title, String startDate) {
    this.title = title;
    this.startDate = startDate;
  }

  public String getTitle() {
    return title;
  }

  public String getStartDate() {
    return startDate;
  }

  @Override
  public String toString() {
    return "%s (%s)".formatted(title, startDate);
  }
}
