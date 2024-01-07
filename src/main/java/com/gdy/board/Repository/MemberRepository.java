package com.gdy.board.Repository;

import com.gdy.board.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    //<memberEntity,Long(pk값)>
    //이메일로 회원 정보 조회
    //select * from member_table where member_email=?
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
