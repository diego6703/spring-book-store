package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.CategoryDto;
import p.projects.springbookstore.dto.CreateCategoryRequestDto;
import p.projects.springbookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto requestDto);
}
