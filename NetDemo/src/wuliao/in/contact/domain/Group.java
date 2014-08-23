package wuliao.in.contact.domain;

public class Group {
	private String groupId;
	private String groupName;
	private String modifyTime;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public Group() {
	}
	public Group(String groupName) {
		this.groupName = groupName;
	}
	
}
