package cucumber.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.Если;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import pages.CatalogCoursesPage;
import support.GuiceScoped;

public class CatalogCoursesPageSteps {

  @Inject
  private GuiceScoped guiceScoped;

  @Inject
  private CatalogCoursesPage catalogCoursesPagecoursePage;

  @Пусть("Открыта страница каталога курсов")
  public void openCoursesPage() {
    catalogCoursesPagecoursePage.open();
    catalogCoursesPagecoursePage.checkOpenPage();
  }

  @Если("Найти курс (.*)$")
  public void findMaxAmountCourses(String courseName) {
    catalogCoursesPagecoursePage.searchWithText(courseName);
    catalogCoursesPagecoursePage.clickCourseWithName(courseName);
    guiceScoped.setStore("title", courseName);
  }

  @И("Поиск самого дорогого и самого дешевого подготовительного курса")
  public void findMaxAndMinAmountCourses() {
    catalogCoursesPagecoursePage.findMaxAndMinAmountCourses();
  }

  @Тогда("Вывести название и цену самого дорогого подготовительного курса")
  public void printMaxAmountCourse(){
    catalogCoursesPagecoursePage.printMaxAmountCourse();
  }

  @Тогда("Вывести название и цену самого дешевого подготовительного курса")
  public void printMinAmountCourse(){
    catalogCoursesPagecoursePage.printMinAmountCourse();
  }

  @Тогда("^Вывести курсы с датой (.*)$")
  public void findAndPrintCourseWithDate(String courseDate) {
    catalogCoursesPagecoursePage.findCourseWithDate(courseDate);
  }

  @Если("^Нажать на фильтр (.*)$")
  public void clickFilterCheckbox(String filter) {
    catalogCoursesPagecoursePage.clickFilter(filter);
  }

  @И("^Проверить, что фильтр (.*) выбран$")
  public void checkSelectedFilter(String filter) {
    catalogCoursesPagecoursePage.checkSelectedFilter(filter);
  }
}
