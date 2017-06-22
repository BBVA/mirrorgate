package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.ProductIncrementService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductIncrementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductIncrementController.class);

    private ProductIncrementService productIncrementService;

    @Autowired
    public ProductIncrementController(ProductIncrementService productIncrementService){
        this.productIncrementService = productIncrementService;
    }

    @RequestMapping(value = "/dashboards/{name}/productincrement", method = GET, produces = APPLICATION_JSON_VALUE)
    public int getAtiveUserStories(@PathVariable("name") String name) {

        try{
            List<Feature> features = productIncrementService.getProductIncrementFeatures(name);
            return features != null ? features.size() : 0;
        } catch(Exception e) {
            LOGGER.error("Exception while retrieveing PI features",e);
            return 0;
        }
    }

}
