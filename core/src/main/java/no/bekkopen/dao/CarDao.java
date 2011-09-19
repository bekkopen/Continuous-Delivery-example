package no.bekkopen.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import no.bekkopen.domain.Car;

public interface CarDao {
	public List<Car> getCars() throws DataAccessException;
	public Car getCar(Long carId) throws DataAccessException;
}
