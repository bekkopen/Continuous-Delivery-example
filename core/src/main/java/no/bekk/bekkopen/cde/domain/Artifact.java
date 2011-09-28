package no.bekk.bekkopen.cde.domain;

import java.security.InvalidParameterException;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.collect.Sets;

@Entity
@Table(name = "Artifact")
public class Artifact {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -8712872385957386182L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@Column(name = "groupId")
	private String groupId;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
		for (String validPackaging : VALID_PACKAGINGS) {
			if (validPackaging.equals(packaging)) {
				this.packaging = packaging;
				return;
			}
		}
		throw new InvalidParameterException(packaging + " er ikke gyldig! Gyldige verdier er: " + VALID_PACKAGINGS);
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Artifact other = (Artifact) obj;

        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }

        return true;
    }

}
