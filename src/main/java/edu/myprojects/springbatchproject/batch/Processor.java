package edu.myprojects.springbatchproject.batch;

import edu.myprojects.springbatchproject.model.Operation;
import edu.myprojects.springbatchproject.repository.OperationRepo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Processor implements ItemProcessor<Operation, Operation> {

    final
    OperationRepo repo;

    public Processor(OperationRepo repo) {
        this.repo = repo;
    }


    @Override
    public Operation process(Operation oper) throws Exception {
        Optional<Operation> operFromDB = repo.findById(oper.getId());
        if (operFromDB.isPresent()){
            //todo записывать в файл
            System.out.println("operation is present");
        }
        return oper;
    }
}
