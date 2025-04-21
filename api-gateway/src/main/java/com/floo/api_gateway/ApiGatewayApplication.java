package com.floo.api_gateway;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Configuration
	static class GatewayConfig {

//		@Bean
//		public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//			return builder.routes()
//					.route("auth-service-route", p -> p
//							.path("/auth-service/**")
//							.filters(f -> f.stripPrefix(1))
//							.uri("lb://auth-service"))
//					.route("order-service-route", p -> p
//							.path("/order-service/**")
//							.filters(f -> f.stripPrefix(1))
//							.uri("lb://order-service"))
//					.route("test-route", p -> p
//							.path("/test/**")
//							.filters(f -> f.stripPrefix(1))
//							.uri("http://auth-service.default.svc.cluster.local:8081")
//					)
//					.build();
//		}

		@Bean
		public ApplicationRunner runner(DiscoveryClient discoveryClient) {
			return args -> {
				System.out.println("=== Registered services ===");
				discoveryClient.getServices().forEach(System.out::println);
			};
		}

	}

}
