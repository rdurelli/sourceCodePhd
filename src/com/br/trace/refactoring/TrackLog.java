package com.br.trace.refactoring;

public class TrackLog {
	
	private Integer ID;
	
	private String project;
	
	private String change_refactoring;
	
	private String author;
	
	public TrackLog(Integer iD, String project, String change_refactoring,
			String author) {
		super();
		ID = iD;
		this.project = project;
		this.change_refactoring = change_refactoring;
		this.author = author;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getChange_refactoring() {
		return change_refactoring;
	}

	public void setChange_refactoring(String change_refactoring) {
		this.change_refactoring = change_refactoring;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	

}
