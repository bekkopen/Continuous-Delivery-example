package no.bekkopen.dao;

import no.bekkopen.domain.Attendant;
import no.bekkopen.domain.Course;

import java.util.List;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
public interface CourseDao {
    List<Course> findCourses();

    Course findCourse(Long id);

    Course save(Course course);

    void delete(Course course);

    Attendant findAttendant(Long id);

    Attendant save(Attendant attendant);

    void delete(Attendant attendant);

    Attendant addAttendant(Attendant attendant);
}
