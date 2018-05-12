package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import dao.UtilDao;
import entity.Inform;

@Service
public class InformService {
	
	@Autowired
	private UtilDao ud;
	
	public int add(Inform inform){
		String sql = "insert into t_inform(title,content,type,publisher,pub_time,end_time,create_time,deleted) values(?,?,?,?,?,?,now(),0)";
		Object[] o = new Object[]{inform.getTitle(),inform.getContent(),inform.getType(),
				inform.getPublisher(),inform.getPubTime(),inform.getEndTime()};
		return ud.insertReturnKeys(sql, o);
	}
	
	public int del(int id){
		String sql = "delete from t_inform where id=?";
		Object[] o = new Object[]{id};
		return ud.executeNonQuery(sql, o);
	}
	
	public int up(Inform inform){
		StringBuffer sb = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		if(inform.getTitle()!=null&&!"".equals(inform.getTitle())){
			sb.append("title = ?,");
			list.add(inform.getTitle());
		}
		if(inform.getContent()!=null&&!"".equals(inform.getContent())){
			sb.append("content = ?,");
			list.add(inform.getContent());
		}
		if(inform.getType()!=0){
			sb.append("type = ?,");
			list.add(inform.getType());
		}
		if(inform.getPublisher()!=null&&!"".equals(inform.getPublisher())){
			sb.append("publisher = ?,");
			list.add(inform.getPublisher());
		}
		if(inform.getPubTime()!=null&&!"".equals(inform.getPubTime())){
			sb.append("pub_time = ?,");
			list.add(inform.getPubTime());
		}
		if(inform.getEndTime()!=null&&!"".equals(inform.getEndTime())){
			sb.append("end_time = ?,");
			list.add(inform.getEndTime());
		}
		sb.append("deleted = ?,");
		list.add(inform.getDeleted());
		sb.deleteCharAt(sb.length()-1);
		list.add(inform.getId());
		String sql = "update t_inform set "+sb.toString()+" where id=?";
		Object[] o = list.toArray();
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
	public List<Inform> paramQuery(Map<String,String> map,int page,int pageSize){
		String sql = "select * from t_inform where 1=1";
		List<Object> list = new ArrayList<Object>();
		if(map != null){
			if(map.get("map_begin_time")!=null&&!"".equals(map.get("map_begin_time"))){
				sql += " and create_time >= ?";
				list.add(map.get("map_begin_time")+" 24:00:00");
			}
			if(map.get("map_end_time")!=null&&!"".equals(map.get("map_end_time"))){
				sql += " and create_time <= ?";
				list.add(map.get("map_end_time")+" 24:00:00");
			}
			if(map.get("map_publisher")!=null&&!"".equals(map.get("map_publisher"))){
				sql += " and publisher like ?";
				list.add("%"+map.get("map_publisher")+"%");
			}
		}
		sql += " ORDER BY id DESC limit "+page*pageSize+","+pageSize+"";
		Object[] o = list.toArray();
		return (List<Inform>) ud.queryList(sql,o,new BeanPropertyRowMapper(Inform.class));
	}
	
	/**
	 * 获取总条数
	 * @param map
	 * @return
	 */
	public int paramQueryCount(Map<String,String> map){
		String sql = "select count(1) from t_inform where 1=1";
		List<Object> list = new ArrayList<Object>();
		if(map != null){
			if(map.get("map_begin_time")!=null&&!"".equals(map.get("map_begin_time"))){
				sql += " and create_time >= ?";
				list.add(map.get("map_begin_time")+" 24:00:00");
			}
			if(map.get("map_end_time")!=null&&!"".equals(map.get("map_end_time"))){
				sql += " and create_time <= ?";
				list.add(map.get("map_end_time")+" 24:00:00");
			}
			if(map.get("map_publisher")!=null&&!"".equals(map.get("map_publisher"))){
				sql += " and publisher like ?";
				list.add("%"+map.get("map_publisher")+"%");
			}
		}
		Object[] o = list.toArray();
		return ud.queryInt(sql, o);
	}
	
	public Inform getById(int id){
		String sql = "select * from inform where id=?";
		Object[] o = new Object[]{id};
		return (Inform) ud.queryObject(sql, o, new BeanPropertyRowMapper<Inform>(Inform.class));
	}
	
	/**
	 * 根据条件获取物业通知数据
	 * @param type 
	 * 0.返回最新100条的数据  
	 * 1.返回未删除并id>x的数据
	 * 2.返回未删除最新x条数据
	 * 3.返回发布时间 结束时间 未删除 的最新一条数据
	 * @param x
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Inform> conditionShow(int type,int x){
		String sql = "select * from t_inform where 1=1";
		switch (type) {
			case 0:
				sql += " ORDER BY id DESC LIMIT 100";
				break;
			case 1:
				sql += " AND deleted=0 AND id>"+x+"";
				break;
			case 2:
				sql += " AND deleted=0 ORDER BY id DESC LIMIT "+x+"";
				break;
			case 3:
				sql += " AND id=(SELECT MAX(id) FROM t_inform WHERE (pub_time<=NOW() OR pub_time is NULL) AND (end_time>=NOW() OR end_time is NULL) AND deleted=0)";
				break;
			case 4:
				sql += " AND deleted=0 ORDER BY id DESC";
				break;	
			default:
				sql += " AND 1<>1";
		}
		return (List<Inform>) ud.queryList(sql,null,new BeanPropertyRowMapper(Inform.class));
	}
}
