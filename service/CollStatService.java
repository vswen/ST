package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dao.UtilDao;
import entity.CollStat;

@Service
@Transactional
public class CollStatService {
	
	@Autowired
	private UtilDao ud;
	
	public int add(CollStat cs){
		String sql = "insert into t_coll_stat values(null,?,?,?,now(),?,0,?,?)";
		Object[] o = new Object[]{cs.getSipName(),cs.getCollContent(),cs.getCollType(),cs.getDetails(),cs.getAddress(),cs.getRoomNumb()};
		return ud.insertReturnKeys(sql, o);
	}
	
	/**
	 * 根据条件查找
	 * @param map
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CollStat> paramQuery(Map<String,String> map,int page,int pageSize){
		String sql = "select a.id,a.sip_name,a.coll_content,a.coll_type,a.create_time,a.details,a.is_read,b.domain," +
				"a.address,a.room_numb from t_coll_stat a left join subscriber b on a.sip_name=b.username where 1=1";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_sipName")!=null&&!map.get("map_sipName").isEmpty()){
				sql += " and a.sip_name=?";
				list.add(map.get("map_sipName"));
			}
			if(map.get("map_begin_time")!=null&&!"".equals(map.get("map_begin_time"))){
				sql += " and create_time >= ?";
				list.add(map.get("map_begin_time")+" 24:00:00");
			}
			if(map.get("map_end_time")!=null&&!"".equals(map.get("map_end_time"))){
				sql += " and create_time <= ?";
				list.add(map.get("map_end_time")+" 24:00:00");
			}
			if(map.get("map_is_read")!=null&&!"".equals(map.get("map_is_read"))){
				sql += " and a.is_read = ?";
				list.add(map.get("map_is_read"));
			}
			if(map.get("map_address")!=null&&!map.get("map_address").isEmpty()){
				sql += " and a.address like ?";
				list.add("%"+map.get("map_address")+"%");
			}
			if(map.get("map_roomNumb")!=null&&!map.get("map_roomNumb").isEmpty()){
				sql += " and a.room_numb = ?";
				list.add(map.get("map_roomNumb"));
			}
		}
		sql += " order by a.is_read,a.id desc limit "+page*pageSize+","+pageSize+"";
		Object[] o = list.toArray();
		return (List<CollStat>) ud.queryList(sql,o,new BeanPropertyRowMapper(CollStat.class));
	}
	
	/**
	 * 获取总条数
	 * @param map
	 * @return
	 */
	public int paramQueryCount(Map<String,String> map){
		String sql = "select count(1) from t_coll_stat where 1=1";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_sipName")!=null&&!map.get("map_sipName").isEmpty()){
				sql += " and sip_name=?";
				list.add(map.get("map_sipName"));
			}
			if(map.get("map_begin_time")!=null&&!"".equals(map.get("map_begin_time"))){
				sql += " and create_time >= ?";
				list.add(map.get("map_begin_time")+" 24:00:00");
			}
			if(map.get("map_end_time")!=null&&!"".equals(map.get("map_end_time"))){
				sql += " and create_time <= ?";
				list.add(map.get("map_end_time")+" 24:00:00");
			}
			if(map.get("map_address")!=null&&!map.get("map_address").isEmpty()){
				sql += " and address like ?";
				list.add("%"+map.get("map_address")+"%");
			}
			if(map.get("map_roomNumb")!=null&&!map.get("map_roomNumb").isEmpty()){
				sql += " and room_numb = ?";
				list.add("%"+map.get("map_roomNumb")+"%");
			}
		}
		Object[] o = list.toArray();
		return ud.queryInt(sql, o);
	}
	
	/**
	 * 修改为已读
	 * @param id
	 * @return
	 */
	public boolean upRead(int id){
		String sql = "update t_coll_stat set is_read=1 where id=?";
		Object[] o = new Object[]{id};
		return ud.executeNonQuery(sql, o)>0;
	}
	
	/**
	 * 查询未读警报信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CollStat> getByRead(){
		String sql = "select * from t_coll_stat where is_read=0";
		return (List<CollStat>) ud.queryList(sql,null,new BeanPropertyRowMapper(CollStat.class));
	}
	
	/**
	 * 查询未读警报总条数
	 * @return
	 */
	public int countRead(){
		String sql = "select count(1) from t_coll_stat where is_read=0";
		return ud.queryInt(sql, null);
	}
}
