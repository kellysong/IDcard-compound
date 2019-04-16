package com.sjl.idcard.entity;

import android.graphics.Bitmap;

/**
 * 身份证实体
 *
 * @author Kelly
 * @version 1.0.0
 * @filename IdentityCard.java
 * @time 2017/4/13 18:13
 * @copyright(C) 2017 song
 */
public class IdentityCard {
	private String name;//姓名
	private String sex;//性别
	private String nation;//民族
	private String birthday;//出生年月日
	private String address;//住址
	private String idCardNo;//身份证号
	private String police;//签发机关
	private String expiryDate;//有效日期

	private int[] headImageByte;//原始头像字节数据
	private Bitmap headImage;//原始头像
	/**
	 * 合成位图
	 */
	private Bitmap backBitmap;//反面身份证
	private Bitmap frontBitmap;//正面身份证
	private Bitmap fullBitmap;//身份证

	/**
	 * 返回给前端的数据
	 */
	private String headImageBase64;//头像图像base64
	private String frontImageBase64;//正面图像base64
	private String backImageBase64;//反面图像base64
	private String fullBitmapBase64;
	//外国特有
	private String englishName;//英文姓名
	private String areaNo;//地区代码
	private String versionNo;//版本号
	private String policeNo;//机关代码
	private String idCardFalg;//证件标志
	private String reservedItem;//预留项



	/**
	 * 中国人身份证信息构造器
	 * @param name
	 * @param sex
	 * @param nation
	 * @param birthday
	 * @param address
	 * @param idCardNo
	 * @param police
	 * @param expiryDate
	 * @param headImage
	 */
	public IdentityCard(String name, String sex, String nation, String birthday, String address, String idCardNo,
                        String police, String expiryDate,Bitmap headImage) {
		this.name = name != null ? name.trim() : "";
		this.sex = sex != null ? sex.trim() : "";
		this.nation = nation != null ? nation.trim() : "";
		this.birthday = birthday != null ? birthday.trim() : "";
		this.address = address != null ? address.trim() : "";
		this.idCardNo = idCardNo != null ? idCardNo.trim() : "";
		this.police = police != null ? police.trim() : "";
		this.expiryDate = expiryDate != null ? expiryDate.trim() : "";
		this.headImage = headImage;
	}

	/**
	 * 中国人身份证信息构造器
	 * @param name
	 * @param sex
	 * @param nation
	 * @param birthday
	 * @param address
	 * @param idCardNo
	 * @param police
	 * @param expiryDate
	 * @param originalHeadImage
	 */
	public IdentityCard(String name, String sex, String nation, String birthday, String address, String idCardNo,
						String police, String expiryDate,int[] originalHeadImage) {
		this.name = name != null ? name.trim() : "";
		this.sex = sex != null ? sex.trim() : "";
		this.nation = nation != null ? nation.trim() : "";
		this.birthday = birthday != null ? birthday.trim() : "";
		this.address = address != null ? address.trim() : "";
		this.idCardNo = idCardNo != null ? idCardNo.trim() : "";
		this.police = police != null ? police.trim() : "";
		this.expiryDate = expiryDate != null ? expiryDate.trim() : "";
		this.headImageByte = originalHeadImage;
	}


	/**
	 * 外国人永久居住证
	 * @param englishName 英文姓名
	 * @param sex 性别
	 * @param idCardNo  证号
	 * @param areaNo 地区代码
	 * @param name 中文姓名
	 * @param expiryDate 签发日期+终止日期
	 * @param birthday 出生日期
	 * @param versionNo 版本号
	 * @param policeNo 机关代码
	 * @param idCardFalg 证件标志
	 * @param reservedItem 预留项
	 */
	public IdentityCard(String englishName, String sex, String idCardNo, String areaNo, String name, String expiryDate,
                        String birthday, String versionNo, String policeNo, String idCardFalg, String reservedItem) {
		this.englishName = englishName.trim();
		this.sex = sex.trim();
		this.idCardNo = idCardNo.trim();
		this.areaNo = areaNo.trim();
		this.name = name.trim();
		this.idCardNo = idCardNo.trim();
		this.expiryDate = expiryDate.trim();
		this.birthday = birthday.trim();
		this.versionNo = versionNo.trim();
		this.policeNo = policeNo.trim();
		this.idCardFalg = idCardFalg.trim();
		this.reservedItem = reservedItem.trim();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getPolice() {
		return police;
	}

	public void setPolice(String police) {
		this.police = police;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int[] getHeadImageByte() {
		return headImageByte;
	}

	public void setHeadImageByte(int[] headImageByte) {
		this.headImageByte = headImageByte;
	}

	public Bitmap getHeadImage() {
		return headImage;
	}

	public void setHeadImage(Bitmap headImage) {
		this.headImage = headImage;
	}

	public Bitmap getBackBitmap() {
		return backBitmap;
	}

	public void setBackBitmap(Bitmap backBitmap) {
		this.backBitmap = backBitmap;
	}

	public Bitmap getFrontBitmap() {
		return frontBitmap;
	}

	public void setFrontBitmap(Bitmap frontBitmap) {
		this.frontBitmap = frontBitmap;
	}

	public String getHeadImageBase64() {
		return headImageBase64;
	}

	public void setHeadImageBase64(String headImageBase64) {
		this.headImageBase64 = headImageBase64;
	}

	public String getFrontImageBase64() {
		return frontImageBase64;
	}

	public void setFrontImageBase64(String frontImageBase64) {
		this.frontImageBase64 = frontImageBase64;
	}

	public String getBackImageBase64() {
		return backImageBase64;
	}

	public void setBackImageBase64(String backImageBase64) {
		this.backImageBase64 = backImageBase64;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getPoliceNo() {
		return policeNo;
	}

	public void setPoliceNo(String policeNo) {
		this.policeNo = policeNo;
	}

	public String getIdCardFalg() {
		return idCardFalg;
	}

	public void setIdCardFalg(String idCardFalg) {
		this.idCardFalg = idCardFalg;
	}

	public String getReservedItem() {
		return reservedItem;
	}

	public void setReservedItem(String reservedItem) {
		this.reservedItem = reservedItem;
	}

	public Bitmap getFullBitmap() {
		return fullBitmap;
	}

	public void setFullBitmap(Bitmap fullBitmap) {
		this.fullBitmap = fullBitmap;
	}

	public String getFullBitmapBase64() {
		return fullBitmapBase64;
	}

	public void setFullBitmapBase64(String fullBitmapBase64) {
		this.fullBitmapBase64 = fullBitmapBase64;
	}

	@Override
	public String toString() {
		return "IdentityCard{" +
				"name='" + name + '\'' +
				", sex='" + sex + '\'' +
				", nation='" + nation + '\'' +
				", birthday='" + birthday + '\'' +
				", address='" + address + '\'' +
				", idCardNo='" + idCardNo + '\'' +
				", police='" + police + '\'' +
				", expiryDate='" + expiryDate + '\'' +
				'}';
	}

}
