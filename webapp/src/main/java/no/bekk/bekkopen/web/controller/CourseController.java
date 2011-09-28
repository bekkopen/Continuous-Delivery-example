package no.bekk.bekkopen.web.controller;

import no.bekk.bekkopen.dao.CourseDao;
import no.bekk.bekkopen.domain.Course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
@Controller
public class CourseController {
    private static final String SEARCH_VIEW_KEY = "redirect:search.html";
    private static final String SEARCH_MODEL_KEY = "courses";

    @Autowired
    protected CourseDao courseDao = null;

    /**
     * For every request for this controller, this will
     * create an course instance for the form.
     */
    @ModelAttribute
    public Course newRequest(@RequestParam(required = false) Long id) {
        return (id != null ? courseDao.findCourse(id) : new Course());
    }

    /**
     * <p>Course form request.</p>
     * <p/>
     * <p>Expected HTTP GET and request '/course/form'.</p>
     */
    @RequestMapping(value = "/course/form", method = RequestMethod.GET)
    public void form() {
    }

    /**
     * <p>Saves an course.</p>
     * <p/>
     * <p>Expected HTTP POST and request '/course/form'.</p>
     */
    @RequestMapping(value = "/course/form", method = RequestMethod.POST)
    public void form(Course course, Model model) {
        Course result = courseDao.save(course);

        // set id from create
        if (course.getId() == null) {
            course.setId(result.getId());
        }
        model.addAttribute("statusMessageKey", "course.form.msg.success");
    }

    /**
     * <p>Deletes an course.</p>
     * <p/>
     * <p>Expected HTTP POST and request '/course/delete'.</p>
     */
    @RequestMapping(value = "/course/delete", method = RequestMethod.POST)
    public String delete(Course course) {
        courseDao.delete(course);
        return SEARCH_VIEW_KEY;
    }

    /**
     * <p>Searches for all courses and returns them in a
     * <code>Collection</code>.</p>
     * <p/>
     * <p>Expected HTTP GET and request '/course/search'.</p>
     */
    @RequestMapping(value = "/course/search", method = RequestMethod.GET)
    public
    @ModelAttribute(SEARCH_MODEL_KEY)
    Collection<Course> search() {
        return courseDao.findCourses();
    }
}
