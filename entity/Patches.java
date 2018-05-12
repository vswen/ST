package entity;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Patches {
	private int id;
	private String version;
	private String fileName;
	private int downCount;
	private String verifyMd5;
	private Date createTime;
	private int deleted;
	private String details;
	private JSONObject detailsJson;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getDownCount() {
		return downCount;
	}
	public void setDownCount(int downCount) {
		this.downCount = downCount;
	}
	public String getVerifyMd5() {
		return verifyMd5;
	}
	public void setVerifyMd5(String verifyMd5) {
		this.verifyMd5 = verifyMd5;
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
		if(details != null){
			this.detailsJson = JSON.parseObject(details);
		}
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setDetailsJson(JSONObject detailsJson) {
		this.detailsJson = detailsJson;
	}
	public JSONObject getDetailsJson() {
		return detailsJson;
	}
	

}
