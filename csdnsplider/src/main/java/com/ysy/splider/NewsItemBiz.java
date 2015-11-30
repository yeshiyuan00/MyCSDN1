package com.ysy.splider;


import com.ysy.bean.News;
import com.ysy.bean.NewsDto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import sun.rmi.runtime.Log;


/**
 * 处理NewItem的业务类
 * Created by yeshiyuan on 11/19/15.
 */
public class NewsItemBiz {
    /**
     * 业界、移动、云计算
     *
     * @param newsType
     * @param currentPage
     * @return
     * @throws CommonException
     */
    public List<com.ysy.splider.NewsItem> getNewsItems(int newsType, int currentPage) throws CommonException {

        String urlStr = URLUtil.generateUrl(newsType, currentPage);

        String htmlStr = com.ysy.splider.DataUtil.doGet(urlStr);

        List<com.ysy.splider.NewsItem> newsItems = new ArrayList<com.ysy.splider.NewsItem>();
        com.ysy.splider.NewsItem newsItem = null;


        Document doc = Jsoup.parse(htmlStr);

        Elements units = doc.getElementsByClass("unit");

        for (int i = 0; i < units.size(); i++) {
            newsItem = new com.ysy.splider.NewsItem();
            newsItem.setNewsType(newsType);

            Element unit_ele = units.get(i);

            Element h1_ele = unit_ele.getElementsByTag("h1").get(0);
            Element h1_a_ele = h1_ele.child(0);
            String title = h1_a_ele.text();
            String href = h1_a_ele.attr("href");

            newsItem.setLink(href);
            newsItem.setTitle(title);

            Element h4_ele = unit_ele.getElementsByTag("h4").get(0);
            Element ago_ele = h4_ele.getElementsByClass("ago").get(0);
            String date = ago_ele.text();

            newsItem.setDate(date);

            Element dl_ele = unit_ele.getElementsByTag("dl").get(0);// dl
            Element dt_ele = dl_ele.child(0);// dt
            try {// 可能没有图片
                Element img_ele = dt_ele.child(0);
                String imgLink = img_ele.child(0).attr("src");
                newsItem.setImgLink(imgLink);
            } catch (IndexOutOfBoundsException e) {

            }
            Element content_ele = dl_ele.child(1);// dd
            String content = content_ele.text();
            newsItem.setContent(content);
            newsItems.add(newsItem);
        }
        return newsItems;
    }

    /**
     * 根据文章的url返回一个NewsDto对象
     *
     * @return
     * @throws CommonException
     */
    public NewsDto getNews(String urlStr) throws CommonException {
        NewsDto newsDto = new NewsDto();
        List<News> newses = new ArrayList<News>();
        String htmlStr = DataUtil.doGet(urlStr);

        Document document = Jsoup.parse(htmlStr);

        // 获得文章中的第一个detail
        Element title = document.getElementsByTag("h1").get(0);
//        Element detailEle = document.select(".left .detail").get(0);
        // 标题
//        Element titleEle = detailEle.select("h1.title").get(0);
        News news = new News();
        news.setTitle(title.text());
        news.setType(News.NewsType.TITLE);
        newses.add(news);

        // 摘要
//        Element summaryEle = detailEle.select("div.summary").get(0);
//        news = new News();
//        news.setSummary(summaryEle.text());
//        newses.add(news);

        // 内容
//        Element contentEle = dou.select("div.con.news_content").get(0);
//        Elements childrenEle = contentEle.children();
        Elements childrenEle = document.getElementsByTag("p");

        for (Element child : childrenEle) {
            Elements imgEles = child.getElementsByTag("img");
            // 图片
            if (imgEles.size() > 0) {
                for (Element imgEle : imgEles) {
                    if (imgEle.attr("src").equals(""))
                        continue;
                    news = new News();
                    news.setImageLink(imgEle.attr("src"));
                    news.setType(News.NewsType.IMG);
                    newses.add(news);
                }
            }
            // 移除图片
            imgEles.remove();
            if (child.text().equals(""))
                continue;

            news = new News();
            news.setType(News.NewsType.CONTENT);

            try {
                if (child.children().size() == 1) {
                    Element cc = child.child(0);
                    if (cc.tagName().equals("b")) {
                        news.setType(News.NewsType.BOLD_TITLE);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            news.setContent(child.outerHtml());
            newses.add(news);
        }
        newsDto.setNewses(newses);
        return newsDto;
    }
}
