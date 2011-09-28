package no.bekk.bekkopen.cde.web.controller;

import java.util.Collection;

import no.bekk.bekkopen.cde.dao.ArtifactDao;
import no.bekk.bekkopen.cde.domain.Artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ArtifactController {

    private static final String SEARCH_VIEW_KEY = "redirect:search.html";
    private static final String SEARCH_MODEL_KEY = "artifacts";

    @Autowired
    protected ArtifactDao artifactDao = null;

    /**
     * For every request for this controller, this will 
     * create an artifact instance for the form.
     */
    @ModelAttribute
    public Artifact newRequest(@RequestParam(required=false) Long id) {
        return (id != null ? artifactDao.findArtifact(id) : new Artifact());
    }

    /**
     * <p>Artifact form request.</p>
     * 
     * <p>Expected HTTP GET and request '/artifact/form'.</p>
     */
    @RequestMapping(value="/artifact/form", method=RequestMethod.GET)
    public void form() {}
    
    /**
     * <p>Saves an artifact.</p>
     * 
     * <p>Expected HTTP POST and request '/artifact/form'.</p>
     */
    @RequestMapping(value="/artifact/form", method=RequestMethod.POST)
    public void form(Artifact artifact, Model model) {
        Artifact result = artifactDao.save(artifact);
     
        // set id from create
        if (artifact.getId() == null) {
        	artifact.setId(result.getId());
        }
        model.addAttribute("statusMessageKey", "artifact.form.msg.success");
    }

    /**
     * <p>Deletes an artifact.</p>
     * 
     * <p>Expected HTTP POST and request '/artifact/delete'.</p>
     */
    @RequestMapping(value="/artifact/delete", method=RequestMethod.POST)
    public String delete(Artifact artifact) {
        artifactDao.delete(artifact);

        return SEARCH_VIEW_KEY;
    }

    /**
     * <p>Searches for all artifacts and returns them in a 
     * <code>Collection</code>.</p>
     * 
     * <p>Expected HTTP GET and request '/artifact/search'.</p>
     */
    @RequestMapping(value="/artifact/search", method=RequestMethod.GET)
    public @ModelAttribute(SEARCH_MODEL_KEY) Collection<Artifact> search() {
        return artifactDao.findArtifacts();
    }

}
