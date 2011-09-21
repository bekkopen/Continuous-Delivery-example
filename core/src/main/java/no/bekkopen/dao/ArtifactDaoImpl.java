package no.bekkopen.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import no.bekkopen.domain.Artifact;

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
	public List<Artifact> findArtifacts() {
		Query query = getEntityManager()
				.createQuery("select a from Artifact a");
		@SuppressWarnings("unchecked")
		List<Artifact> resultList = query.getResultList();
		return resultList;
	}

	@Transactional
	public Artifact getArtifact(Long id) {
		return getEntityManager().find(Artifact.class, id);
	}

	@Transactional
	public Artifact save(Artifact artifact) {
		return getEntityManager().merge(artifact);
	}

	@Transactional
	public void delete(Artifact artifact) {
		getEntityManager().remove(artifact);
	}
}
