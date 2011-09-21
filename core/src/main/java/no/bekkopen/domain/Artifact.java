package no.bekkopen.domain;

import java.security.InvalidParameterException;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.collect.Sets;

@Entity
@Table(name = "Artifact")
public class Artifact {
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "groupId")
	private String gropId;
	@Column(name = "artifactId")
	private String artifactId;
	@Column(name = "version")
	private String version;
	@Column(name = "packaging")
	private String packaging;

	static final Set<String> VALID_PACKAGINGS = Sets.newHashSet("war", "jar", "ear", "zip", "pom");

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getGropId() {
		return gropId;
	}

	public void setGropId(final String gropId) {
		this.gropId = gropId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(final String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(final String packaging) {
		if (VALID_PACKAGINGS.contains(packaging)) {
			throw new InvalidParameterException(packaging + " er ikke gyldig! Gyldige verdier er: " + VALID_PACKAGINGS);
		}
		this.packaging = packaging;
	}

}
