package com.gudlad.rest.webservices.restfulwebservices.rest_api_filtering;


import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.gudlad.rest.webservices.restfulwebservices.rest_api_filtering.FilterLogic.applyDynamicFilter;

// dynamic filtering = customize filtering for a bean for specific REST API
@RestController
public class DynamicFilteringController {
    @GetMapping("/dynamic-filtering")     // RestAPi 1
    public MappingJacksonValue filtering() {
        SomeBean2 someBean = new SomeBean2("value1", "value2", "value3");
        return applyDynamicFilter(someBean, "field2", "field3");
    }

    @GetMapping("/dynamic-filtering-list")  // RestApi 2
    public MappingJacksonValue filteringList() {
        List<SomeBean2> list = Arrays.asList(new SomeBean2("value1", "value2", "value3"),
                new SomeBean2("value11", "value22", "value33"),
                new SomeBean2("value111", "value222", "value333"));
        return applyDynamicFilter(list, "field1", "field3");
    }

}