package p.projects.springbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import p.projects.springbookstore.dto.CategoryDto;
import p.projects.springbookstore.dto.CreateCategoryRequestDto;
import p.projects.springbookstore.dto.UpdateCategoryRequestDto;

public interface CategoryService {

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto update(Long id, UpdateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
