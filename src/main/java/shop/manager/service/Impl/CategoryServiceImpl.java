package shop.manager.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.manager.dto.CategoryDto;
import shop.manager.entity.Category;
import shop.manager.repository.CategoryRepository;
import shop.manager.service.CategoryService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> list() {

        List<Category> categories = categoryRepository.findAll();

       return CategoryDto.of(categories);
    }


    // 카테고리 추가
    @Override
    public boolean add(String categoryName) {

        // 카테고리 중복인지 확인

        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .regDt(LocalDateTime.now())
                .build();
        categoryRepository.save(category);

        return true;
    }
    // 카테고리 수정
    @Override
    public boolean update(CategoryDto parameter) {

        return false;
    }
    // 카테고리 삭제
    @Override
    public boolean delete(long id) {

        return false;
    }
}
