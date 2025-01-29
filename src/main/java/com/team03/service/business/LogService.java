package com.team03.service.business;

import com.team03.entity.business.Advert;
import com.team03.entity.business.Log;
import com.team03.entity.user.User;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.LogMappers;
import com.team03.payload.response.business.LogResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.LogRepository;
import com.team03.service.helper.MethodHelper;
import com.team03.service.helper.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final LogMappers logMappers;
    private final MethodHelper methodHelper;

    public Log createLog(String log, User user, Advert advert, LocalDateTime create_at, String desc) {

        Log logObj= Log.builder()
                .log(log)
                .user(user)
                .description(desc)
                .advert(advert)
                .createAt(LocalDateTime.now())
                .build();
        return logRepository.save(logObj);

    }


    public ResponseMessage<List<LogResponse>> getAll() {
        List<Log> logs= logRepository.findAll();
        List<LogResponse> logResponses = logs.stream().map(logMappers::logToLogResponse).toList();
        return ResponseMessage.<List<LogResponse>>builder()
                .message(MessageUtil.getMessage("logs.loaded"))
                .object(logResponses)
                .httpStatus(HttpStatus.OK).build();
    }

    public ResponseMessage<List<LogResponse>> getByUser(Long id) {
        methodHelper.getUserById(id);
        List<Log> logs=  logRepository.findByUser(id);
        List<LogResponse> logResponses = logs.stream().map(logMappers::logToLogResponse).toList();
        return ResponseMessage.<List<LogResponse>>builder()
                .message(MessageUtil.getMessage("logs.loaded"))
                .object(logResponses)
                .httpStatus(HttpStatus.OK).build();
    }


    public ResponseMessage<Page<LogResponse>> getByUserPage(Long id, int page, int size,
                                                            String sort, String type) {

        Pageable pageable = methodHelper.getPageableWithProperties(page, size, sort, type);
        Page<Log> logs=logRepository.findAllLogsById(id,pageable);
        return ResponseMessage.<Page<LogResponse>>builder()
                .object(new RestPage<> (logs.map(logMappers::logToLogResponse)))
                .message(MessageUtil.getMessage("logs.loaded"))
                .httpStatus(HttpStatus.OK).build();
    }

    public ResponseMessage<Page<LogResponse>> getAllPage(int page, int size, String sort, String type) {
        Pageable pageable = methodHelper.getPageableWithProperties(page, size, sort, type);
        Page<Log> logs=  logRepository.findAll(pageable);
        return ResponseMessage.<Page<LogResponse>>builder()
                .object(new RestPage<> (logs.map(logMappers::logToLogResponse)))
                .message(MessageUtil.getMessage("logs.loaded"))
                .httpStatus(HttpStatus.OK).build();
    }
}
