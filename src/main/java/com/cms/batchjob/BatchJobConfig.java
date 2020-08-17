package com.cms.batchjob;
 
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
 
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {
     
    @Autowired
    private JobBuilderFactory jobBuilder;
 
    @Autowired
    private StepBuilderFactory stepBuilder;
 
    @Value("classPath:/csv/endpoint_pfile_20050523-20200809.csv")
    private Resource csvResource;
    
    @Autowired
    private DataSource dataSource;
 
    @Bean
    public Job readCSVFile() {
        return jobBuilder
                .get("readCSVFile")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }
 
    @Bean
    public Step step() {
        return stepBuilder
                .get("step")
                .<Endpoint_Reference, Endpoint_Reference>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
     
    @Bean
    public ItemProcessor<Endpoint_Reference, Endpoint_Reference> processor() {
        return new EmployeeProcessor();
    }
    
    // reading from csv file
    @Bean
    public FlatFileItemReader<Endpoint_Reference> reader() {
        FlatFileItemReader<Endpoint_Reference> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(csvResource);
        return itemReader;
    }
 
    //convert csv rows to beans
    @Bean
    public LineMapper<Endpoint_Reference> lineMapper() {
        DefaultLineMapper<Endpoint_Reference> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "npi", "endpointType", "endpointTypeDescription", "endpoint",
                "affiliation", "endpointDescription", "affiliationLegalBusinessName","useCode","useDescription",
                "otherUseDescription",
        "contentType","contentDescription","otherContentDescription","affiliationAddressLineOne",
                "affiliationAddressLineTwo","affiliationAddressCity","affiliationAddressState","affiliationAddressCountry",
        "affiliationAddressLinePostalCode"});
        lineTokenizer.setIncludedFields(new int[] { 0, 1, 2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 });
        BeanWrapperFieldSetMapper<Endpoint_Reference> fieldSetMapper = new BeanWrapperFieldSetMapper<Endpoint_Reference>();
        fieldSetMapper.setTargetType(Endpoint_Reference.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
 
    // writting into mysql database
    @Bean
    public JdbcBatchItemWriter<Endpoint_Reference> writer() {
        JdbcBatchItemWriter<Endpoint_Reference> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("INSERT INTO Endpoint_Reference (NPI, ENDPOINT_TYPE, ENDPOINT_TYPE_DESCRIPTION, ENDPOINT, AFFILIATION," +
                "ENDPOINT_DESCRIPTION,AFFILIATION_LEGAL_BUSINESS_NAME,USE_CODE,USE_DESCRIPTION,OTHER_USE_DESCRIPTION,CONTENT_TYPE," +
                "CONTENT_DESCRIPTION,OTHER_CONTENT_DESCRIPTION,AFFILIATION_ADDRESS_LINE_ONE,AFFILIATION_ADDRESS_LINE_TWO," +
                "AFFILIATION_ADDRESS_CITY,AFFILIATION_ADDRESS_STATE,AFFILIATION_ADDRESS_COUNTRY,AFFILIATION_ADDRESS_LINE_POSTAL_CODE) " +
                "VALUES (:npi,:endpointType,:endpointTypeDescription,:endpoint,:affiliation,:endpointDescription" +
                ",:affiliationLegalBusinessName,:useCode,:useDescription,:otherUseDescription,:contentType,:contentDescription" +
                ",:otherContentDescription,:affiliationAddressLineOne,:affiliationAddressLineTwo,:affiliationAddressCity" +
                ",:affiliationAddressState,:affiliationAddressCountry,:affiliationAddressLinePostalCode)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Endpoint_Reference>());
        return itemWriter;
    }
     
}
