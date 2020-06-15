package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.ProgramIncrementDTO;

public interface ProgramIncrementService {

    ProgramIncrementDTO getProgramIncrement(final String dashboardName);

}
