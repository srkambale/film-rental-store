import os
import re

base_dir = r'd:\film-rental-store2\film-rental-store\src\main\java\com\example\demo'
import_statement = 'import org.springframework.web.bind.annotation.CrossOrigin;\n'
annotation = '@CrossOrigin(origins = "http://localhost:8082")\n@RestController'

import_pattern = re.compile(r'import\s+org\.springframework\.web\.bind\.annotation\.\w+;')

for root, dirs, files in os.walk(base_dir):
    for file in files:
        if file.endswith('Controller.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            if '@RestController' in content and '@CrossOrigin' not in content:
                # Add import if missing
                if 'import org.springframework.web.bind.annotation.CrossOrigin;' not in content:
                    match = import_pattern.search(content)
                    if match:
                        content = content[:match.start()] + import_statement + content[match.start():]
                
                # Add annotation
                content = content.replace('@RestController', annotation)
                
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f'Updated {file}')
