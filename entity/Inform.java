package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inform {
	private int id;
	private String title;
	private String content;
	/**
	 * 1.文本  2.图片 3.文本图片混合 
	 */
	private int type;
	private String publisher;
	private String pubTime;
	private String endTime;
	private String createTime;
	private int deleted;
	private String contentStr;
	private List<String> contentUrls;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		List<String> list = new ArrayList<String>();
		Pattern r = Pattern.compile("<img.*?/>");
		Matcher m = r.matcher(content);
		while (m.find()) {
			Matcher m2 = Pattern.compile("x=\"(.*?)\" src=\"(.*?)\"").matcher(m.group());
			if (m2.find()) {
				list.add(m2.group(2));
			}
		}
		contentUrls = list;
		contentStr = m.replaceAll("");
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		if("".equals(pubTime)){
			this.pubTime = null;
		}else{
			this.pubTime = pubTime;
		}
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		if("".equals(endTime)){
			this.endTime = null;
		}else{
			this.endTime = endTime;
		}
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}
	public String getContentStr() {
		return contentStr;
	}
	public void setContentUrls(List<String> contentUrls) {
		this.contentUrls = contentUrls;
	}
	public List<String> getContentUrls() {
		return contentUrls;
	}

}
