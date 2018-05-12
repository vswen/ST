package entity;

import java.util.Date;

public class Weather {
	private float temp;
	private float humi;
	private float radi;
	private float rait;
	private float raif;
	private float winr;
	private float wins;
	private float dens;
	private float enth;
	private float pres;
	private Date createTime;
	private String tempUnit;
	private String humiUnit;
	private String radiUnit;
	private String raitUnit;
	private String raifUnit;
	private String winrUnit;
	private String winsUnit;
	private String densUnit;
	private String enthUnit;
	private String presUnit;
	private String tempName;
	private String humiName;
	private String radiName;
	private String raitName;
	private String raifName;
	private String winrName;
	private String winsName;
	private String densName;
	private String enthName;
	private String presName;
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	public float getHumi() {
		return humi;
	}
	public void setHumi(float humi) {
		this.humi = humi;
	}
	public float getRadi() {
		return radi;
	}
	public void setRadi(float radi) {
		this.radi = radi;
	}
	public float getRait() {
		return rait;
	}
	public void setRait(float rait) {
		this.rait = rait;
	}
	public float getRaif() {
		return raif;
	}
	public void setRaif(float raif) {
		this.raif = raif;
	}
	public float getWinr() {
		return winr;
	}
	public void setWinr(float winr) {
		this.winr = winr;
	}
	public float getWins() {
		return wins;
	}
	public void setWins(float wins) {
		this.wins = wins;
	}
	public float getDens() {
		return dens;
	}
	public void setDens(float dens) {
		this.dens = dens;
	}
	public float getEnth() {
		return enth;
	}
	public void setEnth(float enth) {
		this.enth = enth;
	}
	public float getPres() {
		return pres;
	}
	public void setPres(float pres) {
		this.pres = pres;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTempUnit() {
		if(tempUnit == null){
			return "°C";
		}
		return tempUnit;
	}
	public void setTempUnit(String tempUnit) {
		this.tempUnit = tempUnit;
	}
	public String getHumiUnit() {
		if(humiUnit == null){
			return "%";
		}
		return humiUnit;
	}
	public void setHumiUnit(String humiUnit) {
		this.humiUnit = humiUnit;
	}
	public String getRadiUnit() {
		if(radiUnit == null){
			return "w/m2";
		}
		return radiUnit;
	}
	public void setRadiUnit(String radiUnit) {
		this.radiUnit = radiUnit;
	}
	public String getRaitUnit() {
		if(raitUnit == null){
			return "mm/h";
		}
		return raitUnit;
	}
	public void setRaitUnit(String raitUnit) {
		this.raitUnit = raitUnit;
	}
	public String getRaifUnit() {
		if(raifUnit == null){
			return "mm";
		}
		return raifUnit;
	}
	public void setRaifUnit(String raifUnit) {
		this.raifUnit = raifUnit;
	}
	public String getWinrUnit() {
		if(winrUnit == null){
			return "度";
		}
		return winrUnit;
	}
	public void setWinrUnit(String winrUnit) {
		this.winrUnit = winrUnit;
	}
	public String getWinsUnit() {
		if(winsUnit == null){
			return "m/s";
		}
		return winsUnit;
	}
	public void setWinsUnit(String winsUnit) {
		this.winsUnit = winsUnit;
	}
	public String getDensUnit() {
		if(densUnit == null){
			return "kg/m3";
		}
		return densUnit;
	}
	public void setDensUnit(String densUnit) {
		this.densUnit = densUnit;
	}
	public String getEnthUnit() {
		if(enthUnit == null){
			return "kj/kg";
		}
		return enthUnit;
	}
	public void setEnthUnit(String enthUnit) {
		this.enthUnit = enthUnit;
	}
	public String getPresUnit() {
		if(presUnit == null){
			return "hPa";
		}
		return presUnit;
	}
	public void setPresUnit(String presUnit) {
		this.presUnit = presUnit;
	}
	public String getTempName() {
		if(tempName == null){
			return "空气温度";
		}
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getHumiName() {
		if(humiName == null){
			return "相对湿度";
		}
		return humiName;
	}
	public void setHumiName(String humiName) {
		this.humiName = humiName;
	}
	public String getRadiName() {
		if(radiName == null){
			return "紫外线强度";
		}
		return radiName;
	}
	public void setRadiName(String radiName) {
		this.radiName = radiName;
	}
	public String getRaitName() {
		if(raitName == null){
			return "雨雪强度";
		}
		return raitName;
	}
	public void setRaitName(String raitName) {
		this.raitName = raitName;
	}
	public String getRaifName() {
		if(raifName == null){
			return "雨量";
		}
		return raifName;
	}
	public void setRaifName(String raifName) {
		this.raifName = raifName;
	}
	public String getWinrName() {
		if(winrName == null){
			return "风向";
		}
		return winrName;
	}
	public void setWinrName(String winrName) {
		this.winrName = winrName;
	}
	public String getWinsName() {
		if(winsName == null){
			return "风速";
		}
		return winsName;
	}
	public void setWinsName(String winsName) {
		this.winsName = winsName;
	}
	public String getDensName() {
		if(densName == null){
			return "空气密度";
		}
		return densName;
	}
	public void setDensName(String densName) {
		this.densName = densName;
	}
	public String getEnthName() {
		if(enthName == null){
			return "比热含量";
		}
		return enthName;
	}
	public void setEnthName(String enthName) {
		this.enthName = enthName;
	}
	public String getPresName() {
		if(presName == null){
			return "空气压力";
		}
		return presName;
	}
	public void setPresName(String presName) {
		this.presName = presName;
	}
}
