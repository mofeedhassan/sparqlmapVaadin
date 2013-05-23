package org.aksw;

import java.util.HashMap;

public class TripleMap 
{
	private String subject;
	private String tablename;
	private String tmplat;
	private String clas;
	private String column;
	private String predicate;
	public HashMap<String, String> predicatesColumns;//= new HashMap<String, String>();
	public TripleMap(){}
	public TripleMap (String subject,String tblname, String tmpl, String cls)
	{ 
		this.setSubject(subject);
		this.setTblname(tblname);
		this.setTemplate(tmpl);
		this.setClass(cls);
	}
	public String getTblname() {
		return tablename;
	}
	public void setTblname(String tblname) {
		this.tablename = tblname;
	}
	public String getTmpl() {
		return tmplat;
	}
	public void setTemplate(String tmpl) {
		this.tmplat = tmpl;
	}
	public String getCls() {
		return clas;
	}
	public void setClass(String cls) {
		this.clas = cls;
	}
	public void Processing()
	{
		subject=this.getLastToken(subject,'#'); 
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	private String getLastToken(String s,char c)
	{
		int i = s.lastIndexOf(c); 
		String buffer = s.substring(i+1); 
		return buffer;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
}
