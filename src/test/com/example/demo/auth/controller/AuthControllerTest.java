import static org.mockito.Mockito.*;

import org.hibernate.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testregister() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        request.setRole("CUSTOMER");

        AuthResponse response = new AuthResponse("token", "CUSTOMER", "john.doe@example.com");

        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

}
