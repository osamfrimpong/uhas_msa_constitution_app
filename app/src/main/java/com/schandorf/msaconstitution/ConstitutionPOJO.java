package com.schandorf.msaconstitution;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ConstitutionPOJO implements Serializable{

	@SerializedName("reference")
	private String reference;

	@SerializedName("title")
	private String title;

	@SerializedName("article")
	private String article;

	@SerializedName("content")
	private String content;

	@SerializedName("id")
	private int id;

	public void setReference(String reference){
		this.reference = reference;
	}

	public String getReference(){
		return reference;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setArticle(String article){
		this.article = article;
	}

	public String getArticle(){
		return article;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ConstitutionPOJO{" +
				"reference='" + reference + '\'' +
				", title='" + title + '\'' +
				", article='" + article + '\'' +
				", content='" + content + '\'' +
				", id=" + id +
				'}';
	}
}