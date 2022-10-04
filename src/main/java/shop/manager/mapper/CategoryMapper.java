package shop.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import shop.manager.dto.CategoryDto;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> select(CategoryDto parameter);



}
