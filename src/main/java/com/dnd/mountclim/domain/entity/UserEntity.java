package com.dnd.mountclim.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name="TB_USER")
public class UserEntity extends BaseTimeEntity{

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    private String userPw;
}
