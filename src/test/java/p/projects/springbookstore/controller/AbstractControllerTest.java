package p.projects.springbookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import p.projects.springbookstore.security.JwtUtil;
import p.projects.springbookstore.security.SecurityConfig;

@WebMvcTest
@Import({JwtUtil.class, SecurityConfig.class})
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected JwtUtil jwtUtil;

    @MockitoBean
    protected UserDetailsService userDetailsService;
}
