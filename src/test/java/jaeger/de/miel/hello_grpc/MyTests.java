package jaeger.de.miel.hello_grpc;

import io.grpc.health.v1.HealthGrpc;
import io.grpc.reflection.v1.ServerReflectionGrpc;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyTests {


    @Test
    public void testWithSet1() {
        var result = !Set.of(HealthGrpc.SERVICE_NAME, ServerReflectionGrpc.SERVICE_NAME).contains(HealthGrpc.SERVICE_NAME);
        assertFalse("Expecting false for getServiceDescriptor name is 'grpc.health.v1.Health'.", result);
    }

    @Test
    public void testWithSet2() {
        var result = !Set.of(HealthGrpc.SERVICE_NAME, ServerReflectionGrpc.SERVICE_NAME).contains("Simple");
        assertTrue("Expecting true for getServiceDescriptor name is 'Simple'.", result);
    }

}
