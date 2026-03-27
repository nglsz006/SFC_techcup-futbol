package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataInitializerService {

    private final AdministradorRepository administradorRepository;

    public DataInitializerService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Transactional
    public void sembrarAdmin() {
        String emailAdmin = "admin@escuelaing.edu.co";
        System.out.println(">>> [DataInitializer] Buscando admin por email: " + emailAdmin);

        if (administradorRepository.findByEmail(emailAdmin).isEmpty()) {
            System.out.println(">>> [DataInitializer] No encontrado, creando...");

            Administrador admin = new Administrador();
            admin.setName("Administrador");
            admin.setEmail(emailAdmin);
            admin.setPassword(PasswordUtil.cifrar("admin1234"));
            admin.setUserType(Usuario.TipoUsuario.PERSONAL_ADMIN);
            admin.setActivo(true);

            Administrador guardado = administradorRepository.save(admin);
            System.out.println(">>> [DataInitializer] Save ejecutado. ID asignado: " + guardado.getId());
        } else {
            System.out.println(">>> [DataInitializer] Admin ya existe, omitiendo.");
        }
    }
}