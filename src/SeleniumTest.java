
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class SeleniumTest {

    public static void main(String[] args) {

        SeleniumTest selTest = new SeleniumTest();
        selTest.crawl();

    }


    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Users\\jhta\\Downloads\\Crawling2\\src\\chromedriver-win64\\chromedriver.exe";

    //크롤링 할 URL
    private String base_url;

    public SeleniumTest() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        //Driver SetUp
        driver = new ChromeDriver(options);
        base_url = "https://www.algumon.com/deal/rank";
    }

    public void crawl() {

        try {
            driver.get(base_url);
            WebElement outerElement = driver.findElement(By.xpath("/html/body/div[6]/div[2]/ul"));
            List<WebElement> innerElement = outerElement.findElements(By.className("post-li"));

            for (WebElement webElement : innerElement) {
                System.out.println(webElement.getText());
                List<WebElement> imgElements = webElement.findElements(By.tagName("img"));
                for (WebElement imgElement : imgElements) {
                    String src = imgElement.getAttribute("src");
                    System.out.println(src);
                }
            }

        } catch (Exception e) {
            System.out.println("[에러] " + e.getMessage());
        } finally {
        }

    }

}
