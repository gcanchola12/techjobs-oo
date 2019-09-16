package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.data.JobData;
import org.launchcode.models.forms.JobForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        Job jobDetails = jobData.findById(id);
        model.addAttribute("job", jobDetails);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors, RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            return "new-job";
        }

        String newName = jobForm.getName();
        Employer newJobEmployer = jobData.getEmployers().findById(jobForm.getEmployerId());
        Location newJobLocation = jobData.getLocations().findById(jobForm.getLocationId());
        CoreCompetency newJobComp = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());
        PositionType newJobType = jobData.getPositionTypes().findById(jobForm.getPositionId());

        Job newJob = new Job(newName, newJobEmployer, newJobLocation, newJobType, newJobComp);
        jobData.add(newJob);

        attributes.addAttribute("id", newJob.getId());

        return "redirect:/job";

    }
}
