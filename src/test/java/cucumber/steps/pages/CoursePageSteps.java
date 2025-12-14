package cucumber.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.То;
import pages.CoursePage;
import support.GuiceScoped;

public class CoursePageSteps {

  @Inject
  GuiceScoped guiceScoped;

  @Inject
  CoursePage coursePage;

  @То("Страница курса успешно открыта")
  public void checkOpenPage(){
    coursePage.checkOpenPage(guiceScoped.getStore("title"));
  }
}
