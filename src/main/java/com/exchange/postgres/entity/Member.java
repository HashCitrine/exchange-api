package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Member {

    @Id
    private String memberId;

    private String password;

    @Enumerated(EnumType.STRING)
    private Constants.ROLE role;

    @Enumerated(EnumType.STRING)
    private Constants.YN useYn;

    private LocalDateTime regDate;

    private LocalDateTime uptDate;
}
