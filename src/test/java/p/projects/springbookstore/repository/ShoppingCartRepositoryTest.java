package p.projects.springbookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import p.projects.springbookstore.model.ShoppingCart;
import p.projects.springbookstore.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"classpath:database/delete-shopping-carts.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find shopping cart by user id when cart exists")
    void findByUserId_ExistingUserId_ReturnsShoppingCart() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        User savedUser = userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(savedUser);
        shoppingCartRepository.save(cart);

        Optional<ShoppingCart> actualCart = shoppingCartRepository.findByUserId(savedUser.getId());

        assertThat(actualCart).isPresent();
        assertThat(actualCart.get().getUser().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("Should return empty optional when shopping cart does not exist for user id")
    void findByUserId_NonExistingUserId_ReturnsEmptyOptional() {
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findByUserId(999L);

        assertThat(actualCart).isEmpty();
    }
}
