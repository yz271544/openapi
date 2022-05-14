package com.teradata.openapi.sample.response;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "userList")
public class UserListResponse {

	@XmlAttribute
	private List<String> userIds = Arrays.asList("1", "2", "2");

	@XmlElement
	private List<String> userNames = Arrays.asList("a", "b", "c");

	public List<String> getUserIds() {
		return userIds;
	}

	public List<String> getUserNames() {
		return userNames;
	}
}
