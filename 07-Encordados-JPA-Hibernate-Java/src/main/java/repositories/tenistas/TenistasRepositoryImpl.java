package repositories.tenistas;

import db.HibernateManager;
import exceptions.RaquetaException;
import exceptions.TenistaException;
import jakarta.persistence.TypedQuery;
import models.Tenista;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class TenistasRepositoryImpl implements TenistasRepository {
    private final Logger logger = Logger.getLogger(TenistasRepositoryImpl.class.getName());

    @Override
    public List<Tenista> findAll() {
        logger.info("findAll()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        TypedQuery<Tenista> query = hb.getManager().createNamedQuery("Tenista.findAll", Tenista.class);
        List<Tenista> list = query.getResultList();
        hb.close();
        return list;
    }

    @Override
    public Optional<Tenista> findById(UUID uuid) {
        logger.info("findById()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        Optional<Tenista> tenista = Optional.ofNullable(hb.getManager().find(Tenista.class, uuid));
        hb.close();
        return tenista;
    }

    @Override
    public Tenista save(Tenista entity) throws RaquetaException {
        logger.info("save()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        hb.getTransaction().begin();

        // Por otro lado y si la raqueta no existe? Podemos controlar que exista la raqueta
        // antes de guardar el tenista o que la inserte con el tenista. Vamos a ser restrictivos
        var existeRaqueta = hb.getManager().find(entity.getRaqueta().getClass(), entity.getRaqueta().getUuid());
        if (existeRaqueta == null) {
            throw new RaquetaException("La raqueta con uuid " + entity.getRaqueta().getUuid() + " no existe" );
        }
        try {
            hb.getManager().merge(entity);
            hb.getTransaction().commit();
            hb.close();
            return entity;
        } catch (Exception e) {
            throw new RaquetaException("Error al salvar tenista con uuid: " + entity.getUuid() + "\n" + e.getMessage());
        } finally {
            if (hb.getTransaction().isActive()) {
                hb.getTransaction().rollback();
            }
        }
    }

    @Override
    public Boolean delete(Tenista entity) {
        logger.info("delete()");
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
       try {
            hb.getTransaction().begin();
            // Ojo que borrar implica que estemos en la misma sesión y nos puede dar problemas, por eso lo recuperamos otra vez
            entity = hb.getManager().find(Tenista.class, entity.getUuid());
            hb.getManager().remove(entity);
            hb.getTransaction().commit();
            hb.close();
            return true;
        } catch (Exception e) {
            throw new TenistaException("Error al eliminar tenista con uuid: " + entity.getUuid() + "\n" + e.getMessage());
        } finally {
            if (hb.getTransaction().isActive()) {
                hb.getTransaction().rollback();
            }
        }
    }
}
