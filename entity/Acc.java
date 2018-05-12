package entity;

import java.util.Date;

public class Acc {
	private int id;
	private String method;
	private String fromUri;
	private String toUri;
	private String fromTag;
	private String toTag;
	private String callid;
	private String sipCode;
	private String sipReason;
	private Date time;
	private int duration;
	private int setuptime;
	private Date created;
	private String fromUriSub;
	private String toUriSub;
	public String getFromUriSub() {
		return fromUriSub;
	}
	public void setFromUriSub(String fromUriSub) {
		this.fromUriSub = fromUriSub;
	}
	public String getToUriSub() {
		return toUriSub;
	}
	public void setToUriSub(String toUriSub) {
		this.toUriSub = toUriSub;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFromUri() {
		return fromUri;
	}
	public void setFromUri(String fromUri) {
		this.fromUri = fromUri;
		try {
			int b = fromUri.indexOf(":")+1;
			int e = fromUri.indexOf("@");
			this.fromUriSub = fromUri.substring(b,e);
		} catch (Exception e) {
			this.fromUriSub = fromUri;
		}
	}
	public String getToUri() {
		return toUri;
	}
	public void setToUri(String toUri) {
		this.toUri = toUri;
		try {
			int b = toUri.indexOf(":")+1;
			int e = toUri.indexOf("@");
			this.toUriSub = toUri.substring(b,e);
		} catch (Exception e) {
			this.toUriSub = toUri;
		}
	}
	public String getFromTag() {
		return fromTag;
	}
	public void setFromTag(String fromTag) {
		this.fromTag = fromTag;
	}
	public String getToTag() {
		return toTag;
	}
	public void setToTag(String toTag) {
		this.toTag = toTag;
	}
	public String getCallid() {
		return callid;
	}
	public void setCallid(String callid) {
		this.callid = callid;
	}
	public String getSipCode() {
		return sipCode;
	}
	public void setSipCode(String sipCode) {
		this.sipCode = sipCode;
	}
	public String getSipReason() {
		return sipReason;
	}
	public void setSipReason(String sipReason) {
		this.sipReason = sipReason;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getSetuptime() {
		return setuptime;
	}
	public void setSetuptime(int setuptime) {
		this.setuptime = setuptime;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}

}
