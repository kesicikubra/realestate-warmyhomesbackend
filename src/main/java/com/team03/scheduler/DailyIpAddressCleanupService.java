package com.team03.scheduler;

import com.team03.repository.business.DailyAdvertViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyIpAddressCleanupService {

   // private final RedisTemplate<String, Object> redisTemplate;

    private final DailyAdvertViewRepository dailyAdvertViewRepository;
    @Scheduled(cron = "0 0 3 * * *") // Her gün saat 03:00'de çalışacak
   // @Scheduled(fixedRate = 120000) // 2 dakikada bir çalışacak (2 dakika = 120.000 milisaniye)
    public void cleanUpDailyAdvertViews() {
        dailyAdvertViewRepository.deleteAll();
    }


//
//    //@Scheduled(cron = "0 0 * * *") // Her gece saat 00:00'da çalışır
//    @Scheduled(fixedRate = 120000) // 2 dakikada bir çalışacak (2 dakika = 120.000 milisaniye)
//    public void clearCache() {
//        redisTemplate.delete("adverts");
//    }


}
