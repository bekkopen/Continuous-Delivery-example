package no.bekk.bekkopen.dao;

import java.util.List;

import no.bekk.bekkopen.domain.Artifact;

public interface ArtifactDao {
	public List<Artifact> findArtifacts();
	public Artifact findArtifact(Long id);
	public Artifact save(Artifact artifact);
	void delete(Artifact artifact);
}
