package no.mesan.android.scala.demo.model.dto;

import java.io.Serializable;
import java.util.Date;

import android.graphics.drawable.Drawable;

public class TweetDTO implements Serializable {
	private static final long serialVersionUID = 2769808976839209522L;
	private String content, profileName;
	private String profileUrl;
	private Drawable imgProfile;

	private Date date;

	public TweetDTO() {
		
	}

	public TweetDTO(String content, String profileName, String profileUrl,
			Date date) {
		setContent(content);
		setProfileName(profileName);
		setProfileUrl(profileUrl);
		setDate(date);
	}

	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Drawable getImgProfile() {
		return imgProfile;
	}

	public void setImgProfile(Drawable imgProfile) {
		this.imgProfile = imgProfile;
	}

	public boolean hasImage(){
		return this.imgProfile != null;
	}
}
