package busim.kkilogbu.global.batch;

import busim.kkilogbu.api.ParkingAPI.service.ParkingDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {


    private final ParkingDataService parkingDataService;


    @Bean
    public Job simpleJob1(JobRepository jobRepository, Step simpleStep1) {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep1)
                .build();
    }

    @Bean
    public Step simpleStep1(JobRepository jobRepository, Tasklet fetchAndSaveTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(fetchAndSaveTasklet, platformTransactionManager).build();
    }

    @Bean
    public Tasklet fetchAndSaveTasklet() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>> 주차 데이터를 가져와서 저장하는 작업 시작");
            parkingDataService.fetchAndSaveData();
            return RepeatStatus.FINISHED;
        });
    }
}