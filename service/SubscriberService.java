package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dao.UtilDao;
import entity.Subscriber;

@Service
@Transactional
public class SubscriberService {
	
	@Autowired
	private UtilDao ud;
	
	public int add(Subscriber subscriber){
		String sql = "insert into subscriber(username,domain,password,email_address,ha1,ha1b,rpid) values(?,?,?,?,?,?,?)";
		Object[] o = new Object[]{subscriber.getUserName(),subscriber.getDomain(),subscriber.getPassword(),
				subscriber.getEmailAddress(),subscriber.getHa1(),subscriber.getHa1b(),subscriber.getRpid()};
		return ud.insertReturnKeys(sql, o);
	}
	
	public int del(int id){
		String sql = "delete from subscriber where id=?";
		Object[] o = new Object[]{id};
		return ud.executeNonQuery(sql, o);
	}
	
	public int up(Subscriber subscriber){
		String sql = "update subscriber set password=?,ha1=?,ha1b=? where id=?";
		Object[] o = new Object[]{subscriber.getPassword(),subscriber.getHa1(),subscriber.getHa1b(),subscriber.getId()};
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
	public List<Subscriber> paramQuery(Map<String,String> map,int page,int pageSize){
		String sql = "select * from subscriber where 1=1";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_name")!=null&&!map.get("map_name").isEmpty()){
				sql += " and username=?";
				list.add(map.get("map_name"));
			}
			if(map.get("map_domain")!=null&&!map.get("map_domain").isEmpty()){
				sql += " and domain=?";
				list.add(map.get("map_domain"));
			}
		}
		sql += " limit "+page*pageSize+","+pageSize+"";
		Object[] o = list.toArray();
		return (List<Subscriber>) ud.queryList(sql,o,new BeanPropertyRowMapper(Subscriber.class));
	}
	
	/**
	 * 获取总条数
	 * @param map
	 * @return
	 */
	public int paramQueryCount(Map<String,String> map){
		String sql = "select count(1) from subscriber where 1=1";
		List<String> list = new ArrayList<String>();
		if(map != null){
			if(map.get("map_name")!=null&&!map.get("map_name").isEmpty()){
				sql += " and username=?";
				list.add(map.get("map_name"));
			}
			if(map.get("map_domain")!=null&&!map.get("map_domain").isEmpty()){
				sql += " and domain=?";
				list.add(map.get("map_domain"));
			}
		}
		Object[] o = list.toArray();
		return ud.queryInt(sql, o);
	}
	
	/**
	 * 验证用户名是否存在
	 * @param name
	 * @param id
	 * @return true=存在
	 */
	public boolean isName(String name,int id){
		String sql = "select count(1) from subscriber where username=? and id<>?";
		Object[] o = new Object[]{name,id};
		return ud.queryInt(sql, o)>0;
	}
	
	/**
	 * 验证添加操作
	 * @param subscriber
	 * @return
	 * @throws Exception
	 */
	public int addExtend(Subscriber subscriber) throws Exception{
		boolean b = this.isName(subscriber.getUserName(), 0);
		if(b){
			throw new Exception("用户名已存在！");
		}
		int t = this.add(subscriber);
		return t;
	}
	
	public Subscriber getById(int id){
		String sql = "select * from subscriber where id=?";
		Object[] o = new Object[]{id};
		return (Subscriber) ud.queryObject(sql, o, new BeanPropertyRowMapper<Subscriber>(Subscriber.class));
	}
}
