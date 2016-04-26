package liuke.exam.entity;

import java.io.Serializable;

/**
 * 存放服务端考试的URL，考试信息、考题和已注册用户文件的类
 * @author Administrator
 *
 */
public class Files implements Serializable {
	
	private String url;
	private String fnExamInfo;
	private String fnQuestion;
	private String fnUser;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFnExamInfo() {
		return fnExamInfo;
	}
	public void setFnExamInfo(String fnExamInfo) {
		this.fnExamInfo = fnExamInfo;
	}
	public String getFnQuestion() {
		return fnQuestion;
	}
	public void setFnQuestion(String fnQuestion) {
		this.fnQuestion = fnQuestion;
	}
	public String getFnUser() {
		return fnUser;
	}
	public void setFnUser(String fnUser) {
		this.fnUser = fnUser;
	}
	public Files(String url, String fnExamInfo, String fnQuestion, String fnUser) {
		super();
		this.url = url;
		this.fnExamInfo = fnExamInfo;
		this.fnQuestion = fnQuestion;
		this.fnUser = fnUser;
	}
	public Files() {
		super();
	}
	@Override
	public String toString() {
		return "Files [url=" + url + ", fnExamInfo=" + fnExamInfo
				+ ", fnQuestion=" + fnQuestion + ", fnUser=" + fnUser + "]";
	}
	

}
