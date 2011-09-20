package no.bekkopen.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import no.bekkopen.domain.Artifact;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ArtifactDaoImpl implements ArtifactDao {

	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	public List<Artifact> getArtifacts() throws DataAccessException {
		Query query = getEntityManager().createQuery("select a from Artifact a");
		@SuppressWarnings("unchecked")
		List<Artifact> resultList = query.getResultList();
		return resultList;
	}

	@Transactional
	public Artifact getArtifact(Long id) throws DataAccessException {
		return getEntityManager().find(Artifact.class, id);
	}
}
