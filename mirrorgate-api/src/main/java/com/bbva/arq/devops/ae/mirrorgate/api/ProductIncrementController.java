package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.ProductIncrementService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductIncrementController {

    private ProductIncrementService productIncrementService;

    @Autowired
    public ProductIncrementController(ProductIncrementService productIncrementService){
        this.productIncrementService = productIncrementService;
    }

    @RequestMapping(value = "/dashboards/{name}/pi", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<Feature> getAtiveUserStories(@PathVariable("name") String name) {

        return productIncrementService.getProductIncrementFeatures(name);
    }

}
