
#!/bin/bash

# Navigate to the Backend folder
cd Backend

# Execute 'mvn install' in the Backend folder
mvn install

# Navigate back to the previous directory
cd ..

# Navigate to the Frontend folder
cd Frontend

# Execute 'mvn install' in the Frontend folder
mvn install

# Navigate back to the previous directory
cd ..

# Execute 'mvn install' in the main project folder
mvn install

# Print success message
echo "Project folders installed successfully."
