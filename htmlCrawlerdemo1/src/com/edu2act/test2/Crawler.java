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
 * ʵ���Լ����������棬ץȡ���������š���http://news.sina.com.cn/����վ�Ĳ�����Ϣ��
 * 1����Ҫץȡ��Ϣ��������ַ,���⣬ʱ�䣬��ҳ���ݡ��������·���� 
 * 2����ץȡ������д��excel���С�
 */

public class Crawler extends BreadthCrawler {

	public Crawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	static int j = 0;
	static int k = 0;
	Content cons[] = new Content[100];// ��ץȡ����Ϣ����һ��Content����������

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

		// ��Excel�ļ������ڹ������ڸ�Ŀ¼��
		File file = new File("G:/A�����/��������/htmlCrawlerdemo1/test.xls");

		String comment = "";
		String path = file.toString();

		System.out.println("��ַ��" + url);
		System.out.println("���⣺" + title);
		System.out.println("ʱ�䣺" + time);
		System.out.println("��ҳ���ݣ�" + content);
		System.out.println("�������·����" + path);

		cons[j++] = new Content(url, title, comment, time, content, path);

		// ���õ�jΪ10��ʱ��ʹ���excel��񣬲��������
		if (j == 10) {
			try {
				Workbook wb = new HSSFWorkbook();

				FileOutputStream out = new FileOutputStream(file);

				// ����һ��SHEET
				Sheet sheet1 = wb.createSheet("���ݽ���");
				String[] con = { "��ַ", "����", "����", "ʱ��", "����", "�������·��" };

				int i = 0;

				// ����һ��
				Row row = sheet1.createRow((short) 0);

				// ������
				for (String s : con) {
					Cell cell = row.createCell(i);
					cell.setCellValue(s);
					i++;
				}

				// ѭ���������������
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
