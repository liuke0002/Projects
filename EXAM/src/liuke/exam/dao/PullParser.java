package liuke.exam.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import liuke.exam.Utils.HttpUtils;
import liuke.exam.entity.ExamInfo;
import liuke.exam.entity.Files;
import liuke.exam.entity.Question;
import liuke.exam.entity.User;
import android.content.Context;

public class PullParser extends ExamDaoBase implements IExamDao ,Serializable{

	Files mFiles;
	public PullParser(Context context) {
		mFiles=getFiles(context);
		loadUsesr();
	}

	

	@Override
	public ArrayList<Question> loadQuesttion() {
		// TODO Auto-generated method stub
		String url=mFiles.getUrl()+mFiles.getFnQuestion();
		InputStream is=null;
		ArrayList<Question>questions=new ArrayList<Question>();
		try {
			is=HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");
			for(int eventType=XmlPullParser.START_DOCUMENT;eventType!=XmlPullParser.END_DOCUMENT;eventType=parser.next()){
				if(eventType==XmlPullParser.START_TAG){
					String tagName=parser.getName();
					if(tagName.equals("question")){
						String title=parser.getAttributeValue(null, "title");
						int score=Integer.parseInt(parser.getAttributeValue(null, "score"));
						int level=Integer.parseInt(parser.getAttributeValue(null, "level"));
						ArrayList<String>answers=new ArrayList<String>();
						String s=parser.getAttributeValue(null, "answer");
						for(int i=0;i<s.length();i++){
							answers.add(s.charAt(i)+"");
						}
						StringBuilder options=new StringBuilder();
						for(int i=4;i<=7;i++){
							options.append(parser.getAttributeValue(i)+"\n");
						}
						Question question=new Question(answers, level, score, title, options.toString());
						questions.add(question);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			HttpUtils.closeClient();
		}
		return questions;
	}

	@Override
	public ExamInfo loadexamInfo() {
		String url=mFiles.getUrl()+mFiles.getFnExamInfo();
		InputStream is = null;
		ExamInfo examInfo=new ExamInfo();
		try {
			is = HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");
			for(int eventType=XmlPullParser.START_DOCUMENT;eventType!=XmlPullParser.END_DOCUMENT;eventType=parser.next()){
				if(eventType==XmlPullParser.START_TAG){
					String tagName=parser.getName();
					System.out.println(tagName);
					if(tagName.equals("exam_info")){
						String questionCount=parser.getAttributeValue(2);
						String limittTime=parser.getAttributeValue(1);
						String subjectTitle=parser.getAttributeValue(0);
						examInfo.setLimittTime(Integer.parseInt(limittTime));
						examInfo.setQuestionCount(Integer.parseInt(questionCount));
						examInfo.setSubjectTitle(subjectTitle);
						return examInfo;
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtils.closeClient();
		}
		return examInfo;
	}

	@Override
	protected void loadUsesr() {
		// TODO Auto-generated method stub
		String url=mFiles.getUrl()+mFiles.getFnUser();
		InputStream is=null;
		try {
			is=HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");
			for(int eventType=XmlPullParser.START_DOCUMENT;eventType!=XmlPullParser.END_DOCUMENT;eventType=parser.next()){
				if(eventType==XmlPullParser.START_TAG){
					String tagName=parser.getName();
					if(tagName.equals("user")){
						int id=Integer.parseInt(parser.getAttributeValue(null, "id"));
						String name=parser.getAttributeValue(null, "name");
						String password=parser.getAttributeValue(null, "password");
						String phone=parser.getAttributeValue(null, "phone");
						String email=parser.getAttributeValue(null, "email");
						User user=new User(id, name, password, phone, email);
						mUsers.put(id, user);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtils.closeClient();
		}
	}

}
