package liuke.exam.biz;

import java.util.ArrayList;

import liuke.exam.entity.ExamInfo;
import liuke.exam.entity.Question;
import liuke.exam.entity.User;

public interface IExamBiz {
	
	
	
	User login(int uid,String pwd)throws IdOrPwdException;
	
	/**
	 * 本方法在主菜单页面和可欧式页面调用
	 * @return
	 */
	
	User getUser();
	
	/**
	 *加载考题 
	 */
	void loadQuestions();
	
	/**
	 * 开始考试
	 * 将一组考试随机打乱，并返回考试信息对象
	 * @return
	 */
	
	ExamInfo beginExam();
	
	/**
	 * 为窗口提供一道考题
	 * @param id
	 * @return
	 */
	
	Question getQuestion(int qid);
	/**
	 * 保存保存考试选择的答案
	 * @param id考题的索引
	 * @param userAnswers
	 */
	void saveUserAnswers(int qid,ArrayList<String>userAnswers);
	
	
	/**
	 * 判题打分
	 * @return
	 */
	int over();
	
	
	
	
}
