import os

base_dir = r'd:\film-rental-store2\film-rental-store\src\main\java\com\example\demo'
old_origin = '@CrossOrigin(origins = "http://localhost:8082")'
new_origin = '@CrossOrigin(origins = "http://10.30.74.131:8082")'

for root, dirs, files in os.walk(base_dir):
    for file in files:
        if file.endswith('Controller.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            if old_origin in content:
                content = content.replace(old_origin, new_origin)
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f'Updated {file}')
