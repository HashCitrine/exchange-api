package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query(value = "select member_id, reg_date from member where member_id=?1", nativeQuery = true)
    Object findMember(String memberId);

    @Query(value = "select * from member where member_id=?1", nativeQuery = true)
    Member findByMemberId(String memberId);

    @Modifying
    @Query(value = "update member set token='' where member_id=?1", nativeQuery = true)
    void updateMemberTokenSetNull(String memberId);
}
