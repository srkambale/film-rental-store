import os
import re

test_dir = r'd:\\film-rental-store2\\film-rental-store\\src\\test\\java\\com\\example\\demo'
for root, dirs, files in os.walk(test_dir):
    for f in files:
        if f.endswith('ControllerTest.java') and f != 'PaymentControllerTest.java':
            path = os.path.join(root, f)
            with open(path, 'r', encoding='utf-8') as file:
                content = file.read()
            
            # Find the ApiTests block and remove the bogus constructor instantiation
            pattern = re.compile(r'(class ApiTests\s*\{.*?@BeforeEach\s*void setUp\(\)\s*\{\s*)unit\w+Controller = new \w+Controller\(unit\w+Service\);\s*', re.DOTALL)
            new_content = pattern.sub(r'\1', content)
            
            if new_content != content:
                with open(path, 'w', encoding='utf-8') as file:
                    file.write(new_content)
                print(f'Fixed {f}')
