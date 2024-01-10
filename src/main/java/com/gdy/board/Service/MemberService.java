package com.gdy.board.Service;

import com.gdy.board.DTO.MemberDTO;
import com.gdy.board.Entity.MemberEntity;
import com.gdy.board.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        // 1. dto -> entity 변환
        // 2. repository의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity); //jpa가 제공하는 메서드
        //repository의 save 메서드 호출 (조건 : entity 객체를 넘겨줘야함)
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 한다.
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단.
         */
        //1. 회원이 입력한 이메일로 db에서 조회
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        //2. db에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단.
        if(byMemberEmail.isPresent()){ //조회 결과가 있다.
            MemberEntity memberEntity = byMemberEmail.get(); //엔티티 객체 가져옴
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){ //비밀번호 일치
                // entity ->dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }
            else{ //비밀번호 불일치
                return null;
            }
        }
        else{ //조회 결과가 없다.
            return null;
        }
    }

    public List<MemberDTO> findAll() { //회원 목록 출력을 위한 함수
        //Entity List를 DTO List로 줘야함
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        //list끼리 변환하려면 memberEntityList에서 하나하나 꺼내 DTOlist에 하나하나 담아야함
        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            //MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            //memberDTOList.add(memberDTO)
        }

        return memberDTOList;

    }

    public MemberDTO findById(Long id) {
        //옵셔널 객체를 한번 까야 entity 객체가 보임 -> dto로 변환을하고 controller로 줘야함
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
            //MemberEntity memberEntity = optionalMemberEntity.get();
            //MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            //return memberDTO;
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }
        else{
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }
        else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        //toUpdateMemberEntity를 안만들어주면 insert가 되어버림
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
