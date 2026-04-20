import os
import re

test_dir = r'd:\\film-rental-store2\\film-rental-store\\src\\test\\java\\com\\example\\demo'
for root, dirs, files in os.walk(test_dir):
    for f in files:
        if f.endswith('ControllerTest.java'):
            path = os.path.join(root, f)
            with open(path, 'r', encoding='utf-8') as file:
                content = file.read()
            
            if 'jwtService;' in content and f != 'PaymentControllerTest.java' and f != 'AuthControllerTest.java':
                continue
                
            if 'static class TestSecurityConfig {' in content and 'jwtService;' not in content:
                mock_beans = '''static class TestSecurityConfig {
        @org.springframework.boot.test.mock.mockito.MockBean
        private com.example.demo.auth.service.JwtService jwtService;

        @org.springframework.boot.test.mock.mockito.MockBean
        private com.example.demo.auth.service.UserDetailsServiceImpl userDetailsService;'''
                
                new_content = content.replace('static class TestSecurityConfig {', mock_beans)
                
                with open(path, 'w', encoding='utf-8') as file:
                    file.write(new_content)
                print(f'Fixed MockBeans in {f}')
