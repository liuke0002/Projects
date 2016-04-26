package liuke.exam.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

	private ArrayList<String>anwsers;
	private int level;
	private int score;
	private ArrayList<String>userAnswers;
	private String title;
	private String options;
	public Question(ArrayList<String> anwsers, int level, int score,
			 String title, String options) {
		super();
		this.anwsers = anwsers;
		this.level = level;
		this.score = score;
		this.userAnswers = new ArrayList<String>();
		this.title = title;
		this.options = options;
	}
	public ArrayList<String> getAnwsers() {
		return anwsers;
	}
	public void setAnwsers(ArrayList<String> anwsers) {
		this.anwsers = anwsers;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public ArrayList<String> getUserAnswers() {
		return userAnswers;
	}
	public void setUserAnswers(ArrayList<String> userAnswers) {
		this.userAnswers = userAnswers;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public Question() {
		super();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.title+"\n"+this.options;
	}
}
