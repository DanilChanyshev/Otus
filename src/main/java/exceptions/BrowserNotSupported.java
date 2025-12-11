package exceptions;

public class BrowserNotSupported extends RuntimeException {

  public BrowserNotSupported(String browserName) {
    super("Browser %s not supported".formatted(browserName));
  }
}
