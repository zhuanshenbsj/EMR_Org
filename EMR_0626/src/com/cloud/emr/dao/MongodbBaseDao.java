package com.cloud.emr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.DBCollection;

public abstract class MongodbBaseDao<T>{

	
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * spring mongodb　集成操作类　
	 */
	protected MongoTemplate mongoTemplate;

	/*
	 * 链接本地数据库并创建数据表
	 * 
	 * @author cyy
	 * 
	 * @param dbName
	 * 
	 * @param collectionName
	 */
	public void CreateCollection(String collectionName) {
		try {
			mongoTemplate.createCollection(collectionName);
			log.info("Collection created successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/*
	 * 获取数据表
	 * 
	 * @author cyy
	 * 
	 * @param dbName
	 * 
	 * @param collectionName
	 */
	
	public DBCollection GetCollection( String collectionName) {
		DBCollection coll=null;
		try {
			 coll = mongoTemplate.getCollection(collectionName);
			return coll;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return coll;
	}
	
	
	
	
	
	/**
	 * 通过条件查询实体(集合)
	 * 
	 * @param query
	 */
	public List<T>  Listfind(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}

	/**
	 * 通过一定的条件查询一个实体
	 * 
	 * @param query
	 * @return
	 */
	public T findOne(Query query) {
		return (T) mongoTemplate.findOne(query, this.getEntityClass());
	}

	/**
	 * 通过条件查询更新数据
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public void update(Query query, Update update) {
		mongoTemplate.upsert(query, update, this.getEntityClass());
	}

	/**
	 * 保存一个对象到mongodb
	 * 
	 * @param bean
	 * @return
	 */
	public T save(T bean) {
		mongoTemplate.save(bean);
		return bean;
	}

	/**
	 * 通过ID获取记录
	 * 
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return (T) mongoTemplate.findById(id, this.getEntityClass());
	}

	/**
	 * 通过ID获取记录,并且指定了集合名(表的意思)
	 * 
	 * @param id
	 * @param collectionName
	 *            集合名
	 * @return
	 */
	public T get(String id, String collectionName) {
		return (T) mongoTemplate.findById(id, this.getEntityClass(), collectionName);
	}

	/**
	 * 获取需要操作的实体类class
	 * 
	 * @return
	 */
	protected abstract Class getEntityClass();

	/**
	 * 注入mongodbTemplate
	 * 
	 * @param mongoTemplate
	 */
	protected abstract void setMongoTemplate(MongoTemplate mongoTemplate);
	
}

