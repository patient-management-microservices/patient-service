package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

    public BillingServiceGrpcClient(@Value("${billing.service.address}") String serverAddress,
                                    @Value("${billing.service.grpc.port}") int serverPort) {
        log.info("Connecting to Billing service GRPC service at serverAddress={}, serverPort={}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }
    
    public BillingResponse createBillingAccount(String patientId, String patientName, String patientEmail) {
        log.info("Creating billing account for patientId={}, patientName={}, patientEmail={}", patientId, patientName, patientEmail);

        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(patientName)
                .setEmail(patientEmail)
                .build();

        BillingResponse response = billingServiceBlockingStub.createBillingAccount(request);
        log.info("Billing account created for patientId={}, accountId={}, status={}", patientId, response.getAccountId(), response.getStatus());

        return response;
    }
}
