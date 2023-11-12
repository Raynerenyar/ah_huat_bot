package org.telegram.toto;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.support.CronExpression;
import org.telegram.toto.service.CronJobService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@SpringBootTest
class TotoApplicationTests {

    @SpyBean
    private CronJobService cronJobService;
    private static final Logger logger = LoggerFactory.getLogger(TotoApplicationTests.class);
    private static final String onTheDayRemindExpression = "0 30 18/1 ? * MON,THU";

    @Test
    void testCron() {
        cronSchedulerGenerator(onTheDayRemindExpression, 100);
    }

    public void cronSchedulerGenerator(String paramScheduler, int index) {
        CronExpression cronExp = CronExpression.parse(onTheDayRemindExpression);
        LocalDateTime date = LocalDateTime.now();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < index; i++) {
            if (date != null) {
                LocalDateTime newDate = date.plusDays((long) i);
                date = cronExp.next(newDate);
                verify(cronJobService, atLeast(50)).dailyReminder();
                verify(cronJobService, atLeast(50)).onTheDayRemind();
                logger.info(newDate.toString());
            }
        }

    }

}
