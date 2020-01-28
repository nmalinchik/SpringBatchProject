package edu.myprojects.springbatchproject.tasks;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@AllArgsConstructor
public class MoveFilesToProcessedDirectory implements Tasklet, InitializingBean {

    private String inputDirectory;
    private String processedDirectory;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        createFolderWithProcessed();
        File dir = new File(inputDirectory);
        File[] files = dir.listFiles();
        if (files == null) return RepeatStatus.FINISHED;
        for (File file : files) {
            String newFile = processedDirectory + "/" + file.getName();
            try {
                Files.move(Paths.get(file.toURI()), Paths.get(newFile));
            } catch (IOException e) {
                throw new UnexpectedJobExecutionException("Could not remove file " + file.getPath());
            }
        }
        return RepeatStatus.FINISHED;
    }

    private void createFolderWithProcessed() {
        File folderProcessed = new File(processedDirectory);
        if (!folderProcessed.exists()) {
            folderProcessed.mkdir();
        }
    }

    @Override
    public void afterPropertiesSet() {}

}
