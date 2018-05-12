package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import dao.UtilDao;
import entity.Acc;

@Service
public class AccService {

	@Autowired
	private UtilDao ud;
	
	/**
	 * 根据条件查找
	 * @param map
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Acc> paramQuery(Map<String,String> map,int page,int pageSize){
		String sql = "select * from acc where method='INVITE'";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_fromUri")!=null&&!map.get("map_fromUri").isEmpty()){
				sql += " and from_uri like ?";
				list.add("%"+map.get("map_fromUri")+"%");
			}
			if(map.get("map_toUri")!=null&&!map.get("map_toUri").isEmpty()){
				sql += " and to_uri like ?";
				list.add("%"+map.get("map_toUri")+"%");
			}
		}
		sql += " order by id desc limit "+page*pageSize+","+pageSize+"";
		Object[] o = list.toArray();
		return (List<Acc>) ud.queryList(sql,o,new BeanPropertyRowMapper(Acc.class));
	}
	
	/**
	 * 获取总条数
	 * @param map
	 * @return
	 */
	public int paramQueryCount(Map<String,String> map){
		String sql = "select count(1) from acc where method='INVITE'";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_fromUri")!=null&&!map.get("map_fromUri").isEmpty()){
				sql += " and from_uri like ?";
				list.add("%"+map.get("map_fromUri")+"%");
			}
			if(map.get("map_toUri")!=null&&!map.get("map_toUri").isEmpty()){
				sql += " and to_uri like ?";
				list.add("%"+map.get("map_toUri")+"%");
			}
		}
		Object[] o = list.toArray();
		return ud.queryInt(sql, o);
	}
}
