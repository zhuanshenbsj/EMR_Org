package com.cloud.emr.base.Domain;

/**
 * 
 * 项目名称：EMR   
 * 类名称：Patient   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-1-26 下午06:07:26   
 * 修改人：lw   
 * 修改时间：2014-1-26 下午06:07:26   
 * 修改备注： 
 * @version
 */
public class Patient implements java.io.Serializable {
	private static final long serialVersionUID = 2284954391490103232L;
	private Integer patientId;
	private String idcard;
	private String name;
	private String phone;
	private String email;
	private String deviceId;
	private String appType;
	private Integer sex;
	private String birth;

	// Constructors

	/** default constructor */
	public Patient() {
	}

	/** minimal constructor */
	public Patient(String appType) {
		this.appType = appType;
	}

	/** full constructor */
	public Patient(String idcard, String name, String phone, String email,
			String deviceId, String appType, Integer sex, String birth) {
		this.idcard = idcard;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.deviceId = deviceId;
		this.appType = appType;
		this.sex = sex;
		this.birth = birth;
	}

	// Property accessors

	public Integer getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAppType() {
		return this.appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return this.birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}
}