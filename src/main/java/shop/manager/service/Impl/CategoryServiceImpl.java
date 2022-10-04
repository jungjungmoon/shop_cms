package shop.manager.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shop.manager.dto.CategoryDto;
import shop.manager.entity.Category;
import shop.manager.mapper.CategoryMapper;
import shop.manager.model.CategoryInput;
import shop.manager.repository.CategoryRepository;
import shop.manager.service.CategoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Sort getSortBySortValueDesc() {

        return Sort.by(Sort.Direction.DESC, "sortValue");
    }

    @Override
    public List<CategoryDto> list() {

        List<Category> categories = categoryRepository.findAll(getSortBySortValueDesc());
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
    public boolean update(CategoryInput parameter) {

        Optional<Category> optionalCategory = categoryRepository.findById(parameter.getId());
        if (optionalCategory.isPresent()) {

            Category category = optionalCategory.get();

            category.setCategoryName(parameter.getCategoryName());
            category.setUsingYn(parameter.isUsingYn());
            category.setSortValue(parameter.getSortValue());
            categoryRepository.save(category);
        }
        return true;
    }

    // 카테고리 삭제
    @Override
    public boolean delete(long id) {

        categoryRepository.deleteById(id);

        return true;
    }

    @Override
    public List<CategoryDto> frontList(CategoryDto parameter) {

        return categoryMapper.select(parameter);

    }
}
