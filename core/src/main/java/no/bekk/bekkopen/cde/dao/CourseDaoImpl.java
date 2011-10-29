package no.bekk.bekkopen.cde.dao;

import no.bekk.bekkopen.cde.domain.Course;
import no.bekk.bekkopen.cde.feature.Feature;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class CourseDaoImpl implements CourseDao {
    @PersistenceContext
    private EntityManager em = null;

    /** @noinspection unchecked*/
    @SuppressWarnings("unchecked")
	@Override
    public List<Course> findCourses() {
        return em.createQuery("from Course ").getResultList();
    }

    @Override
    public Course findCourse(Long id) {
        return em.find(Course.class, id);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Course save(Course course) {
        System.out.println("Save? " + Feature.Course.Save.isEnabled());
        if (Feature.Course.Save.isEnabled()) {
            return em.merge(course);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void delete(Course course) {
        System.out.println("Delete? " + Feature.Course.Save.isEnabled());
        if (Feature.Course.Delete.isEnabled()) {
            em.remove(em.getReference(Course.class, course.getId()));
        }
    }
}
