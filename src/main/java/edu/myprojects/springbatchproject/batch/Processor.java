package edu.myprojects.springbatchproject.batch;

import au.com.bytecode.opencsv.CSVWriter;
import edu.myprojects.springbatchproject.model.Operation;
import edu.myprojects.springbatchproject.repository.OperationRepo;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

@Component
public class Processor implements ItemProcessor<Operation, Operation> {

    private static Logger logger = LoggerFactory.getLogger(Processor.class);

    @Value("${myproject.directories.duplicated}")
    String duplicatedDir;
    final OperationRepo repo;

    public Processor(OperationRepo repo) {
        this.repo = repo;
    }

    @Override
    public Operation process(Operation oper) throws Exception {
        Optional<Operation> operationFromDB = repo.findByNameAndValueAndStatus(oper.getName(), oper.getValue(), oper.getStatus());
        if (operationFromDB.isPresent()) {
            String filePath = duplicatedDir + "/duplicates.csv";
            boolean isExist = new File(filePath).exists();

            createFolderWithDuplicates();

            @Cleanup
            CSVWriter writer = getWriter(filePath, isExist);
            if (!isExist)
                addFirstLineInNewFile(writer);


            String[] line = {oper.getId() + "", oper.getName(), oper.getValue(), oper.getStatus()};
            logger.info("Duplicated operation: " + oper.toString());
            writer.writeNext(line);
            return null;
        }
        return oper;
    }

    private CSVWriter getWriter(String filePath, boolean isExist) throws IOException {
        return isExist ? new CSVWriter(new FileWriter(filePath, true)) : new CSVWriter(new FileWriter(filePath));
    }

    private void createFolderWithDuplicates() {
        File folderDuplications = new File(duplicatedDir);
        if (!folderDuplications.exists()) {
            folderDuplications.mkdir();
        }
    }

    private void addFirstLineInNewFile(CSVWriter writer) {
        String[] firstLine = "id,name,value,status".split(",");
        writer.writeNext(firstLine);
    }
}
