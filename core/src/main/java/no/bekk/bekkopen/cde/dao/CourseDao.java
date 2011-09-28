package no.bekk.bekkopen.cde.dao;

import no.bekk.bekkopen.cde.domain.Course;

import java.util.List;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
public interface CourseDao {
    public List<Course> findCourses();

    public Course findCourse(Long id);

    public Course save(Course course);

    void delete(Course course);
}
