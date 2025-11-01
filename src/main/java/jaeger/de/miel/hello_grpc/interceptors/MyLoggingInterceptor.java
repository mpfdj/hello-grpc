package jaeger.de.miel.hello_grpc.interceptors;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyLoggingInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyLoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();
        logger.info("Received gRPC call: {}", methodName);
        logger.debug("Metadata: {}", headers);

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onMessage(ReqT message) {
                logger.debug("Request message: {}", message);
                super.onMessage(message);
            }

            @Override
            public void onComplete() {
                logger.info("Call completed: {}", methodName);
                super.onComplete();
            }

            @Override
            public void onCancel() {
                logger.warn("Call cancelled: {}", methodName);
                super.onCancel();
            }
        };
    }
}