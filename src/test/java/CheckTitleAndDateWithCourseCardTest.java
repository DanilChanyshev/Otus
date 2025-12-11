import com.google.inject.Inject;
import extensions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogCoursesPage;

@ExtendWith(UIExtension.class)
public class CheckTitleAndDateWithCourseCardTest {

  @Inject
  CatalogCoursesPage catalogCoursesPage;

  @Test
  public void findCourseThatStartEarlierTest() {
    catalogCoursesPage.open()
            .checkOpenPage()
            .verifyEarliestCourses()
            .verifyLatestCourses();

  }
}
