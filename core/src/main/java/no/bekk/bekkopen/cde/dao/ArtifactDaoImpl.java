package no.bekk.bekkopen.cde.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import no.bekk.bekkopen.cde.domain.Artifact;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ArtifactDaoImpl implements ArtifactDao {

	private EntityManager em = null;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Artifact> findArtifacts() {
		Query query = em
				.createQuery("select a from Artifact a");
		@SuppressWarnings("unchecked")
		List<Artifact> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public Artifact findArtifact(Long id) {
		return em.find(Artifact.class, id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Artifact save(Artifact artifact) {
		return em.merge(artifact);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Artifact artifact) {
		em.remove(em.merge(artifact));
	}
}
