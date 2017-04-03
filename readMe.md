# Html Parser

> The simplicity is a key.

### How to build

execute from the terminal build.sh which is located in the project root.

### How to run
execute the above command in target directory in the project root 

java -jar technical.challenge-0.0.1-SNAPSHOT.jar

### Simple HTML Parser Form 

This application aims to parse a given link by user and show the required information which is described below:

* Version of Document
* Page title
* Number of headings grouped by heading level
* Number of hypermedia links grouped by internal/external

The application has also a login form by using GitHub, Facebook and Spiegel login form.


## Technical Infrastructure

### Language
Java 8

### Runtime
JDK 8

### Build
Maven is the preferential build manager. Just run the build.sh in the project file

### HTML Parser
JSoup

### Framework
In this application Spring framework was selected with these features: Spring Boot, Spring MVC, Spring Security, Angular.js  
   
### Why Spring?
It provides a strong infrastructure and to get us productivity as qucikly as possible. 
It is simple to integrate with other Java frameworks
Spring's dependency injection provides us to write testable, robust code.  

### Spring Boot
Spring Boot motto is "just run". You don't need to worry about server side part because Tomcat/Jetty are embedded and no need to deploy WAR externally.
It is easy to manage dependency : adding "springboot-starter-web" in a maven pom.xml, it will pull all used libraries while developing MVC application.
As we know, Tomcat is a servlet container application server. In Spring Boot Application class is a simple java class annotated with @SpringApplication
When we run it,[http://localhost:8080/]will be able to reachable. 

```  
@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
```

Just this code block is enough for our application to get up and run. Simple and cool way. 


### Spring MVC
It is flexible instead og other Java frameworks like Struts. It provides multiple view technologies except JSP(XSLT,XL,Velocity )
It provides Model-View-Controller architecture and there are many advantages to implement enterprise applicatons via Spring MVC

### Spring Security
We need a security framework to make our application secure.It focuses on authentication and authorization. In this application
github and facebook(social login) with oauth2 authentication  and der spiegel login. 
There are two common approach for authentication : Session/Cookie based, Oauth2/Api and some stateless ones like JSonWebTokens,Http Basic Auth
I implemented oauth2 authentication for social login part using Spring Security and Spring Boot just add the dependencies in maven pom.xml


### JSoup
It is java library to extract and manupulate HTML data. 

### Test
Spring boot test library

### Angular.js
Angular.js is a javascript framework and it is well formed with Spring MVC.
P.S: I am not good at Angular.js, this is my first :) 

Default port: 8080
Application url : localhost:/8080

### Application Flow

If there is no user is logged in the application redirect to login page. 
After logged in there is a form has a textbox to query desired url(HTML page).  
There is a logout button on the page and username is shown next to it. After submit form, url analyse will be shown in the below.  

To make the application secure with Spring security, we create a class which extends WebSecurityConfigurerAdapter and override configure method:

``` 
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.antMatcher("/**")
		.authorizeRequests().antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest()
				.authenticated().and().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
				.and().logout().logoutSuccessUrl("/").permitAll().and().
				csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
				.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
		
	}
```
   
@EnableWebSecurity annoatation enables the Spring security suppport and Spring MVC integration.
I also add csrf protection in configure method. Angular.js is slightly different from Spring security.
csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) is added for Angular.js

Some configuration methods in this class helps to login process. 
application.yml file keeps required token keys,token uris, oauth url related to social login accounts 
and some logging configuration properties like log level and log format. 

The method which is in the above, handles the redirections from facebook,github to the application  
via servlet Filter

```
@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}
```


### Exception Mechanism
I implemented exception mechanism by using @ControllerAdvice annotation which is based on Global Exception Handling.
@ExceptionHandler annotation is used in the handler methods. Even if I handled one custom exception type,
it would be capable of handling all the global exception scenarios.


### Logging
I activated spring security and spring web logging in error level so I did not add extra log code here. 
I prefer to add log info/error codes into business part.  
	
### Test 
There are some useful methods were implemented to test analyzing functions. I just implemented unit test cases but some scenario based cases should be considered in real. There are some qualified tools(like Selenium) to perform these scenarios and provide end to end scalable application. 
Spring boot offers also integration test support. Integration test is very helpful if your application is 
distributed.


### Form Validation
Angular.js makes forms easy and provides simple/quick form validation. I used Angular form validation.

### UnImplemented Part
I did redirection to login page with javascript, the main idea of login part here would be getting html document elements via some javascript
calls and check if some login id/name attributes are in the page. If it does not include any login related attributes, just close the popup and 
get username as an authenticated user. Github provides oauth authentication but Spiegel does not. So two different way  should be considered.

### Optinal Part
Validation if each link is available via HTTPS would be slow, especially if we have too many links in requested page.
There are two ways to check links:
First we can do this to implement some java code on server side. But performance is the issue, we can enhance the performance by multithreading.
Second way would be Asynchronous Javascript requests. Running the request asynchronously will ensure our page is responsive and 
the rest of code continues to run while HTTP request is taking its time. 

 

