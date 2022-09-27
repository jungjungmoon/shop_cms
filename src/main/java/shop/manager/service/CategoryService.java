package shop.manager.service;

import org.springframework.stereotype.Service;
import shop.manager.dto.CategoryDto;

import java.util.List;

@Service
public interface CategoryService {

    /**
     * 화면에 내용 가지고 오는 부분
     */
    List<CategoryDto> list();

    /**
     * 카테고리 추가
     */
    boolean add(String categoryName);

    /**
     * 카테고리 수정
     */
    boolean update(CategoryDto parameter);

    /**
     * 카테고리 삭제
     */
    boolean delete(long id);
}
