package User;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    void shouldPassWhenUserIsAdult() {
        User user = new User();
        user.setDateOfBirth(LocalDate.now().minusYears(20)); // 20 ans

        assertDoesNotThrow(() -> userService.isAdult(user));
    }

    @Test
    void shouldThrowExceptionWhenUserIsMinor() {
        User user = new User();
        user.setDateOfBirth(LocalDate.now().minusYears(16)); // 16 ans

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.isAdult(user)
        );

        assertEquals("User must be at least 18 years old.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDateOfBirthIsNull() {
        User user = new User(); // pas de date

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.isAdult(user)
        );

        assertEquals("Birthdate mandatory.", exception.getMessage());
    }
}
