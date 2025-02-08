package com.embarkx.firstjobapp.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService ;

    public JobController(JobService jobService) {

        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> findAll() {
        return new ResponseEntity<>(jobService.findAll() ,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> cretateJob(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity("job added Successfully" ,HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobId(@PathVariable Long id){
        Job job =jobService.getJobById(id);
        if(job!= null)
           return new ResponseEntity<>(job , HttpStatus.OK);
        return new ResponseEntity("Job not found",HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){
        boolean deleted= jobService.deleteJobById(id);
        if(deleted)
           return new ResponseEntity<>("Job deleted Successfully" ,HttpStatus.OK);
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob){
        boolean updated = jobService.updateJob(id ,updatedJob);
        if(updated)
            return new ResponseEntity<>("Job updated Successfully" ,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
