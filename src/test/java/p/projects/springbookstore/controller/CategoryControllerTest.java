package p.projects.springbookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should create category when user has ADMIN role")
    void createCategory_asAdmin_returnsCreated() throws Exception {
        String jsonCategory = """
                {
                    "name": "Programming",
                    "description": "Books about programming and software development"
                }
                """;

        mockMvc.perform(post("/api/categories")
                        .content(jsonCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when USER tries to create a category")
    void createCategory_asUser_returnsForbidden() throws Exception {
        String jsonCategory = """
                {
                    "name": "Programming",
                    "description": "Books about programming and software development"
                }
                """;

        mockMvc.perform(post("/api/categories")
                        .content(jsonCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 200 when USER browses categories")
    void getAllCategories_asUser_returnsOk() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 200 when USER gets category by ID")
    void getCategoryById_asUser_returnsOk() throws Exception {
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should update category when user has ADMIN role")
    void updateCategory_asAdmin_returnsOk() throws Exception {
        String jsonCategory = """
                {
                    "name": "Updated Programming",
                    "description": "Updated description"
                }
                """;

        mockMvc.perform(put("/api/categories/1")
                        .content(jsonCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when USER tries to update a category")
    void updateCategory_asUser_returnsForbidden() throws Exception {
        String jsonCategory = """
                {
                    "name": "Updated Programming",
                    "description": "Updated description"
                }
                """;

        mockMvc.perform(put("/api/categories/1")
                        .content(jsonCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when USER tries to delete a category")
    void deleteCategory_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 200 when USER gets books by category ID")
    void getBooksByCategoryId_asUser_returnsOk() throws Exception {
        mockMvc.perform(get("/api/categories/1/books"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 401 when unauthenticated user tries to get categories")
    void getAllCategories_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());
    }
}
