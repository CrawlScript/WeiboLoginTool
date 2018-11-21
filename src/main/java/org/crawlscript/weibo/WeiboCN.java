/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.crawlscript.weibo;

import java.util.Set;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该登录算法适用时间: 2018-11-21 —— ?
 * 利用Selenium获取登陆新浪微博weibo.cn的cookie
 *
 * @author hu
 */
public class WeiboCN {

    public static final Logger LOG = LoggerFactory.getLogger(WeiboCN.class);

    /**
     * 获取新浪微博的cookie，这个方法针对weibo.cn有效，对weibo.com无效 weibo.cn以明文形式传输数据，请使用小号
     *
     * @param username 新浪微博用户名
     * @param password 新浪微博密码
     * @return
     * @throws Exception
     */
    public static String getSinaCookie(String username, String password) throws Exception {
        StringBuilder sb = new StringBuilder();
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER);
//        FirefoxDriver driver = new FirefoxDriver();

        driver.setJavascriptEnabled(true);
        driver.get("https://passport.weibo.cn/signin/login");
        driver.executeScript("document.getElementById('loginWrapper').style.display = 'block'");
        WebElement mobile = driver.findElementByCssSelector("input#loginName");
        mobile.sendKeys(username);
        WebElement pass = driver.findElementByCssSelector("input#loginPassword");
        pass.sendKeys(password);
        WebElement submit = driver.findElementByCssSelector("a#loginAction");
        submit.click();
        for(int i=0;i<50;i++) {
            Thread.sleep(100);
            String result = concatCookie(driver);
            LOG.debug("current cookie: " + result);
            if(result.contains("SUB")){
                LOG.debug("login successfully with cookie: " + result);
                return result;
            }
        }
        driver.close();
        throw new Exception("weibo login failed");

    }

    public static String concatCookie(WebDriver driver) {
        Set<Cookie> cookieSet = driver.manage().getCookies();
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookieSet) {
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        String result = sb.toString();
        return result;
    }


}