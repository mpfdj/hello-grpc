package jaeger.de.miel.hello_grpc.interceptors;

import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class MyResponseHeaderInterceptor2 implements ServerInterceptor {

    private static final Metadata.Key<String> SERVER_ID_HEADER =
            Metadata.Key.of("x-server-id-2", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall<ReqT, RespT> wrappedCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                trailers.put(SERVER_ID_HEADER, "TEST-RESPONSE-HEADER-2");
                super.close(status, trailers);
            }
        };

        return next.startCall(wrappedCall, headers);
    }
}