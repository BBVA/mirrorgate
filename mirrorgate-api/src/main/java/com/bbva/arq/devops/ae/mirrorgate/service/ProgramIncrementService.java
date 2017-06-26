package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import java.util.List;


public interface ProgramIncrementService {

    List<Feature> getProgramIncrementFeatures(String dashboardName);

}
