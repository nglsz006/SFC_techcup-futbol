package edu.dosw.project.SFC_TechUp_Futbol;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SFCTechCupFutbolApiApplicationMainTest {

    @Test
    void instancia_noEsNula() {
        SFCTechCupFutbolApiApplication app = new SFCTechCupFutbolApiApplication();
        assertNotNull(app);
    }
}
