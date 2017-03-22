package com.edu2act.test1;

import java.io.IOException;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;

/**
 * 进行相关设置：
 *  1、设置爬取深度 
 *  2、设置开启的线程数
 *  3、设置爬取的URL上限
 *  4、通过正则表达式设置爬取哪些网页，不爬取哪些网页等（eg：不要爬取jpg|png|gif）
 */

public class Crawler extends BreadthCrawler {

	public Crawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		try {
			System.out.println("正在提取的页面的URL：" + page.getUrl()); // 网址

			String title = page.getDoc().title();// 标题

			System.out.println("标题：" + title);

			// 将爬取的内容写到*.HTML页,下载到文件所在目录的新建文件夹downloads下
			// FileUtils实现下载到本地
			FileUtils.writeFileWithParent("downloads//" + title + ".html", page.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// 创建爬虫对象
		Crawler crawler = new Crawler("html_crawler", true);

		// 开始页
		crawler.addSeed("http://software.hebtu.edu.cn");

		// 找到格式如http://software.hebtu.edu.cn/.*的页面
		crawler.addRegex("http://software.hebtu.edu.cn/.*");

		// 通过正则表达式设置不要爬取JPG|PNG|GIF 的网页
		crawler.addRegex("-.*\\.(jpg|png|gif).*");

		// 不要爬取包含"#"的链接
		crawler.addRegex("-.*#.*");

		// 设置抓取URL上限为12个
		crawler.setTopN(12);
		crawler.setAutoParse(true);

		// 设置启动的线程数为5
		crawler.setThreads(5);

		try {
			// 启动 爬虫
			crawler.start(3);// 设置爬取深度为3
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
