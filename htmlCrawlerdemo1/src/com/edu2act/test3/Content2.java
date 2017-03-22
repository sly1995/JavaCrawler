package com.edu2act.test3;

public class Content2 {
	//  Ù–‘
	private String url;
	private String title;
	private String comment;
	private String time;
	private String content;

	// ∑Ω∑®
	Content2(String url, String title, String time, String content ,String comment) {
		this.url = url;
		this.title = title;
		this.comment = comment;
		this.time = time;
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
