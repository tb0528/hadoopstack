package com.xiaoxiaomo.storm.core.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.xiaoxiaomo.storm.core.entity.Idable;
import com.xiaoxiaomo.storm.core.utils.ReflectionUtils;

@SuppressWarnings("all")
public abstract class BaseDao<T extends Idable>{
	protected Class clazz;
	@Resource
	protected HibernateTemplate hibernateTemplate;
	@Resource
	public JdbcTemplate jdbcTemplate;
	
	public BaseDao(){
		clazz = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	@SuppressWarnings("unchecked")
	public T get(Serializable id){
		return (T) hibernateTemplate.get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(){
		return hibernateTemplate.find("from "+clazz.getSimpleName()+" order by id asc");
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByHql(String queryString) throws DataAccessException {
		return hibernateTemplate.find(queryString, (Object[]) null);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByHql(String queryString, Object value) throws DataAccessException {
		return hibernateTemplate.find(queryString, new Object[] {value});
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByHql(String queryString, Object... value) throws DataAccessException {
		return hibernateTemplate.find(queryString, value);
	}
	
	public Long countWhere(String queryString){
		String hql = "select count(1) from "+clazz.getSimpleName();
		if(StringUtils.isNotBlank(queryString)){
			hql += " where "+queryString;
		}
		final List result = hibernateTemplate.find(hql);
		return (Long) (result!=null?result.get(0):0);
	}
	
	public long findTotalCount(DetachedCriteria detachedCriteria) {
		// select count(*) from 表名;
		// 使用 QBC 投影查询
		detachedCriteria.setProjection(Projections.rowCount());
		final List findByCriteria = this.hibernateTemplate.findByCriteria(detachedCriteria);
		return (Long) (findByCriteria.isEmpty()?0L:findByCriteria.get(0));
	}
	
	public List<T> findByCriteria(DetachedCriteria detachedCriteria,
								  int firstResult, int maxResults) {
		return this.hibernateTemplate.findByCriteria(detachedCriteria, firstResult, maxResults);
	}

	public void save(Idable t){
		hibernateTemplate.save(t);
	}

	public void update(Idable t){
		hibernateTemplate.update(t);
	}

	public void saveOrUpdate(Idable t){
		hibernateTemplate.saveOrUpdate(t);
	}

	public void delete(Serializable id){
		hibernateTemplate.delete(this.get(id));
	}
	
	public void delete(T t){
		hibernateTemplate.delete(t);
	}
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return this.jdbcTemplate.query(sql, new RowMapperResultSetExtractor<T>(rowMapper));
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
}
