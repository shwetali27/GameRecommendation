package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RECORD_MODEL")
public class RecModel {

	@Id
	@Column(name = "CONTENT_ID")
	private String mContentID;

	@Transient
	private String mVisitorID;

	@Column(name = "CONTENT_NAME")
	private String mContentName;

	@Column(name = "CATEGORY_NAME")
	private String mCategoryName;

	@Transient
	private String mView;

	@Transient
	private String mDownload;

	@Column(name = "IMG_URL")
	private String mImgUrl;

	public String getmImgUrl() {
		return mImgUrl;
	}

	public void setmImgUrl(String pImgUrl) {
		this.mImgUrl = pImgUrl;
	}

	public String getmContentID() {
		return mContentID;
	}

	public void setmContentID(String pContentID) {
		this.mContentID = pContentID;
	}

	public String getmVisitorID() {
		return mVisitorID;
	}

	public void setmVisitorID(String pVisitorID) {
		this.mVisitorID = pVisitorID;
	}

	public String getmContentName() {
		return mContentName;
	}

	public void setmContentName(String pContentName) {
		this.mContentName = pContentName;
	}

	public String getmCategoryName() {
		return mCategoryName;
	}

	public void setmCategoryName(String pCategoryName) {
		this.mCategoryName = pCategoryName;
	}

	public String getmView() {
		return mView;
	}

	public void setmView(String pView) {
		this.mView = pView;
	}

	public String getmDownload() {
		return mDownload;
	}

	public void setmDownload(String pDownload) {
		this.mDownload = pDownload;
	}

	public RecModel() {

	}

	public RecModel(String pVisitorID, String pContentID, String pContentName, String pCategoryName, String pView,
			String pDownload,String pImgUrl) {
		this.setmContentID(pContentID);
		this.setmCategoryName(pCategoryName);
		this.setmContentName(pContentName);
		this.setmVisitorID(pVisitorID);
		this.setmView(pView);
		this.setmDownload(pDownload);
		this.setmImgUrl(pImgUrl);
	}
}
