package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = Logger.getLogger(DataInitializer.class.getName());
    private static final String EMAIL_ADMIN = "admin@escuelaing.edu.co";

    private final AdministradorRepository administradorRepository;

    public DataInitializer(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (administradorRepository.findByEmail(EMAIL_ADMIN).isPresent()) {
            log.info("Admin por defecto ya existe, omitiendo.");
            return;
        }
        Administrador admin = new Administrador();
        admin.setName("Administrador");
        admin.setEmail(EMAIL_ADMIN);
        admin.setPassword(PasswordUtil.cifrar("admin1234"));
        admin.setUserType(Usuario.TipoUsuario.PERSONAL_ADMIN);
        admin.setActivo(true);
        administradorRepository.save(admin);
        log.info("Admin por defecto creado correctamente.");
    }
}