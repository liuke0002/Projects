package liuke.utils;

public class UserData {
	private String userName;
	private String password;
	private int iconId;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getIconId() {
		return iconId;
	}
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	/**
	 * @param userName
	 * @param password
	 * @param iconId
	 */
	public UserData(String userName, String password, int iconId) {
		super();
		this.userName = userName;
		this.password = password;
		this.iconId = iconId;
	}
	@Override
	public String toString() {
		return "UserData [userName=" + userName + ", password=" + password + ", iconId=" + iconId + "]";
	}
	
	
}
