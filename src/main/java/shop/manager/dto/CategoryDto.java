package shop.manager.dto;

import lombok.*;
import shop.manager.entity.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    Long id;

    String categoryName;
    LocalDateTime regDt;
    int sortValue;
    boolean usingYn;

    int productCount;

    public static List<CategoryDto> of (List<Category> categories) {
        if (categories != null) {
            List<CategoryDto> categoryDtoList = new ArrayList<>();
            for (Category x : categories) {
                categoryDtoList.add(of(x));
            }
            return categoryDtoList;
        }
        return null;
    }

    public static CategoryDto of(Category category) {

        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .regDt(category.getRegDt())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }
}
