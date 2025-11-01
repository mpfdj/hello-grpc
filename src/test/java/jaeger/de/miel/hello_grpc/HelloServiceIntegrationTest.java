package jaeger.de.miel.hello_grpc;

import jaeger.de.miel.hello_grpc.proto.HelloReply;
import jaeger.de.miel.hello_grpc.proto.HelloRequest;
import jaeger.de.miel.hello_grpc.proto.SimpleGrpc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


// Use the SpringRunner.class when using junit 4 !!!
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceIntegrationTest {

    @Autowired
    private SimpleGrpc.SimpleBlockingStub stub;

    @Test
    public void test(){
        String name = "integration test";
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply reply = stub.sayHello(request);
        var msg = reply.getMessage();

        assertThat(msg, containsString("Hello ==> integration test"));
    }

}
