package com.example.messagingstompwebsocket.longpolling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequestMapping("/long-polling")
@RestController
public class LongPollingController {

    private final ConcurrentMap<Long, DeferredResult<ResponseEntity<LongPollingMessage>>> roomSession = new ConcurrentHashMap<>();
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/messages")
    public DeferredResult<ResponseEntity<LongPollingMessage>> getMessage(@RequestParam("roomId") Long roomId) {
        log.info("getMessage");
        DeferredResult<ResponseEntity<LongPollingMessage>> output = new DeferredResult<>(5000L);

        output.onTimeout(() -> {
            log.error("{}: onTimeout", roomId);
            output.setErrorResult(ResponseEntity.noContent().build());
        });
        output.onError(e -> {
            log.error("{}: onError", roomId);
            output.setErrorResult(ResponseEntity.internalServerError().build());
        });
        output.onCompletion(() -> log.info("{}: onComplete", roomId));
        roomSession.put(roomId, output);

        return output;
    }

    @PostMapping("/messages")
    public ResponseEntity<String> postMessage(@RequestBody Post post) {
        log.info("postMessage: {}", post);
        roomSession.get(post.getRoomId()).setResult(ResponseEntity.ok(new LongPollingMessage(post.getMessage())));
        return ResponseEntity.ok("success");
    }
}
