# WeiboLoginTool
基于WebCollector的新浪微博爬虫及相关登录工具，如新浪微博Cookie获取



## 一行代码获取微博登录Cookie

```java
String cookie = WeiboCN.getSinaCookie("微博用户名", "微博密码");
```

## 基于WebCollector的微博爬虫

```java
WeiboCrawler crawler = new WeiboCrawler("crawl_weibo", "你的用户名", "你的密码");
/* 对某人微博前5页进行爬取 */
for (int i = 1; i <= 5; i++) {
    String seedUrl = "https://weibo.cn/zhouhongyi?vt=4&page=" + i;
    crawler.addSeedAndReturn(seedUrl).meta("pageNum", i);
}
crawler.start(1);
```


## 讨论群

QQ: 831391915