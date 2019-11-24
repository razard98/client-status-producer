package com.golfzon.gs.clinet.status.producer;

import com.golfzon.gs.clinet.status.producer.model.GsClientStatus;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class ClientStatusProducerApplication implements ApplicationRunner {

    private static final int MAX_THREAD_COUNT = 5;
    //private final KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ClientStatusProducerApplication.class, args);
    }

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.56.102:9092,192.168.56.102:9093,192.168.56" +
                ".102:9094");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    //@Scheduled(cron = "0/1 * * * * *")
    public void sendMessage() throws InterruptedException {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        while (true) {
            TimeUnit.MICROSECONDS.sleep(100);
            log.info("send message...");
            String data =
                    new Gson().toJson(GsClientStatus.builder().frameId(11).categoryId(2).stepId(6).actionType(1)
                            .lockKey(100).shopCode(1).sysType(1)
                            .time(System.currentTimeMillis() / 1000).online(1).ver("ver1.1").build());
            Message<String> message = MessageBuilder.withPayload(data)
                    .setHeader(KafkaHeaders.TOPIC, "gs.monitor")
                    //.setHeader(KafkaHeaders.MESSAGE_KEY, "100")
                    .build();
            kafkaTemplate.send(message);
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
        IntStream.range(0, MAX_THREAD_COUNT).forEach(n -> executorService.execute(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(300);
                log.info("{} - hello", Thread.currentThread().getName());
                sendMessage();
            } catch (Exception e) {
                log.error("error", e);
                e.printStackTrace();
            }
        }));
    }
}
