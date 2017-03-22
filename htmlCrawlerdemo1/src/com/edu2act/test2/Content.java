package com.edu2act.test2;

public class Content {
	//  Ù–‘
	private String url;
	private String title;
	private String comment;
	private String time;
	private String content;
	private String path;

	// ∑Ω∑®
	public Content(String url, String title, String comment, String time, String content, String path) {
		this.url = url;
		this.title = title;
		this.comment = comment;
		this.time = time;
		this.content = content;
		this.path = path;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
