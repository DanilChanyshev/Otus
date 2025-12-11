package data;

public enum CourseName {
  JAVA_QA_ENGINEER_PRO("Java QA Engineer. Professional");

  private final String name;

  CourseName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
