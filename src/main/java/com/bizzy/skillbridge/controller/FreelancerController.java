package com.bizzy.skillbridge.controller;

import com.bizzy.skillbridge.entity.Freelancer;
import com.bizzy.skillbridge.service.FreelancerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancers")
@CrossOrigin(origins = "http://lisungui-client.s3-website.eu-north-1.amazonaws.com") // Add your frontend domain here
public class FreelancerController {

    private final FreelancerService freelancerService;

    @Autowired
    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Freelancer createFreelancerProfile(@RequestParam String uid, @RequestBody Freelancer freelancerDTO) {
        return freelancerService.createFreelanceUser(uid, freelancerDTO);
    }

    @GetMapping("/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Freelancer getFreelancer(@PathVariable String uid) {
        return freelancerService.getFreelancer(uid);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Freelancer> getFreelancers() {
        return freelancerService.getFreelancers();
    }
}
