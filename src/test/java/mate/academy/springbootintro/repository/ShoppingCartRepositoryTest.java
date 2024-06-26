package mate.academy.springbootintro.repository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import mate.academy.springbootintro.model.ShoppingCart;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    private static final Long USER_ID = 3L;
    private static final Long INVALID_USER_ID = 999L;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("""
            Verify findShoppingCartByUserId() method works
            """)
    @Sql(scripts = {
            "classpath:database/books/delete-books-from-books-table.sql",
            "classpath:database/users/add-1-user-to-users-table.sql",
            "classpath:database/shopping_carts/add-user-1-shopping-cart.sql",
            "classpath:database/books/add-3-books-to-books-table.sql",
            "classpath:database/cart_items/add_items_to_cart_1.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/cart_items/delete-cart-items-from-cart_items-table.sql",
            "classpath:database/shopping_carts/delete-shopping-cart-from-shopping_carts-table.sql",
            "classpath:database/books/delete-books-from-books-table.sql",
            "classpath:database/users/delete-user-from-users-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUserId_ValidUserId_ReturnsShoppingCart() {
        // Given
        User user = new User();
        user.setId(USER_ID);

        ShoppingCart expectedShoppingCart = new ShoppingCart();
        expectedShoppingCart.setUser(user);

        // When
        Optional<ShoppingCart> shoppingCartByUserId =
                shoppingCartRepository.findShoppingCartByUserId(USER_ID);

        // Then
        assertTrue(shoppingCartByUserId.isPresent(),
                "Shopping cart should be present for given user ID");

        ShoppingCart retrievedShoppingCart = shoppingCartByUserId.get();
        assertNotNull(retrievedShoppingCart);
        assertEquals(expectedShoppingCart
                .getUser().getId(), retrievedShoppingCart.getUser().getId());

        assertFalse(retrievedShoppingCart.isDeleted());
        assertNotNull(retrievedShoppingCart.getCartItems());
    }

    @Test
    @DisplayName("""
             Verify findShoppingCartByUserId() method returns empty for invalid user ID
            """)
    @Sql(scripts = {
            "classpath:database/books/delete-books-from-books-table.sql",
            "classpath:database/users/add-1-user-to-users-table.sql",
            "classpath:database/shopping_carts/add-user-1-shopping-cart.sql",
            "classpath:database/books/delete-books-from-books-table.sql",
            "classpath:database/books/add-3-books-to-books-table.sql",
            "classpath:database/cart_items/add_items_to_cart_1.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/cart_items/delete-cart-items-from-cart_items-table.sql",
            "classpath:database/shopping_carts/delete-shopping-cart-from-shopping_carts-table.sql",
            "classpath:database/books/delete-books-from-books-table.sql",
            "classpath:database/users/delete-user-from-users-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUserId_InvalidUserId_ReturnsEmpty() {
        // Given
        User user = new User();
        user.setId(INVALID_USER_ID);

        // When
        Optional<ShoppingCart> shoppingCartByUserId
                = shoppingCartRepository.findShoppingCartByUserId(INVALID_USER_ID);

        // Then
        assertFalse(shoppingCartByUserId.isPresent());
    }
}
