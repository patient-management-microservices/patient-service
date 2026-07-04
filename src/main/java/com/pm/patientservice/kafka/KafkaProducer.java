package com.pm.patientservice.kafka;

import com.pm.patientservice.enums.PatientEventType;
import com.pm.patientservice.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${spring.kafka.topic.patient}")
    private String patientTopic;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(PatientEventType.PATIENT_CREATED.name())
                .build();

        try {
            kafkaTemplate.send(patientTopic, event.toByteArray());
        } catch (Exception e) {
            log.error("Failed to send event to Kafka Topic: {} Event {}", e.getMessage(), event);
        }
    }
}
