/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.redhat.fuse.demo.porders;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(
            new CamelHttpTransportServlet(), "/purchase/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
            restConfiguration()
                .contextPath("/purchase").apiContextPath("/api-doc")
                    .apiProperty("api.title", "Purchase Orders REST API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
		.dataFormatProperty("json.out.prettyPrint","true");
/*
            rest("/purchase").description("Purchase Orders REST service")
                .get("/").description("The list of all the purchase orders")
                    .route().routeId("purchase-order-list")
                    .to("sql:select distinct description from orders?" +
                        "dataSource=dataSource&" +
                        "outputClass=com.redhat.fuse.demo.porders.PurchaseOrder")
                    .endRest()
                .get("order/{id}").description("Details of a purchase order by id")
                    .route().routeId("order-api")
                    .to("sql:select * from orders where id = :#${header.id}?" +
                        "dataSource=dataSource&outputType=SelectOne&" +
                        "outputClass=com.redhat.fuse.demo.porders.Order");
*/
        }
    }

    @Component
    class Backend extends RouteBuilder {

        @Override
        public void configure() {
            // A first route generates some orders (~10) and queue them in DB
            from("timer:new-order?delay=1s&period={{quickstart.generateOrderPeriod:2s}}&repeatCount=10")
                .routeId("generate-order")
                .bean("orderService", "generateOrder")
                .to("sql:insert into orders (item, price, quantity, description, cname, dcode, origin) values " +
                    "(:#${body.item}, :#${body.price}, :#${body.quantity}, :#${body.description}, :#${body.cname}, :#${body.dcode}, :#${body.origin})?" +
                    "dataSource=dataSource")
                .log("Inserted new order for item=${body.item}");

/*
            // A second route polls the DB for new orders and processes them
            from("sql:select * from orders where processed = false?" +
                "consumer.onConsume=update orders set processed = true where id = :#id&" +
                "consumer.delay={{quickstart.processOrderPeriod:5s}}&" +
                "dataSource=dataSource")
                .routeId("process-order")
                .bean("orderService", "rowToOrder")
                .log("Processed order #id ${body.id} with ${body.amount} copies of the «${body.description}» book");
*/
        }
    }
}
