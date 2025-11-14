package jaeger.de.miel.hello_grpc;

import io.grpc.stub.StreamObserver;
import jaeger.de.miel.hello_grpc.proto.HelloReply;
import jaeger.de.miel.hello_grpc.proto.HelloRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HelloServiceMockTest {

    @InjectMocks
    private GrpcServerService grpcServerService;

    @Mock
    private StreamObserver<HelloReply> responseObserver;

    @Test
    public void testOk() {
        HelloRequest request = HelloRequest.newBuilder().setName("Miel").build();

        grpcServerService.sayHello(request, responseObserver);

        ArgumentCaptor<HelloReply> captor = ArgumentCaptor.forClass(HelloReply.class);
        verify(responseObserver, times(1)).onNext(captor.capture());
        verify(responseObserver, times(1)).onCompleted();

        HelloReply reply = captor.getValue();
        assertEquals("Hello ==> Miel", reply.getMessage());
    }

    @Test
    public void testError() {
        HelloRequest errorRequest = HelloRequest.newBuilder().setName("error123").build();

        try {
            grpcServerService.sayHello(errorRequest, responseObserver);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Bad name: error123", e.getMessage());
        }
    }

    @Test
    public void testInternalError() {
        HelloRequest errorRequest = HelloRequest.newBuilder().setName("internal123").build();

        try {
            grpcServerService.sayHello(errorRequest, responseObserver);
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
        }
    }
}
