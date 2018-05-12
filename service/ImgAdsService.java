package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dao.UtilDao;
import entity.ImgAds;

@Service
@Transactional
public class ImgAdsService {
	
	@Autowired
	private UtilDao ud;
	
	public int add(ImgAds ia){
		String sql = "insert into t_img_ads(file_name,play_length,order_value,play_time,end_time,create_time,deleted,details) " +
				"values(?,?,?,?,?,now(),0,?)";
		Object[] o = new Object[]{ia.getFileName(),ia.getPlayLength(),ia.getOrderValue(),ia.getPlayTime(),ia.getEndTime(),ia.getDetails()};
		return ud.insertReturnKeys(sql, o);
	}
	
	public int up(ImgAds ia){
		String sql = "update t_img_ads set play_length=?,order_value=?,play_time=?,end_time=?,deleted=? where id=?";
		Object[] o = new Object[]{ia.getPlayLength(),ia.getOrderValue(),ia.getPlayTime(),ia.getEndTime(),ia.getDeleted(),ia.getId()};
		return ud.executeNonQuery(sql, o);
	}
	
	/**
	 * 根据条件查找
	 * @param map
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImgAds> paramQuery(Map<String,String> map,int page,int pageSize){
		String sql = "select * from t_img_ads where 1=1";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_deleted")!=null&&!map.get("map_deleted").isEmpty()){
				sql += " and deleted=?";
				list.add(map.get("map_deleted"));
			}
			if(map.get("map_begin_time")!=null&&!"".equals(map.get("map_begin_time"))){
				sql += " and create_time >= ?";
				list.add(map.get("map_begin_time")+" 24:00:00");
			}
			if(map.get("map_end_time")!=null&&!"".equals(map.get("map_end_time"))){
				sql += " and create_time <= ?";
				list.add(map.get("map_end_time")+" 24:00:00");
			}
		}
		sql += " limit "+page*pageSize+","+pageSize+"";
		Object[] o = list.toArray();
		return (List<ImgAds>) ud.queryList(sql,o,new BeanPropertyRowMapper(ImgAds.class));
	}
	
	/**
	 * 获取总条数
	 * @param map
	 * @return
	 */
	public int paramQueryCount(Map<String,String> map){
		String sql = "select count(1) from t_img_ads where 1=1";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_deleted")!=null&&!map.get("map_deleted").isEmpty()){
				sql += " and deleted=?";
				list.add(map.get("map_deleted"));
			}
			if(map.get("map_begin_time")!=null&&!"".equals(map.get("map_begin_time"))){
				sql += " and create_time >= ?";
				list.add(map.get("map_begin_time")+" 24:00:00");
			}
			if(map.get("map_end_time")!=null&&!"".equals(map.get("map_end_time"))){
				sql += " and create_time <= ?";
				list.add(map.get("map_end_time")+" 24:00:00");
			}
		}
		Object[] o = list.toArray();
		return ud.queryInt(sql, o);
	}
	
	public ImgAds getById(int id){
		String sql = "select * from t_img_ads where id=?";
		Object[] o = new Object[]{id};
		return (ImgAds) ud.queryObject(sql, o, new BeanPropertyRowMapper<ImgAds>(ImgAds.class));
	}
	
	/**
	 * 获取未删除并在播放日期内的物业广告
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImgAds> show(){
		String sql = "select * from t_img_ads where (play_time<=now() or play_time is null) " +
				"and (end_time>=now() or end_time is null) and deleted=0 order by order_value desc";
		return (List<ImgAds>) ud.queryList(sql,null,new BeanPropertyRowMapper(ImgAds.class));
	}
	
}
