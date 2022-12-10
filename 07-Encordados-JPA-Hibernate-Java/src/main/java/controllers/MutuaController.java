package controllers;

import models.Raqueta;
import models.Tenista;
import repositories.raquetas.RaquetasRepository;
import repositories.tenistas.TenistasRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class MutuaController {
    private final Logger logger = Logger.getLogger(MutuaController.class.getName());

    // Mis dependencias
    private final RaquetasRepository raquetasRepository;
    private final TenistasRepository tenistasRepository;

    public MutuaController(RaquetasRepository raquetasRepository, TenistasRepository tenistasRepository) {
        this.raquetasRepository = raquetasRepository;
        this.tenistasRepository = tenistasRepository;
    }

    // Tenistas
    public List<Tenista> getTenistas() {
        logger.info("Obteniendo Tenistas");
        return tenistasRepository.findAll();
    }

    public Tenista createTenista(Tenista tenista) {
        logger.info("Creando Tenista");
        return tenistasRepository.save(tenista);
    }

    public Optional<Tenista> getTenistaById(UUID uuid) {
        logger.info("Obteniendo Tenista con uuid: " + uuid);
        return tenistasRepository.findById(uuid);
    }

    public Tenista updateTenista(Tenista tenista) {
        logger.info("Actualizando Tenista con uuid: " + tenista.getUuid());
        return tenistasRepository.save(tenista);
    }

    public Boolean deleteTenista(Tenista tenista) {
        logger.info("Eliminando Tenista con uuid: " + tenista.getUuid());
        return tenistasRepository.delete(tenista);
    }

    // Raquetas
    public List<Raqueta> getRaquetas() {
        logger.info("Obteniendo Raquetas");
        return raquetasRepository.findAll();
    }

    public Raqueta createRaqueta(Raqueta raqueta) {
        logger.info("Creando Raqueta");
        return raquetasRepository.save(raqueta);
    }

    public Optional<Raqueta> getRaquetaById(UUID uuid) {
        logger.info("Obteniendo Raqueta con uuid: " + uuid);
        return raquetasRepository.findById(uuid);
    }

    public Raqueta updateRaqueta(Raqueta raqueta) {
        logger.info("Actualizando Raqueta con uuid: " + raqueta.getUuid());
        return raquetasRepository.save(raqueta);
    }

    public Boolean deleteRaqueta(Raqueta raqueta) {
        logger.info("Eliminando Raqueta con uuid: " + raqueta.getUuid());
        return raquetasRepository.delete(raqueta);
    }
}
