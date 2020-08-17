package com.cms.batchjob;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
 
public class EmployeeProcessor implements ItemProcessor<Endpoint_Reference, Endpoint_Reference>{
	
	  private static final Logger LOGGER =
		      LoggerFactory.getLogger(EmployeeProcessor.class);
	
    public Endpoint_Reference process(Endpoint_Reference endpoint_reference) throws Exception
    {
        LOGGER.info("Inserting Endpoint_Reference '{}'", endpoint_reference);
        return endpoint_reference;
    }
}
