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

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import okhttp3.Request;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 该登录算法适用时间: 2018-11-21 —— ?
 * 利用WebCollector和获取的cookie爬取新浪微博并抽取数据
 *
 * @author hu
 */
public class WeiboCrawler extends BreadthCrawler {

    String cookie;

    public WeiboCrawler(String crawlPath, String weiboUsername, String weiboPassword) throws Exception {
        super(crawlPath, false);
        /* 获取新浪微博的cookie，账号密码以明文形式传输，请使用小号 */
        cookie = WeiboCN.getSinaCookie(weiboUsername, weiboPassword);

        // 为每次请求设置新浪微博cookie
        setRequester(new OkHttpRequester() {
            @Override
            public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
                return super.createRequestBuilder(crawlDatum)
                        .header("Cookie", cookie);
            }
        });

        //设置线程数
        setThreads(3);
        //设置每个线程的爬取间隔
        getConf().setExecuteInterval(1000);
    }


    @Override
    public void visit(Page page, CrawlDatums next) {
        int pageNum = Integer.valueOf(page.meta("pageNum"));
        /* 抽取微博 */
        Elements weibos = page.select("div.c[id]");
        for (Element weibo : weibos) {
            System.out.println("第" + pageNum + "页\t" + weibo.text());
        }
    }

    public static void main(String[] args) throws Exception {
        WeiboCrawler crawler = new WeiboCrawler("crawl_weibo", "你的用户名", "你的密码");
        /* 对某人微博前5页进行爬取 */
        for (int i = 1; i <= 5; i++) {
            String seedUrl = "https://weibo.cn/zhouhongyi?vt=4&page=" + i;
            crawler.addSeedAndReturn(seedUrl).meta("pageNum", i);
        }
        crawler.start(1);
    }

}