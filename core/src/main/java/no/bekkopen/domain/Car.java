package no.bekkopen.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Car")
public class Car {
	@Id
	@Column(name = "id")
	private long id;
	@Column(name = "company")
	private String company;
	@Column(name = "model")
	private String model;
	@Column(name = "price")
	private long price;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

}
