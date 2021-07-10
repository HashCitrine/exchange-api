package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {

//    @Query("SELECT member_id, reg_date FROM member WHERE member_id = ?1 and password = ?2")
    List<Member> findByMemberIdAndPassword(String memberId, String password);

}
