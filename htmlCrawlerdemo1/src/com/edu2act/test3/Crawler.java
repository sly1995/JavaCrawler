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
 * Ŀ�ģ���ץȡ������ֱ��д�����ݿ��� ��
 * ���������⼰����취��
 *     1����Statement������в������ʱ��values()�Ĳ���������String���͵ģ�
 *        ������PreparedStatement�������������ݿ��Բ���String���͵Ĳ�����
 *     2���ڽ������ݿ�ʱ��Ҫ���mysql.exe�����������н������ݿ⣬�������ڿͻ���DBeaver�н������ݿ⡣
 */

public class Crawler extends BreadthCrawler {

	public Crawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	List<Content2> list = new ArrayList<Content2>();
	static int j = 0;

	@Override
	public void visit(Page page, CrawlDatums next) {

		// ��ȡ��ַ
		String url = page.getUrl();

		// ��ȡ����
		String title = page.getDoc().title();

		// ͨ��������ʽ��ȡʱ��
		String time = page.getDoc().select("span[id=navtimeSource]").text();

		// ͨ��������ʽ��ȡ��ҳ����
		String content = page.getDoc().select("div[id=artibody]>p").text();

		String comment = "";

		System.out.println("��ַ��" + url);
		System.out.println("���⣺" + title);
		System.out.println("ʱ�䣺" + time);
		System.out.println("��ҳ���ݣ�" + content);

		Content2 c = new Content2(url, title, time, content, comment);
		list.add(c);
		j++;

		// ���õ�jΪ10��ʱ��ʹ���excel��񣬲��������
		if (j == 10) {
			try {
				// 1����������
				Class.forName("com.mysql.jdbc.Driver");

				// 2����ȡ���ݿ�����
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crawlertest", "root", "123");

				// 3�����ݲ���
				Statement stmt = con.createStatement();

				// ����
				stmt.execute(
						"create table t_mess(url varchar(255) not null, title varchar(255) ,comment varchar(255),date varchar(255),content TEXT(10000));");

				// ѭ���ڱ��в�������
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

				// 4���ر����ݿ�����
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
		// �����������a
		Crawler crawler = new Crawler("html_crawler", true);

		// ��ʼҳ
		crawler.addSeed("http://news.sina.com.cn");

		// �ҵ���ʽ��http://news.sina.com.cn/.*��ҳ��
		crawler.addRegex("http://news.sina.com.cn/c/.*");

		// ͨ��������ʽ���ò�Ҫ��ȡJPG|PNG|GIF ����ҳ
		crawler.addRegex("-.*\\.(jpg|png|gif).*");

		// ��Ҫ��ȡ����"#"������
		crawler.addRegex("-.*#.*");

		// ����ץȡURL����Ϊ12��
		crawler.setTopN(12);
		crawler.setAutoParse(true);

		// �����������߳���Ϊ5
		crawler.setThreads(5);

		try {
			// ���� ����
			crawler.start(5);// ������ȡ���Ϊ5
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
