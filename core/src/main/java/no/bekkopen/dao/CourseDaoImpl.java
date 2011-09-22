package no.bekkopen.dao;

import no.bekkopen.domain.Attendant;
import no.bekkopen.domain.Course;
import no.bekkopen.feature.Feature;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
@Repository
@Transactional(readOnly = true)
public class CourseDaoImpl implements CourseDao {
    @PersistenceContext
    private EntityManager em = null;

    /**
     * @noinspection unchecked
     */
    @Override
    public List<Course> findCourses() {
        return em.createQuery("from Course c left join fetch c.attendants").getResultList();
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

    @Override
    public Attendant addAttendant(Attendant attendant) {
        if (Feature.Course.Attendants.isEnabled()) {
            return em.merge(attendant);
        }
        return null;
    }

    @Override
    public Attendant findAttendant(Long id) {
        if (Feature.Course.Attendants.isEnabled()) {
            return em.find(Attendant.class, id);
        }
        return null;
    }

    @Override
    public Attendant save(Attendant attendant) {
        if (Feature.Course.Attendants.isEnabled()) {
            return em.merge(attendant);
        }
        return null;
    }

    @Override
    public void delete(Attendant attendant) {
        if (Feature.Course.Attendants.isEnabled()) {
            em.remove(attendant);
        }
    }
}
