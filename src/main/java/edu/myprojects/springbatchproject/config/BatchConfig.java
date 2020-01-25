package edu.myprojects.springbatchproject.config;

import edu.myprojects.springbatchproject.model.Operation;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends JobExecutionListenerSupport {

    @Value("${myproject.directories.input}")
    private String inputDir;

    @Value("${myproject.butch.size}")
    int chunkSize;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Operation> operationReader,
                   ItemProcessor<Operation, Operation> operationProcessor,
                   ItemWriter<Operation> operationWriter
    ) {

        System.out.println(inputDir);

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<Operation, Operation> chunk(chunkSize)
                .reader(operationReader)
                .processor(operationProcessor)
                .writer(operationWriter)
                .build();


        Job job = jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .listener(this)
                .start(step)
                .build();
        return job;
    }

    @Bean
    public MultiResourceItemReader<Operation> multiOperationReader(){
        MultiResourceItemReader<Operation> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(getResources());
        resourceItemReader.setDelegate(operationReader());
        return resourceItemReader;
    }

    private Resource[] getResources(){
        File dir = new File(inputDir);
        File[] files = dir.listFiles();
        Resource [] resources = new Resource[files.length];
        for (int i = 0; i < files.length; i++) {
            resources[i] = new FileSystemResource(files[i].getAbsolutePath());
        }
        return resources;
    }

    @Bean
    public FlatFileItemReader<Operation> operationReader(){
        FlatFileItemReader<Operation> flatFileItemReader = new FlatFileItemReader<>();
        //todo заменить на свойство
        flatFileItemReader.setResource(new FileSystemResource("D:\\program\\projects\\SpringBatchProject\\src\\main\\resources\\income\\source.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Operation> lineMapper() {
        DefaultLineMapper<Operation> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "value", "status");

        BeanWrapperFieldSetMapper<Operation> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Operation.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            System.out.println("BATCH JOB COMPLETED SUCCESSFULLY");
            System.exit(0);
        }
    }
    
}
