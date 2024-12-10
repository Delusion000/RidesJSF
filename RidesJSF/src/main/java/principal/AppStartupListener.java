package principal;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @Inject
    private HibernateDataAccess dataAccess;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("AppStartupListener: Inicializando base de datos...");
        dataAccess.initializeDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Código opcional para liberar recursos al apagar la aplicación
    }
}
