package ru.netology.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.cssSelector;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void sendFormCorrect() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("+71111111111");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".paragraph")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.strip());
    }

    @Test
    void sendWrongName() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Ivan");
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("+71111111111");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_type_text .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", message.strip());
    }

    @Test
    void sendWrongPhone() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("71111111111");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_type_tel .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message.strip());
    }

    @Test
    void sendWithoutAgreement() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("+71111111111");
        driver.findElement(cssSelector("button")).click();
        assertTrue(driver.findElement(cssSelector(".checkbox__box")).isEnabled());
    }

    @Test
    void sendFormEmptyAll() {
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_type_text .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void sendFormEmptyName() {
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("+71111111111");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_type_text .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void sendFormEmptyPhone() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_type_tel .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }
}
