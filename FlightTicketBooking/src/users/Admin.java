package users;

public class Admin {
private int AdminId;
private String AdminName;
private String AdminPassword;
public Admin(int id,String name,String password) {
	AdminId=id;
	AdminName=name;
	AdminPassword=password;
}
public int getAdminId() {
	return AdminId;
}
public void setAdminId(int adminId) {
	AdminId = adminId;
}
public String getAdminName() {
	return AdminName;
}
public void setAdminName(String adminName) {
	AdminName = adminName;
}
public String getAdminPassword() {
	return AdminPassword;
}
public void setAdminPassword(String adminPassword) {
	AdminPassword = adminPassword;
}

}
