import com.google.inject.Inject;
import data.CatalogCoursesName;
import data.MenuItemData;
import extensions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.HomePage;

@ExtendWith(UIExtension.class)
public class CheckOpenCategoryCoursesTest {

  @Inject
  HomePage homePage;

  @Test
  public void checkOpenCategoryCoursesTest() {
    homePage.open()
            .moveToButton(MenuItemData.EDUCATION)
            .choseCatalogCourses(CatalogCoursesName.PROGRAMMING)
            .checkOpenPage()
            .checkSelectedFilter(CatalogCoursesName.PROGRAMMING);
  }
}
