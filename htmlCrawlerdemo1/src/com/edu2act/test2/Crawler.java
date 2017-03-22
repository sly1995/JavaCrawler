package com.edu2act.test2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * 实现自己的网络爬虫，抓取“新浪新闻”（http://news.sina.com.cn/）网站的部分信息。
 * 1、需要抓取信息包括：网址,标题，时间，网页内容、本地相对路径。 
 * 2、将抓取的数据写到excel表中。
 */

public class Crawler extends BreadthCrawler {

	public Crawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	static int j = 0;
	static int k = 0;
	Content cons[] = new Content[100];// 将抓取的信息放入一个Content类型数组中

	@Override
	public void visit(Page page, CrawlDatums next) {
		// 获取网址
		String url = page.getUrl();

		// 获取标题
		String title = page.getDoc().title();

		// 通过正则表达式获取时间
		String time = page.getDoc().select("span[id=navtimeSource]").text();

		// 通过正则表达式获取网页内容
		String content = page.getDoc().select("div[id=artibody]>p").text();

		// 将Excel文件创建在工程所在根目录下
		File file = new File("G:/A方向课/网络爬虫/htmlCrawlerdemo1/test.xls");

		String comment = "";
		String path = file.toString();

		System.out.println("网址：" + url);
		System.out.println("标题：" + title);
		System.out.println("时间：" + time);
		System.out.println("网页内容：" + content);
		System.out.println("本地相对路径：" + path);

		cons[j++] = new Content(url, title, comment, time, content, path);

		// 设置当j为10的时候就创建excel表格，并填充数据
		if (j == 10) {
			try {
				Workbook wb = new HSSFWorkbook();

				FileOutputStream out = new FileOutputStream(file);

				// 创建一个SHEET
				Sheet sheet1 = wb.createSheet("数据解析");
				String[] con = { "网址", "标题", "评论", "时间", "内容", "本地相对路径" };

				int i = 0;

				// 创建一行
				Row row = sheet1.createRow((short) 0);

				// 填充标题
				for (String s : con) {
					Cell cell = row.createCell(i);
					cell.setCellValue(s);
					i++;
				}

				// 循环创建行填充数据
				for (int k = 0; k < j; k++) {
					AtomicInteger id = new AtomicInteger(k);
					Row row2 = sheet1.createRow((short) id.incrementAndGet());
					row2.createCell(0).setCellValue(cons[k].getUrl());
					row2.createCell(1).setCellValue(cons[k].getTitle());
					row2.createCell(2).setCellValue(cons[k].getComment());
					row2.createCell(3).setCellValue(cons[k].getTime());
					row2.createCell(4).setCellValue(cons[k].getContent());
					row2.createCell(5).setCellValue(cons[k].getPath());
				}
				wb.write(out);
				wb.close();
				System.exit(0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// 创建爬虫对象a
		Crawler crawler = new Crawler("html_crawler", true);

		// 开始页
		crawler.addSeed("http://news.sina.com.cn");

		// 找到格式如http://news.sina.com.cn/.*的页面
		crawler.addRegex("http://news.sina.com.cn/c/.*");

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
			crawler.start(5);// 设置爬取深度为5
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
