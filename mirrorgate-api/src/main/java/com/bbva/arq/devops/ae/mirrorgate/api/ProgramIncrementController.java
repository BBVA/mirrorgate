package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.bbva.arq.devops.ae.mirrorgate.dto.ProgramIncrementDTO;
import com.bbva.arq.devops.ae.mirrorgate.service.ProgramIncrementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgramIncrementController {

    private final ProgramIncrementService programIncrementService;

    @Autowired
    public ProgramIncrementController(ProgramIncrementService programIncrementService){
        this.programIncrementService = programIncrementService;
    }

    @RequestMapping(value = "/dashboards/{name}/programincrement", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    public ProgramIncrementDTO getActiveUserStories(@PathVariable("name") String name) {
        return programIncrementService.getProgramIncrementFeatures(name);
    }

}
