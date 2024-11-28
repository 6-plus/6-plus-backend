package com.plus.domain.user.entity;

import com.plus.domain.common.BaseTimestamped;
import com.plus.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String email;
	private String password;
	@Column(unique = true)
	private String nickname;
	private String phoneNumber;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Builder
	public User(String email, String password, String nickname, String phoneNumber, UserRole userRole) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.userRole = userRole;
	}
}
