package repositories.raquetas;

import db.HibernateManager;
import exceptions.RaquetaException;
import exceptions.TenistaException;
import jakarta.persistence.TypedQuery;
import models.Raqueta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class RaquetasRepositoryImpl implements RaquetasRepository {
    private final Logger logger = Logger.getLogger(RaquetasRepositoryImpl.class.getName());

    @Override
    public List<Raqueta> findAll() {
        logger.info("findAll()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        TypedQuery<Raqueta> query = hb.getManager().createNamedQuery("Raqueta.findAll", Raqueta.class);
        List<Raqueta> list = query.getResultList();
        hb.close();
        return list;
    }

    @Override
    public Optional<Raqueta> findById(UUID uuid) {
        logger.info("findById()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        Optional<Raqueta> raqueta = Optional.ofNullable(hb.getManager().find(Raqueta.class, uuid));
        hb.close();
        return raqueta;
    }

    @Override
    public Raqueta save(Raqueta entity) {
        logger.info("save()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        hb.getTransaction().begin();

        try {
            hb.getManager().merge(entity);
            hb.getTransaction().commit();
            hb.close();
            return entity;

        } catch (Exception e) {
            throw new RaquetaException("Error al salvar raqueta con uuid: " + entity.getUuid() + "\n" + e.getMessage());
        } finally {
            if (hb.getTransaction().isActive()) {
                hb.getTransaction().rollback();
            }
        }
    }

    @Override
    public Boolean delete(Raqueta entity) {
        logger.info("delete()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        try {
            hb.getTransaction().begin();
            // Ojo que borrar implica que estemos en la misma sesión y nos puede dar problemas, por eso lo recuperamos otra vez
            entity = hb.getManager().find(Raqueta.class, entity.getUuid());
            hb.getManager().remove(entity);
            hb.getTransaction().commit();
            hb.close();
            return true;
        } catch (Exception e) {
            throw new TenistaException("Error al eliminar tenista con uuid: " + entity.getUuid() + " - " + e.getMessage());
        } finally {
            if (hb.getTransaction().isActive()) {
                hb.getTransaction().rollback();
            }
        }
    }
}
