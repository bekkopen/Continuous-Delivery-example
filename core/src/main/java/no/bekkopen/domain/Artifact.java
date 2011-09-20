package no.bekkopen.domain;

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
	private long id;
	@Column(name = "groupId")
	private String gropId;
	@Column(name = "artifactId")
	private String artifactId;
	@Column(name = "version")
	private String version;
	@Column(name = "packaging")
	private String packaging;
	
	static final Set<String> VALID_PACKAGINGS = Sets.newHashSet("war", "jar", "ear", "zip", "pom");
	

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getGropId() {
		return gropId;
	}

	public void setGropId(String gropId) {
		this.gropId = gropId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

}
