package jaeger.de.miel.hello_grpc.interceptors;

import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class MyResponseHeaderInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> SERVER_ID_HEADER =
            Metadata.Key.of("x-server-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall<ReqT, RespT> wrappedCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                trailers.put(SERVER_ID_HEADER, "my-server-123");
                super.close(status, trailers);
            }
        };

        return next.startCall(wrappedCall, headers);
    }
}