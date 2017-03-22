package com.edu2act.test1;

import java.io.IOException;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;

/**
 * ����������ã�
 *  1��������ȡ��� 
 *  2�����ÿ������߳���
 *  3��������ȡ��URL����
 *  4��ͨ��������ʽ������ȡ��Щ��ҳ������ȡ��Щ��ҳ�ȣ�eg����Ҫ��ȡjpg|png|gif��
 */

public class Crawler extends BreadthCrawler {

	public Crawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		try {
			System.out.println("������ȡ��ҳ���URL��" + page.getUrl()); // ��ַ

			String title = page.getDoc().title();// ����

			System.out.println("���⣺" + title);

			// ����ȡ������д��*.HTMLҳ,���ص��ļ�����Ŀ¼���½��ļ���downloads��
			// FileUtilsʵ�����ص�����
			FileUtils.writeFileWithParent("downloads//" + title + ".html", page.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// �����������
		Crawler crawler = new Crawler("html_crawler", true);

		// ��ʼҳ
		crawler.addSeed("http://software.hebtu.edu.cn");

		// �ҵ���ʽ��http://software.hebtu.edu.cn/.*��ҳ��
		crawler.addRegex("http://software.hebtu.edu.cn/.*");

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
			crawler.start(3);// ������ȡ���Ϊ3
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
