package data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class SeleniumTest {

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
        // options.addArguments("headless");

        //Driver SetUp
        driver = new ChromeDriver(options);
        base_url = "https://www.algumon.com/";
    }

    public void crawl() {
        try {
            driver.get(base_url);
            WebElement outerElement = driver.findElement(By.xpath("/html/body/div[6]/div[2]/ul"));
            List<WebElement> innerElement = outerElement.findElements(By.className("post-li"));

            URLDomain urlDomain = new URLDomain();

            for (WebElement webElement : innerElement) {
                String[] lines = webElement.getText().split("\n");
                urlDomain.setItem_name(lines[0].trim());
                urlDomain.setFrom_url(lines[1].trim());
                urlDomain.setPrice(lines[2].trim().isEmpty() ? "가격없음" : lines[2].trim());
                urlDomain.setDelivery(lines[3].trim());
                urlDomain.setWriter(lines[4].trim());
                urlDomain.setWant_to_buy(lines[5].trim());
                urlDomain.setBought(lines[6].trim());
                urlDomain.setComt(lines[7].trim());
                urlDomain.setShare(lines[8].trim());

                List<WebElement> imgElements = webElement.findElements(By.tagName("img"));
                for (WebElement imgElement : imgElements) {
                    String src = imgElement.getAttribute("src");
                    urlDomain.setImg_src(src.trim());
                    DBConnection db = new DBConnection();
                    db.Connection(urlDomain);
                }
            }

        } catch (Exception e) {
            System.out.println("[에러] " + e.getMessage());
        } finally {
            driver.close();
        }
    }
}
