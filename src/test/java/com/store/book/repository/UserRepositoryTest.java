package com.store.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.book.model.Role;
import com.store.book.model.RoleName;
import com.store.book.model.User;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Знайти користувача за email разом із його ролями")
    @Sql(
            statements = {
                    "DELETE FROM shopping_carts WHERE user_id = 10",
                    "DELETE FROM users_roles WHERE user_id = 10",
                    "DELETE FROM users WHERE id = 10"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/add-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/delete-user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findByEmail_ValidEmail_ShouldReturnUserWithRoles() {
        String email = "user@example.com";

        Optional<User> actual = userRepository.findByEmail(email);

        assertThat(actual).isPresent();
        assertThat(actual.get().getEmail()).isEqualTo(email);

        Set<Role> roles = actual.get().getRoles();
        assertThat(roles).isNotEmpty();

        boolean hasUserRole = roles.stream()
                .anyMatch(role -> role.getName().equals(RoleName.ROLE_USER));
        assertThat(hasUserRole).isTrue();
    }

    @Test
    @DisplayName("Перевірити чи існує email: має повернути true")
    @Sql(
            statements = {
                    "DELETE FROM shopping_carts WHERE user_id = 10",
                    "DELETE FROM users_roles WHERE user_id = 10",
                    "DELETE FROM users WHERE id = 10"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/add-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/delete-user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void existsByEmail_ExistingEmail_ShouldReturnTrue() {
        String email = "user@example.com";

        boolean actual = userRepository.existsByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("Перевірити чи існує email: має повернути false для неіснуючого")
    void existsByEmail_NonExistingEmail_ShouldReturnFalse() {
        String email = "nonexistent@example.com";

        boolean actual = userRepository.existsByEmail(email);

        assertThat(actual).isFalse();
    }
}
