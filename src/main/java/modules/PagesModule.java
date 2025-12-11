package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import pages.CatalogCoursesPage;
import pages.CoursePage;
import pages.HomePage;
import helper.ParserDateHelper;
import waiters.Waiter;

public class PagesModule extends AbstractModule {

  private final WebDriver driver;

  public PagesModule(WebDriver driver) {
    this.driver = driver;
  }

  @Provides
  @Singleton
  public HomePage getHomePage() {
    return new HomePage(driver);
  }

  @Provides
  @Singleton
  public CatalogCoursesPage getCatalogCoursesPage() {
    return new CatalogCoursesPage(driver);
  }

  @Provides
  @Singleton
  public CoursePage getCoursePage() {
    return new CoursePage(driver);
  }

  @Provides
  @Singleton
  public Waiter waiter() {
    return new Waiter(driver);
  }

  @Provides
  @Singleton
  public ParserDateHelper parserDateHelper() {
    return new ParserDateHelper(driver);
  }
}
