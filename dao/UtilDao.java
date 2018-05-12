package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class UtilDao extends JdbcDaoSupport{
	
	/**
	 * 增,删,改
	 * @param sql
	 */
	public int executeNonQuery(String sql,Object[] o){
		int ret = -1;
		
		try{
			ret = this.getJdbcTemplate().update(sql,o);
		}
		catch(Exception e){
			System.out.print("executeNonQuery error");
		}
		return ret;
	}
	
	/**
	 * 新增并返回主键
	 * @param sql
	 * @param o
	 * @return
	 */
	public int insertReturnKeys(final String sql, final Object[] o) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0)throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				if (o != null) {
					for (int i = 0; i < o.length; i++) {
						ps.setObject(i + 1, o[i]);
					}
				}
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	/**
	 * 查
	 * @param sql
	 * @return
	 */
	public SqlRowSet executeQuery(String sql,Object[] o){
		return this.getJdbcTemplate().queryForRowSet(sql,o);
	}
	
	public List<?> queryList(String sql,Object[] o,BeanPropertyRowMapper<?> bpr){
		return this.getJdbcTemplate().query(sql,o,bpr);
	}
	
	public Map<String,Object> queryMap(String sql,Object[] o){
		return this.getJdbcTemplate().queryForMap(sql,o);
	}
	
	public int queryInt(String sql,Object[] o){
		return this.getJdbcTemplate().queryForObject(sql,Integer.class,o);
	}
	
	public String queryString(String sql,Object[] o){
		return this.getJdbcTemplate().queryForObject(sql,String.class,o);
	}
	
	public Object queryObject(String sql,Object[] o,BeanPropertyRowMapper<?> bprm) {
		try {
			return this.getJdbcTemplate().queryForObject(sql,o,bprm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<?> queryForList(String sql,Object[] o,Class<?> c){
		return this.getJdbcTemplate().queryForList(sql, c, o);
	}
}
