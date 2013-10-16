package com.br.models.graphviz;

import java.util.ArrayList;

public class PackageModel {
	
	private String completeName = "";
	
	private String name;
	
	private ArrayList<PackageModel> packages;
	
	private PackageModel pack;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}
	
	public String getCompleteName() {
		return completeName;
	}
	
	public void setPackages(ArrayList<PackageModel> packages) {
		this.packages = packages;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<PackageModel> getPackages() {
		return packages;
	}
	
	public void setPack(PackageModel pack) {
		this.pack = pack;
	}
	
	public PackageModel getPack() {
		return pack;
	}

}
