
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TecAdminSeleniumTest {

	public static WebDriver driver;

	@Test
	public void Test() {

		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
		//chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--no-sandbox");
		driver = new ChromeDriver(chromeOptions);
		driver.get("https://dev.qpathways.com");
		// driver.manage().window().maximize();

		if (driver.getPageSource().contains("I'm Feeling Lucky")) {
			System.out.println("Pass");
		} else {
			System.out.println("Fail");
		}
		driver.quit();
	}
}