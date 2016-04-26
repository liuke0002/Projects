package liuke.exam.entity;

import java.io.Serializable;

public class ExamInfo implements Serializable {
	
	private String  subjectTitle;
	private int limittTime;
	private int questionCount;
	private int uid;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	
	public ExamInfo(String subjectTitle, int limittTime, int questionCount,
			int uid) {
		super();
		this.subjectTitle = subjectTitle;
		this.limittTime = limittTime;
		this.questionCount = questionCount;
		this.uid = uid;
	}
	public ExamInfo() {
		super();
	}
	public String getSubjectTitle() {
		return subjectTitle;
	}
	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}
	
	public int getLimittTime() {
		return limittTime;
	}
	public void setLimittTime(int limittTime) {
		this.limittTime = limittTime;
	}
	public int getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "考试科目："+this.subjectTitle+"    考生编号："+this.uid+"\n"
				+"考题数量："+this.questionCount+"    考试时间："+this.limittTime;
	}

}
