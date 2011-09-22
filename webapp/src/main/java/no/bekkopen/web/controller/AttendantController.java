package no.bekkopen.web.controller;

import no.bekkopen.dao.CourseDao;
import no.bekkopen.domain.Attendant;

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
public class AttendantController {
    private static final String SEARCH_VIEW_KEY = "redirect:search.html";
    private static final String SEARCH_MODEL_KEY = "courses";

    @Autowired
    protected CourseDao courseDao = null;

    /**
     * For every request for this controller, this will
     * create an course instance for the form.
     */
    @ModelAttribute
    public Attendant newRequest(@RequestParam(required = false) Long courseId, @RequestParam(required = false) Long id) {
        if(courseId != null){
            return courseDao.findCourse(courseId).newAttendant();
        }
        return (id != null ? courseDao.findAttendant(id) : new Attendant());
    }

    /**
     * <p>Attendant form request.</p>
     * <p/>
     * <p>Expected HTTP GET and request '/course/form'.</p>
     */
    @RequestMapping(value = "/attendant/form", method = RequestMethod.GET)
    public void form() {
    }

    /**
     * <p>Saves an course.</p>
     * <p/>
     * <p>Expected HTTP POST and request '/course/form'.</p>
     */
    @RequestMapping(value = "/attendant/form", method = RequestMethod.POST)
    public String form(Attendant course, Model model) {
        Attendant result = courseDao.save(course);

        // set id from create
        if (course.getId() == null) {
            course.setId(result.getId());
        }
        model.addAttribute("statusMessageKey", "course.form.msg.success");
        return "redirect:/course/search.html";
    }

    /**
     * <p>Deletes an course.</p>
     * <p/>
     * <p>Expected HTTP POST and request '/course/delete'.</p>
     */
    @RequestMapping(value = "/attendant/delete", method = RequestMethod.POST)
    public String delete(Attendant course) {
        courseDao.delete(course);
        return SEARCH_VIEW_KEY;
    }

}
