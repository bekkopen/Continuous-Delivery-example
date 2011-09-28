package no.bekk.bekkopen.cde.dao;

import java.util.List;

import no.bekk.bekkopen.cde.domain.Artifact;

public interface ArtifactDao {
	public List<Artifact> findArtifacts();
	public Artifact findArtifact(Long id);
	public Artifact save(Artifact artifact);
	void delete(Artifact artifact);
}
