package service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.Tools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.UtilDao;
import entity.Weather;
import entity.WeatherAvg;

@Service
@Transactional
public class WeatherService {
	
	@Autowired
	private UtilDao ud;
	
	public int add(Weather w){
		String sql = "insert into t_weather(temp,humi,radi,rait,raif,winr,wins,dens,enth,pres,create_time) " +
				"values(?,?,?,?,?,?,?,?,?,?,now())";
		Object[] o = new Object[]{w.getTemp(),w.getHumi(),w.getRadi(),w.getRait(),w.getRaif(),w.getWinr(),
				w.getWins(),w.getDens(),w.getEnth(),w.getPres()};
		return ud.executeNonQuery(sql, o);
	}
	
	public int add_Avg(WeatherAvg wAvg){
		String sql = "insert into t_weather_avg(temp_avg,humi_avg,radi_avg,rait_avg,raif_avg,winr_avg,wins_avg," +
				"dens_avg,enth_avg,pres_avg,create_time,begin_time,end_time) values(?,?,?,?,?,?,?,?,?,?,now(),?,?)";
		Object[] o = new Object[]{wAvg.getTempAvg(),wAvg.getHumiAvg(),wAvg.getRadiAvg(),wAvg.getRaitAvg(),
				wAvg.getRaifAvg(),wAvg.getWinrAvg(),wAvg.getWinsAvg(),wAvg.getDensAvg(),wAvg.getEnthAvg(),
				wAvg.getPresAvg(),wAvg.getBeginTime(),wAvg.getEndTime()};
		return ud.insertReturnKeys(sql, o);
	}
	
	/**
	 * 删除指定日期数据 
	 * @param time 格式2017-12-06
	 * @return
	 */
	public boolean del(String time){
		String sql = "delete from t_weather where DATE_FORMAT(create_time,'%Y-%m-%d')=?";
		Object[] o = new Object[]{time};
		return ud.executeNonQuery(sql, o)>0;
	}
	
	/**
	 * 获取最后一条数据
	 * @return
	 */
	public Weather getMaxData(){
		String sql = "select * from t_weather order by create_time desc limit 1";
		return (Weather) ud.queryObject(sql, null, new BeanPropertyRowMapper<Weather>(Weather.class));
	}
	
	/**
	 * GROUP BY create_time
	 * @return
	 */
	public List<String> getGroupTime(){
		String sql = "SELECT DATE_FORMAT(create_time,'%Y-%m-%d') FROM t_weather GROUP BY DATE_FORMAT(create_time,'%y %m %d')";
		SqlRowSet srs = ud.executeQuery(sql, null);
		List<String> list = new ArrayList<String>();
		while (srs.next()) {
			list.add(srs.getString(1));
		}
		return list;
	}
	
	/**
	 * 获取指定日期 最小创建时间，最大创建时间，各列平均值
	 * @param time 格式2017-12-06
	 * @return
	 */
	public JSONObject getDataFunction(String time){
		String sql = "SELECT MIN(create_time),MAX(create_time),AVG(temp),AVG(humi),AVG(radi),AVG(rait),AVG(raif)," +
				"AVG(winr),AVG(wins),AVG(dens),AVG(enth),AVG(pres) FROM t_weather WHERE DATE_FORMAT(create_time,'%Y-%m-%d')=?";
		Object[] o = new Object[]{time};
		SqlRowSet srs = ud.executeQuery(sql, o);
		while (srs.next()) {
			JSONObject json = new JSONObject();
			json.put("beginTime", srs.getTimestamp(1));
			json.put("endTime", srs.getTimestamp(2));
			json.put("tempAvg", srs.getFloat(3));
			json.put("humiAvg", srs.getFloat(4));
			json.put("radiAvg", srs.getFloat(5));
			json.put("raitAvg", srs.getFloat(6));
			json.put("raifAvg", srs.getFloat(7));
			json.put("winrAvg", srs.getFloat(8));
			json.put("winsAvg", srs.getFloat(9));
			json.put("densAvg", srs.getFloat(10));
			json.put("enthAvg", srs.getFloat(11));
			json.put("presAvg", srs.getFloat(12));
			return json;
		}
		return null;
	}
	
	/**
	 * 将同一天的数据进行统计 （本日除外）
	 * 	写入统计表
	 * 	删除原始数据
	 * @return
	 */
	public boolean helpWeather(){
		String curDate = Tools.getCurDate();
		List<String> list = this.getGroupTime();
		for (String time : list) {
			if(curDate.equals(time)){
				continue;
			}
			JSONObject json = this.getDataFunction(time);
			WeatherAvg wAvg = JSON.toJavaObject(json, WeatherAvg.class);
			this.add_Avg(wAvg);
			this.del(time);
		}
		return true;
	}
}
