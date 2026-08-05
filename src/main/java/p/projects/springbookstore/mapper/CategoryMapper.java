package p.projects.springbookstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.CategoryDto;
import p.projects.springbookstore.dto.CreateCategoryRequestDto;
import p.projects.springbookstore.dto.UpdateCategoryRequestDto;
import p.projects.springbookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(UpdateCategoryRequestDto dto, @MappingTarget Category category);
}
