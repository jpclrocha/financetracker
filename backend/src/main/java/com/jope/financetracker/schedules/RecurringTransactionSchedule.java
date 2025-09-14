package com.jope.financetracker.schedules;

import com.jope.financetracker.service.RecurringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class RecurringTransactionSchedule {
    @Autowired
    private RecurringTransactionService recurringTransactionService;

    // This cron expression runs the job at 1:00 AM every day
    @Scheduled(cron = "0 0 1 * * ?")
    public void runDailyTransactionCheck() {
        recurringTransactionService.processDueTransactions();
    }
}
