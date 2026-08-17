package p.projects.springbookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import p.projects.springbookstore.dto.BookDtoWithoutCategoryIds;
import p.projects.springbookstore.dto.CategoryDto;
import p.projects.springbookstore.dto.CreateCategoryRequestDto;
import p.projects.springbookstore.dto.UpdateCategoryRequestDto;
import p.projects.springbookstore.service.BookService;
import p.projects.springbookstore.service.CategoryService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should create category when user is ADMIN")
    void createCategory_AsAdmin_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Programming", "Books about programming");
        CategoryDto responseDto = new CategoryDto(1L, "Programming", "Books about programming");

        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Programming"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should forbid category creation when user is regular USER")
    void createCategory_AsUser_ReturnsForbidden() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Programming", "Description");

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return paginated list of categories for USER")
    void getAllCategories_AsUser_ReturnsPage() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1L, "Programming", "Description");
        PageImpl<CategoryDto> page = new PageImpl<>(List.of(categoryDto),
                PageRequest.of(0, 20), 1);

        when(categoryService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Programming"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return category by ID for USER")
    void getCategoryById_AsUser_ReturnsCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto(categoryId, "Programming", "Description");

        when(categoryService.getById(categoryId)).thenReturn(categoryDto);

        mockMvc.perform(get("/api/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Programming"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should update category when user is ADMIN")
    void updateCategory_AsAdmin_ReturnsUpdatedCategory() throws Exception {
        Long categoryId = 1L;
        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("Programming Updated", "New Desc");
        CategoryDto responseDto = new CategoryDto(categoryId, "Programming Updated", "New Desc");

        when(categoryService.update(eq(categoryId),
                any(UpdateCategoryRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Programming Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should delete category and return NO_CONTENT when user is ADMIN")
    void deleteCategory_AsAdmin_ReturnsNoContent() throws Exception {
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/api/categories/{id}", categoryId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return books by category ID for USER")
    void getBooksByCategoryId_AsUser_ReturnsPage() throws Exception {
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        PageImpl<BookDtoWithoutCategoryIds> page = new PageImpl<>(List.of(bookDto),
                PageRequest.of(0, 20), 1);

        when(bookService.getBooksByCategoryId(eq(categoryId), any())).thenReturn(page);

        mockMvc.perform(get("/api/categories/{id}/books", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 400 Bad Request when creating a category with invalid request body")
    void createCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateCategoryRequestDto invalidRequestDto = new CreateCategoryRequestDto(null,null);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
