package liuke.exam.dao;

import java.util.ArrayList;

import liuke.exam.entity.ExamInfo;
import liuke.exam.entity.Question;
import liuke.exam.entity.User;

public interface IExamDao {

	ArrayList<Question>loadQuesttion();
	
	ExamInfo loadexamInfo();
	
	User findUser(int uid);
	
}
