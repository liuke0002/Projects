package liuke.exam.dao;

import java.io.Serializable;
import java.util.HashMap;

import liuke.exam.R;
import liuke.exam.entity.Files;
import liuke.exam.entity.User;
import android.content.Context;
import android.content.res.Resources;

public abstract class ExamDaoBase implements Serializable {

	HashMap<Integer,User>mUsers;
	
	public ExamDaoBase() {
		mUsers=new HashMap<Integer, User>();
	}

	/**
	 * 下载已注册用户
	 */
	protected abstract void loadUsesr();
	
	/**
	 * 获取下载的服务端地址和ExamInfo、Question和User尚未文件名
	 * @param context
	 * @return
	 */
	protected Files getFiles(Context context){
		Resources res = context.getResources();
		String rootUrl=res.getString(R.string.root_url); 
		String parseMode=res.getString(R.string.parse_mode);
		String fnExamInfo=null;
		String fnQuestion=null;
		String fnUser=null;
		if("txt".equals(parseMode)){
			fnExamInfo=res.getString(R.string.exam_info_txt);
			fnQuestion=res.getString(R.string.question_txt);
			fnUser=res.getString(R.string.user_txt);
		}else if ("json".equals(parseMode)){
			fnExamInfo=res.getString(R.string.exam_info_json);
			fnQuestion=res.getString(R.string.question_json);
			fnUser=res.getString(R.string.user_json);
			
		}else if("pull_xml".equals(parseMode)) {
			fnExamInfo=res.getString(R.string.exam_info_xml);
			fnQuestion=res.getString(R.string.question_xml);
			fnUser=res.getString(R.string.user_xml);
			
		}else if("xml_sax".equals(parseMode)){
			fnExamInfo=res.getString(R.string.exam_info_json);
			fnQuestion=res.getString(R.string.question_json);
			fnUser=res.getString(R.string.user_json);
			
		}
		Files files=new Files(rootUrl, fnExamInfo, fnQuestion, fnUser);
		return files;
	}
	
	/**
	 * 业务逻辑层创建本方法
	 * @param uid
	 * @return
	 */
	public User findUser(int uid){
		return mUsers.get(uid);
	}
}
