package no.bekkopen.domain;

import no.bekkopen.dao.CarDao;

import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@IfProfileValue(name = "integration", value = "true")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class CarIntegrationTest {

	@Autowired
	private CarDao carDao;
	private Logger logger = Logger.getLogger("myLog");
	private Long id;

	@Before
	public void init() {
        int carNumber = carDao.getCars().size();
		id = 1L;
	}

	@Test
	public void listCarsTest() {
		List<Car> cars = carDao.getCars();
        logger.info("Cars: " + cars.size());
		Assert.assertNotNull(cars);
		Assert.assertEquals(3, cars.size());
	}

	@Test
	public void getCarTest() {
		Car car = carDao.getCar(id);
		Assert.assertEquals(id.longValue(), car.getId());
		Assert.assertEquals("Corolla Verso", car.getModel());
	}
}
