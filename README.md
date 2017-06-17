# OpenShift FIS 2.0 Purchase Order Application

This FIS (Fuse Integration Services) application exposes a RESTful API for retrieving and storing *Purchase Orders* in a MySQL database server.  This is a Spring Boot application which runs within an embedded Apache Tomcat Web Server.

**Important Notes:**
- It is assumed that you have already deployed OpenShift Container Platform (v3.3 or higher). You can find details on how to Install OpenShift CP [here](https://docs.openshift.com/container-platform/3.3/install_config/index.html).
- Your system should be configured for Fabric8 Maven Workflow. You can refer to the *Getting Started Guide* [here](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/).
- A Red Hat MySQL xPaaS container should already be deployed and running on your OpenShift installation. Refer to the MySQL xPaaS image documentation for OpenShift in order to deploy a MySQL container using the provided `mysql-ephemeral` template.
- This is a Spring Boot FIS 2.0 application and was tested on a OpenShift Container Platform v3.3 installation. This application should work on OpenShift CP v3.3 or higher.

### S2I Binary Workflow
Follow the steps below to deploy this FIS application from a command window with OpenShift Client Tools installed.  The S2I binary workflow allows developers to rapidly develop FIS applications in an iterative manner - Develop -> Build -> Test -> Deploy.

To build and deploy this application within a container on OpenShift CP, follow the steps below.

1.  Fork this GitHub repository so that it gets added to your GitHub account.  Open a command window (in Linux/MacOS) and switch to an empty directory or create a new directory.  Then clone your repository to create a local copy on your computer and sync between the two locations. Refer to the command below.
```
$ git clone https://github.com/YOUR-USERNAME/ocp-fis2-po
```
Replace `YOUR-USERNAME` with your GitHub account name.

2.  (Optional) The application can be built by issuing the following maven command below.
```
$ mvn clean install
```

3.  Login to your OpenShift Server via OpenShift CLI.  See command below.
```
$ oc login URL -u USER -p PASSWORD
```
Substitute correct values for `URL` (OpenShift Master URL), `USER` & `PASSWORD`. 

4.  The application can be built and the corresponding container can be deployed to your OpenShift instance using a single goal:
```
$ mvn fabric8:deploy -Dmysql-service-username=<username> -Dmysql-service-password=<password>
```
Substitute appropriate values for 'username' and 'password' fields in the maven command above.  The `username` and `password` system properties should correspond to the values used when deploying the MySQL database service.

5.  (Optional) To list all the running pods:
```
$ oc get pods
```

6.  (Optional) Find the name of the pod that runs this application, and output the logs from the running pods with:
```
$ oc logs -f <name of pod>
```

You can also use the OpenShift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the running pods, and view logs and much more.

### S2I (Source to Image) Application Template Workflow

An application template allows developers and IT Operations staff to deploy FIS applications to OpenShift by filling out a form in the OpenShift console and allows them to adjust deployment parameters.  The S2I application template workflow allows IT Operations staff to rapidly deploy and promote FIS applications across multiple regions - Test -> Pre-Production -> Production.

Follow the steps outlined below to deploy this RESTFul FIS application using the S2I application template workflow:

1.  Login to the OpenShift Web Console and create a new project.  Name the project `purchase-orders`.  If you created the project using the OpenShift CLI, then make sure your current project is 'purchase-orders' by using the command below.  The output of this command should display your current project.
```
$ oc project
```

2.  Download the `kubernetes.json` file from this GitHub repository to a machine with OpenShift Client Tools installed on it.  Create the `fis2-sprint-boot-camel-rest-sql` application template object in the OpenShift project `purchase-orders` by issuing the command below.
```
$ oc create -f kubernetes.json
```

3.  Click "Add to Project" button in the OpenShift web console and select the template titled `fis2-spring-boot-camel-rest-sql`.  Make sure you specify the location of this GitHub project in the `GIT_REPO` field and specify appropriate values for the MySQL database server `username` and `password`.  Then click on the **Create** application button on the bottom of the web page.

### Accessing the Purchase Order Application REST API 

As soon as the FIS application is started, 10 purchase orders will be inserted into the backend (MySQL) database.  The inserted purchase orders are numbered starting from id=1 to id=10.  When the application is running, the REST API can be used to list, create, update and delete purchase orders.

**NOTE**: The hostname (route) might vary depending upon your OpenShift setup. Use the command `oc get routes` to determine the hostname to be used in the REST API URL.

The REST API exposed by this FIS application can be accessed by using the _context-path_ (or Base Path) `purchase/`.  The REST API endpoint's exposed are as follows.

**`URI Template : HTTP VERB : DESCRIPTION`**
- `orders`: GET : To list all available purchase orders in the backend database.
- `orders/{id}`: GET : To get order details by `order id`.
- `orders` : POST : To create a new purchase order.  The API consumes and produces orders in `JSON` format.
- `orders/{id}` : PUT : To update a new purchase order. The API consumes and produces orders in `JSON` format.
- `orders/{id}` : DELETE : To delete a purchase order.

You can access the Purchase Order REST API from your Web browser, e.g.:

- http://<hostname_route_url>/purchase/orders
- http://<hostname_route_url>/purchase/orders/1

### Swagger API

The documentation for the REST API's exposed by this Purchase Order FIS application can be accessed by using the _context-path_ `purchase/api-doc`, e.g.:

- http://<hostname_route_url>/purchase/api-doc

