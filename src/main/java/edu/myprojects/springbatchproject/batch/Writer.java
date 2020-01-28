package edu.myprojects.springbatchproject.batch;

import edu.myprojects.springbatchproject.model.Operation;
import edu.myprojects.springbatchproject.repository.OperationRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Writer implements ItemWriter<Operation> {

    private static Logger logger = LoggerFactory.getLogger(Writer.class);

    final
    OperationRepo allRepo;

    public Writer(OperationRepo allRepo) {
        this.allRepo = allRepo;
    }

    @Override
    public void write(List<? extends Operation> operations) throws Exception {
        if (operations.size()>0) logger.info("Data Saved for operations: " + operations);
        allRepo.saveAll(operations);
    }
}
