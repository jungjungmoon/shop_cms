package shop.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import shop.manager.dto.MemberDto;
import shop.manager.model.MemberParam;
import java.util.List;

@Mapper
public interface MemberMapper {

    /**
     * MemberMapper.xml 함수값을 가져와서 정의
     * @param parameter
     */
    List<MemberDto> selectList(MemberParam parameter);

    // 페이징 갯수 카운트
    long selectListCount(MemberParam parameter);



}
