package entity;

import java.util.Date;

public class CollStat {

	private int id;
	private String sipName;
	private String collContent;
	private String collType;
	private Date createTime;
	private String details;
	private String domain;
	private int isRead;
	private String address;
	private String roomNumb;
	
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCollContent() {
		return collContent;
	}
	public void setCollContent(String collContent) {
		this.collContent = collContent;
	}
	public String getCollType() {
		return collType;
	}
	public void setCollType(String collType) {
		this.collType = collType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public void setSipName(String sipName) {
		this.sipName = sipName;
	}
	public String getSipName() {
		return sipName;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDomain() {
		return domain;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setRoomNumb(String roomNumb) {
		this.roomNumb = roomNumb;
	}
	public String getRoomNumb() {
		return roomNumb;
	}
	
}
