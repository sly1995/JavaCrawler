package com.edu2act.test3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * 目的：将抓取的数据直接写到数据库中 。
 * 遇到的问题及解决办法：
 *     1、用Statement对象进行插入操作时，values()的参数不能是String类型的，
 *        所以用PreparedStatement对象做插入数据可以插入String类型的参数。
 *     2、在建立数据库时，要点击mysql.exe，在命令行中建立数据库，而不是在客户端DBeaver中建立数据库。
 */

public class Crawler extends BreadthCrawler {

	public Crawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	List<Content2> list = new ArrayList<Content2>();
	static int j = 0;

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

		String comment = "";

		System.out.println("网址：" + url);
		System.out.println("标题：" + title);
		System.out.println("时间：" + time);
		System.out.println("网页内容：" + content);

		Content2 c = new Content2(url, title, time, content, comment);
		list.add(c);
		j++;

		// 设置当j为10的时候就创建excel表格，并填充数据
		if (j == 10) {
			try {
				// 1、加载驱动
				Class.forName("com.mysql.jdbc.Driver");

				// 2、获取数据库连接
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crawlertest", "root", "123");

				// 3、数据操作
				Statement stmt = con.createStatement();

				// 建表
				stmt.execute(
						"create table t_mess(url varchar(255) not null, title varchar(255) ,comment varchar(255),date varchar(255),content TEXT(10000));");

				// 循环在表中插入数据
				for (int i = 0; i < list.size(); i++) {
					PreparedStatement ps = con
							.prepareStatement("insert into t_mess(url,title,comment,date,content) values(?,?,?,?,?)");
					ps.setString(1, list.get(i).getUrl());
					ps.setString(2, list.get(i).getTitle());
					ps.setString(3, list.get(i).getComment());
					ps.setString(4, list.get(i).getTime());
					ps.setString(5, list.get(i).getContent());
					ps.executeUpdate();
				}

				// 4、关闭数据库连接
				stmt.close();
				con.close();

				System.exit(0);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
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
