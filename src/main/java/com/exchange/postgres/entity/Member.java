package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Member {

    @Id
    private String memberId;

    private String password;

    private Constants.ROLE role;

    private Constants.YN useYn;

    private LocalDateTime regDate;

    private LocalDateTime uptDate;
}
