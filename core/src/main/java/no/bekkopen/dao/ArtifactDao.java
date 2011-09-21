package no.bekkopen.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import no.bekkopen.domain.Artifact;

public interface ArtifactDao {
	public List<Artifact> findArtifacts() throws DataAccessException;
	public Artifact getArtifact(Long id) throws DataAccessException;
	Artifact save(Artifact artifact);
	void delete(Artifact artifact) throws DataAccessException;
}
