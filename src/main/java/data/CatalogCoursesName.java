package data;

public enum CatalogCoursesName {
  PROGRAMMING("Программирование");

  private final String name;

  CatalogCoursesName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
