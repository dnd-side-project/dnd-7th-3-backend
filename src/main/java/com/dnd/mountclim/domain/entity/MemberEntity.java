package com.dnd.mountclim.domain.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
public class MemberEntity {

	@Builder
	public MemberEntity(int id, String username, String password, String email, String role, String provider, String providerId,
			Timestamp loginDate, Timestamp createDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.loginDate = loginDate;
		this.createDate = createDate;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;	// ROLE_USER, ROLE_ADMIN
	private String provider;
	private String providerId;
	private Timestamp loginDate;
	@CreationTimestamp
	private Timestamp createDate;
	
	public List<String> getRoleList() {
		if(this.role.length() > 0) {
			return Arrays.asList(this.role.split(","));
		}
		return new ArrayList<>();
	}
}
