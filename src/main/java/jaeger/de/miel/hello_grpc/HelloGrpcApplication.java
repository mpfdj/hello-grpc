package jaeger.de.miel.hello_grpc;

import io.grpc.ServerInterceptor;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.reflection.v1.ServerReflectionGrpc;
import jaeger.de.miel.hello_grpc.interceptors.MyLoggingInterceptor;
import jaeger.de.miel.hello_grpc.interceptors.MyResponseHeaderInterceptor;
import jaeger.de.miel.hello_grpc.interceptors.MyResponseHeaderInterceptor2;
import jaeger.de.miel.hello_grpc.proto.SimpleGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.grpc.autoconfigure.server.GrpcServerFactoryCustomizer;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.grpc.server.DefaultGrpcServerFactory;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.ServerServiceDefinitionFilter;
import org.springframework.grpc.server.ShadedNettyGrpcServerFactory;
import org.springframework.grpc.server.service.ServerInterceptorFilter;

import java.util.Set;

@SpringBootApplication
public class HelloGrpcApplication {

    static void main(String[] args) {
		SpringApplication.run(HelloGrpcApplication.class, args);
	}


    //------------------------------------------------------------------------------------------------------------------
    // https://docs.spring.io/spring-grpc/reference/server.html#service-filtering
    // Service filtering
    // Filter out Health and Reflection services
    //------------------------------------------------------------------------------------------------------------------
    @Bean
    ServerServiceDefinitionFilter myServiceFilter() {
        return (serviceDefinition, __) ->
                !Set.of(HealthGrpc.SERVICE_NAME, ServerReflectionGrpc.SERVICE_NAME)
                        .contains(serviceDefinition.getServiceDescriptor().getName());
    }

    @Bean
    GrpcServerFactoryCustomizer myServerFactoryCustomizer(ServerServiceDefinitionFilter myServiceFilter) {

        // Use shaded Netty factory instead of default NettyGrpcServerFactory
        // https://docs.spring.io/spring-grpc/reference/server.html#service-filtering

        return factory -> {
            if (factory instanceof ShadedNettyGrpcServerFactory nettyServerFactory) {
                nettyServerFactory.setServiceFilter(myServiceFilter);
            }
        };
    }


    //------------------------------------------------------------------------------------------------------------------
    // https://docs.spring.io/spring-grpc/reference/server.html#_global
    // Global interceptor
    //------------------------------------------------------------------------------------------------------------------

    @Bean
    @Order(100)
    @GlobalServerInterceptor
    ServerInterceptor myGlobalLoggingInterceptor() {
        return new MyLoggingInterceptor();
    }

    @Bean
    @Order(200)
    @GlobalServerInterceptor
    ServerInterceptor myGlobalResponseHeaderInterceptor() { return new MyResponseHeaderInterceptor(); }

    //------------------------------------------------------------------------------------------------------------------
    // https://docs.spring.io/spring-grpc/reference/server.html#global-server-interceptor-filtering
    // Global sever interceptor filtering
    //------------------------------------------------------------------------------------------------------------------




    //------------------------------------------------------------------------------------------------------------------
    // https://docs.spring.io/spring-grpc/reference/server.html#_per_service
    // Configure interceptor per service
    //------------------------------------------------------------------------------------------------------------------
    @Bean
    MyResponseHeaderInterceptor2 myResponseHeaderInterceptor2() {
        return new MyResponseHeaderInterceptor2();
    }


    //------------------------------------------------------------------------------------------------------------------
    // https://docs.spring.io/spring-grpc/reference/client.html#_application_properties
    // Setting up a grpc client
    //------------------------------------------------------------------------------------------------------------------

//    @Bean(name = "stub")
//    @Lazy
//    SimpleGrpc.SimpleBlockingStub stub(GrpcChannelFactory channels) {
//        return SimpleGrpc.newBlockingStub(channels.createChannel("local"));
//    }



}
