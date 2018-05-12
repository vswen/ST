package entity;

import java.util.Date;

import util.SysConstants;

public class ImgAds {

	private int id;
	private String fileName;
	private int playLength;
	private int orderValue;
	private Date playTime;
	private Date endTime;
	private Date createTime;
	private int deleted;
	private String details;
	private String fileUrl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getPlayLength() {
		return playLength;
	}
	public void setPlayLength(int playLength) {
		this.playLength = playLength;
	}
	public int getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(int orderValue) {
		this.orderValue = orderValue;
	}
	public Date getPlayTime() {
		return playTime;
	}
	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getFileUrl() {
		if(fileUrl == null){
			return SysConstants.ADS_FILE_URL+fileName;
		}
		return fileUrl;
	}

}
