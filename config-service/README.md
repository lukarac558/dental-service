# config-service

For IntelliJ IDEA to recognize extra configuration files you need to:
1. Go to File | Project Structure
1. Add the Spring facet: from the left-hand list, select Facets, click the Add icon, and select Spring.
1. In the right-hand section, select Configuration Files and click Customize Spring Boot (Customize Spring Boot) in the toolbar.
1. If you want to use a custom configuration file instead of the default one, type its name in the spring.config.name field.
1. Select all configuration files in config folder
1. Click OK and apply the changes.
1. Reload maven