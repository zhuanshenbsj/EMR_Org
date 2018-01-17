package com.cloud.emr.base.Domain;

/**
 * 
 * 项目名称：EMR   
 * 类名称：Concept   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-1-26 下午06:07:43   
 * 修改人：lw   
 * 修改时间：2014-1-26 下午06:07:43   
 * 修改备注： 
 * @version
 */
public class Concept implements java.io.Serializable {
	private static final long serialVersionUID = 7437161981255470601L;

	private Integer conceptId;
	private String conceptDescribe;
	private String conceptName;
	private String chineseName;
	private String appType;
	private String appName;

	/** default constructor */
	public Concept() {
	}

	/** full constructor */
	public Concept(String conceptDescribe, String conceptName,
			String chineseName, String appType, String appName) {
		this.conceptDescribe = conceptDescribe;
		this.conceptName = conceptName;
		this.chineseName = chineseName;
		this.appType = appType;
		this.appName = appName;
	}

	// Property accessors

	public Integer getConceptId() {
		return this.conceptId;
	}

	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}

	public String getConceptDescribe() {
		return this.conceptDescribe;
	}

	public void setConceptDescribe(String conceptDescribe) {
		this.conceptDescribe = conceptDescribe;
	}

	public String getConceptName() {
		return this.conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getChineseName() {
		return this.chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getAppType() {
		return this.appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}