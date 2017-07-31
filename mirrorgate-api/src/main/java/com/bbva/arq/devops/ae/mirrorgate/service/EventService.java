package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Build;

public interface EventService {

    void saveBuildEvent(Build build);

}
