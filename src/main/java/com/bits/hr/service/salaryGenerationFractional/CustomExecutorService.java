package com.bits.hr.service.salaryGenerationFractional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Service;

@Service
public class CustomExecutorService {

    public ExecutorService executorService = Executors.newFixedThreadPool(2);
}
