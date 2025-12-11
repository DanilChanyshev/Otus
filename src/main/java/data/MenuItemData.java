package data;

public enum MenuItemData {
  EDUCATION("Обучение");

  private final String name;

  MenuItemData(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
