import com.google.inject.Inject;
import data.CourseName;
import extensions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogCoursesPage;

@ExtendWith(UIExtension.class)
public class CoursesSearchWithNameTest {

  @Inject
  CatalogCoursesPage catalogCoursesPage;

  @Test
  public void coursesSearchWithNameTest() {

    catalogCoursesPage
            .open()
            .checkOpenPage()
            .searchWithText(CourseName.JAVA_QA_ENGINEER_PRO)
            .clickCourseWithName(CourseName.JAVA_QA_ENGINEER_PRO)
            .checkOpenPage(CourseName.JAVA_QA_ENGINEER_PRO);
  }
}
