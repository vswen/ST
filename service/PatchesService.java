package service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import dao.UtilDao;
import entity.Patches;

@Service
public class PatchesService {
	
	@Autowired
	private UtilDao ud;
	
	/**
	 * 获取补丁包表数据
	 * @param queryType=0 获取全部, =1获取未删除, =2获取未删除的最后一条, =其他获取最后五条
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Patches> showAll(int queryType){
		String sql = "select * from t_patches where 1=1";
		switch (queryType) {
			case 0:
				sql += " order by id desc";
				break;
			case 1:
				sql += " and deleted=0";
				break;
			case 2:
				sql += " and id=(select max(id) from t_patches where deleted=0)";
				break;
			default:
				sql += " order by id desc limit 5";
				break;
		}
		return (List<Patches>) ud.queryList(sql,null,new BeanPropertyRowMapper(Patches.class));
	}
	
	public Patches getById(int id){
		String sql = "select * from t_patches where id=?";
		Object[] o = new Object[]{id};
		return (Patches) ud.queryObject(sql, o, new BeanPropertyRowMapper<Patches>(Patches.class));
	}
	
	public int add(Patches patches){
		String sql = "insert into t_patches(version,file_name,down_count,verify_md5,create_time,deleted,details) values(?,?,0,?,now(),0,?)";
		Object[] o = new Object[]{patches.getVersion(),patches.getFileName(),patches.getVerifyMd5(),patches.getDetails()};
		return ud.insertReturnKeys(sql, o);
	}
	
	/**
	 * 验证版本号是否重复
	 * @param version true表示不重复
	 * @return
	 */
	public boolean verifyV(String version,int id){
		String sql = "select count(0) from t_patches where version=? and id<>?";
		Object[] o = new Object[]{version,id};
		return ud.queryInt(sql, o)==0;
	}
	
	/**
	 * 修改远程网关补丁包数据
	 * @param version
	 * @param downCount
	 * @param deleted
	 * @param details
	 * @param id
	 * @return
	 */
	public boolean up(String version,int downCount,int deleted,String details,int id){
		StringBuffer sb = new StringBuffer();
		if(version!=null&&!"".equals(version)){
			sb.append("version='"+version+"',");
		}
		if(downCount == 1){
			sb.append("down_count=down_count+1,");
		}
		sb.append("deleted="+deleted+",");
		if(details!=null&&!"".equals(details)){
			sb.append("details='"+details+"',");
		}
		sb.deleteCharAt(sb.length()-1);
		String sql = "update t_patches set "+sb.toString()+" where id="+id;
		return ud.executeNonQuery(sql, null)>0;
	}
}
