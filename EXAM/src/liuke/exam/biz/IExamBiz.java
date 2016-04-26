package liuke.exam.biz;

import java.util.ArrayList;

import liuke.exam.entity.ExamInfo;
import liuke.exam.entity.Question;
import liuke.exam.entity.User;

public interface IExamBiz {
	
	
	
	User login(int uid,String pwd)throws IdOrPwdException;
	
	/**
	 * �����������˵�ҳ��Ϳ�ŷʽҳ�����
	 * @return
	 */
	
	User getUser();
	
	/**
	 *���ؿ��� 
	 */
	void loadQuestions();
	
	/**
	 * ��ʼ����
	 * ��һ�鿼��������ң������ؿ�����Ϣ����
	 * @return
	 */
	
	ExamInfo beginExam();
	
	/**
	 * Ϊ�����ṩһ������
	 * @param id
	 * @return
	 */
	
	Question getQuestion(int qid);
	/**
	 * ���汣�濼��ѡ��Ĵ�
	 * @param id���������
	 * @param userAnswers
	 */
	void saveUserAnswers(int qid,ArrayList<String>userAnswers);
	
	
	/**
	 * ������
	 * @return
	 */
	int over();
	
	
	
	
}
