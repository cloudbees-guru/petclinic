package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTest {

	@Test
	@Tag("integration")
	public void testFindOwners() throws Exception {
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		WebDriver driver = new RemoteWebDriver(new URL("http://tools-1:4444/wd/hub"), firefoxOptions);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://35.205.182.83/petclinic_skures/owners/find");

		WebElement lastnameElement = driver.findElement(By.id("lastName"));
		lastnameElement.sendKeys("Black");
		lastnameElement.submit();

		WebElement petNameElement = driver.findElement(By.xpath("/html/body/div/div/table[2]/tbody/tr/td[1]/dl/dd[1]"));
		String petName = petNameElement.getText();
		assertEquals("Lucky", petName);

		driver.quit();
	}

}
