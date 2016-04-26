package liuke.exam.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import liuke.exam.R;
import liuke.exam.dao.IExamDao;
import liuke.exam.dao.PullParser;
import liuke.exam.entity.ExamInfo;
import liuke.exam.entity.Question;
import liuke.exam.entity.User;
import android.content.Context;

public class ExamBiz implements IExamBiz,Serializable{


	User mUser;
	ArrayList<Question>mQuestions;
	ExamInfo mExamInfo;
	IExamDao mDao;
	public ExamBiz(Context context) {
		// TODO Auto-generated constructor stub
		String parseMode=context.getString(R.string.parse_mode);
		if(parseMode.equals("pull_xml")){
			mDao=new PullParser(context);
		}
	}
	@Override
	public User login(int uid, String pwd) throws IdOrPwdException {
		User user=mDao.findUser(uid);
		if(null==user){
			throw new IdOrPwdException("ÇëÏÈ×¢²á");
		}
		if(!user.getPassword().equals(pwd)){
			throw new IdOrPwdException("ÃÜÂë´íÎó");
		}
		mUser=user;
		return user;
	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return mUser;
	}

	@Override
	public void loadQuestions() {
		mQuestions=mDao.loadQuesttion();
	}
	

	@Override
	public ExamInfo beginExam() {
		// TODO Auto-generated method stub
		Collections.shuffle(mQuestions, new Random());
		for(int i=0;i<mQuestions.size();i++){
			Question q=mQuestions.get(i);
			String title=q.getTitle();
			title=i+1+title.substring(title.indexOf("."));
			q.setTitle(title);
		}
		mExamInfo=mDao.loadexamInfo();
		mExamInfo.setUid(mUser.getId());
		return mExamInfo;
	}

	@Override
	public Question getQuestion(int qid) {
		// TODO Auto-generated method stub
		return mQuestions.get(qid);
	}

	@Override
	public void saveUserAnswers(int qid, ArrayList<String> userAnswers) {
		// TODO Auto-generated method stub
		Question q=getQuestion(qid);
		q.setUserAnswers(userAnswers);
	}

	@Override
	public int over() {
		int total=0;
		for(Question q:mQuestions){
			if(q.getAnwsers().equals(q.getUserAnswers())){
				total+=q.getScore();
			}
		}
		return total;
	}

	

}
