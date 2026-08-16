package p.projects.springbookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import p.projects.springbookstore.dto.CategoryDto;
import p.projects.springbookstore.dto.CreateCategoryRequestDto;
import p.projects.springbookstore.dto.UpdateCategoryRequestDto;
import p.projects.springbookstore.exception.EntityNotFoundException;
import p.projects.springbookstore.mapper.CategoryMapper;
import p.projects.springbookstore.model.Category;
import p.projects.springbookstore.repository.CategoryRepository;
import p.projects.springbookstore.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Should return page of categories when findAll is called")
    void findAll_ShouldReturnPageOfCategories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category();
        category.setId(1L);
        category.setName("Programming");

        CategoryDto categoryDto = new CategoryDto(1L, "Programming", null);
        Page<Category> categoryPage = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Programming");

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should return category dto when category exists for given id")
    void getById_WithExistingId_ShouldReturnCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");

        CategoryDto categoryDto = new CategoryDto(categoryId, "Fiction", null);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(categoryId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(categoryId);
        assertThat(result.name()).isEqualTo("Fiction");

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when category does not exist for given id")
    void getById_WithNonExistingId_ShouldThrowException() {
        Long categoryId = 99L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(categoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find category by id: " + categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Should save and return category dto when valid request is provided")
    void save_WithValidRequest_ShouldReturnSavedCategoryDto() {

        Category category = new Category();
        category.setName("Science");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Science");

        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Science", "Science books");

        CategoryDto categoryDto = new CategoryDto(1L, "Science", "Science books");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Science");

        verify(categoryMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(savedCategory);
    }

    @Test
    @DisplayName("Should update and return category dto when category exists")
    void update_WithExistingId_ShouldReturnUpdatedCategoryDto() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Old Name");

        CategoryDto categoryDto = new CategoryDto(categoryId,
                "Updated Name", "Updated description");

        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                "Updated Name", "Updated description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(categoryId, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Updated Name");

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).updateCategoryFromDto(requestDto, category);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existing category")
    void update_WithNonExistingId_ShouldThrowException() {
        Long categoryId = 99L;
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto("Name", "Desc");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(categoryId, requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find category with id: " + categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Should delete category when it exists")
    void deleteById_WithExistingId_ShouldDeleteCategory() {
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        categoryService.deleteById(categoryId);

        verify(categoryRepository, times(1)).existsById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existing category")
    void deleteById_WithNonExistingId_ShouldThrowException() {
        Long categoryId = 99L;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.deleteById(categoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find category with id: " + categoryId);

        verify(categoryRepository, times(1)).existsById(categoryId);
    }
}
