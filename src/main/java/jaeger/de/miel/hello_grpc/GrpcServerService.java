package jaeger.de.miel.hello_grpc;

import io.grpc.stub.StreamObserver;
import jaeger.de.miel.hello_grpc.interceptors.MyResponseHeaderInterceptor2;
import jaeger.de.miel.hello_grpc.proto.HelloReply;
import jaeger.de.miel.hello_grpc.proto.HelloRequest;
import jaeger.de.miel.hello_grpc.proto.SimpleGrpc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

@Service
@GrpcService(interceptors = MyResponseHeaderInterceptor2.class)  // Add interceptor on Service level
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

    private static final Log log = LogFactory.getLog(GrpcServerService.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String name = request.getName();

        log.info("Hello " + name);

        if (name.startsWith("error")) throw new IllegalArgumentException("Bad name: " + name);
        if (name.startsWith("internal")) throw new RuntimeException();

        String message = "Hello ==> " + name;

        HelloReply reply = HelloReply
                .newBuilder()
                .setMessage(message)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void streamHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String name = request.getName();

        log.info("Hello " + name);

        int count = 0;
        while (count < 10) {
            String message = "Hello(" + count + ") ==> " + name;

            HelloReply reply = HelloReply
                    .newBuilder()
                    .setMessage(message)
                    .build();

            responseObserver.onNext(reply);
            count++;

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                responseObserver.onError(e);
                return;
            }
        }

        responseObserver.onCompleted();
    }
}
