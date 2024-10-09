# Chapter 6: Compare performance of Reactive and Servlet-based

A recurring problem at work is a service that keeps on degrading if downstream services it's dependent on are also degrading.
The observed behavior is other use-cases that are not dependent on the same slow service are being affected because of thread starvation.


In this chapter, we'll be implementing a service in 3 ways: (1) using a servlet-based service with a synchronous controller; (2) using a servlet-based service with an asynchronous controller; and (3) using webflux.
The services will all have the following endpoints:
- `/actuator/health/liveness` and `/actuator/health/readiness` to simulate the probes from K8s.
- `/instant` which instantly responds with `HTTP 200`.
- `/delayed` which responds with `HTTP 200` with a random delay between 100 and 10,000 milliseconds.

Ideally, calls to `/instant` will always be serviced successfully regardless of how degraded the performance of `/delayed` is. 
However, the observed behavior on synchronous services is that all endpoints, including the probes, experience drastic degradations.

## Test Setup
We'll be using K6 to perform the tests. The `/instant` and `/delated` endpoints will be hit by a constant 10RPS load while the `/liveness` and `/readiness` will be hit by a constant 1RPS load.

## Results
### Synchronous Servlet Service
```
     http_req_duration..............: avg=27.62s   min=2.53ms   med=26.75s  max=59.09s p(90)=51.29s p(95)=54.04s
       { expected_response:true }...: avg=27.62s   min=2.53ms   med=26.75s  max=59.09s p(90)=51.29s p(95)=54.04s
     ✗ { request_type:delayed }.....: avg=30s      min=967.79ms med=29.57s  max=59.09s p(90)=52.67s p(95)=55.19s
     ✗ { request_type:instant }.....: avg=25.89s   min=2.53ms   med=24.37s  max=56.81s p(90)=50.14s p(95)=53.22s
     ✗ { request_type:liveness }....: avg=25.35s   min=7.86ms   med=23.9s   max=53.46s p(90)=47.82s p(95)=51.29s
```
**Observations:**
- All requests are degraded. Worse still, the requests to `/delayed` seems to have had a compounding negative effect pushing the latencies beyond 50 seconds.


### Asynchronous Servlet Service
```
     http_req_duration..............: avg=2.16s   min=682µs   med=5.72ms max=9.98s    p(90)=7.68s  p(95)=8.86s
       { expected_response:true }...: avg=2.16s   min=682µs   med=5.72ms max=9.98s    p(90)=7.68s  p(95)=8.86s
     ✓ { request_type:delayed }.....: avg=4.98s   min=121.7ms med=4.74s  max=9.98s    p(90)=9.03s  p(95)=9.6s
     ✓ { request_type:instant }.....: avg=3.69ms  min=682µs   med=3.32ms max=103.78ms p(90)=5.45ms p(95)=5.93ms
     ✓ { request_type:liveness }....: avg=7.58ms  min=1.9ms   med=5.23ms max=131.61ms p(90)=7.02ms p(95)=8.24ms
     ✓ { request_type:readiness }...: avg=7.57ms  min=1.98ms  med=5.22ms max=131.68ms p(90)=7.02ms p(95)=7.9ms
```
**Observations:**
- No apparent degradation to other endpoints. Interestingly, even the requests to `/delayed` have had a consistent performance.

### Reactive Service
```
     http_req_duration..............: avg=2.22s   min=850µs    med=6.87ms max=9.99s    p(90)=7.75s  p(95)=8.8s
       { expected_response:true }...: avg=2.22s   min=850µs    med=6.87ms max=9.99s    p(90)=7.75s  p(95)=8.8s
     ✓ { request_type:delayed }.....: avg=5.13s   min=121.21ms med=5.09s  max=9.99s    p(90)=8.96s  p(95)=9.44s
     ✓ { request_type:instant }.....: avg=4.69ms  min=850µs    med=4.19ms max=117.19ms p(90)=6.35ms p(95)=6.9ms
     ✓ { request_type:liveness }....: avg=9.15ms  min=2.07ms   med=6.65ms max=141.73ms p(90)=8.91ms p(95)=10.06ms
     ✓ { request_type:readiness }...: avg=9.15ms  min=2.07ms   med=6.63ms max=141.73ms p(90)=8.91ms p(95)=10.35ms
```
**Observations:**
- Same as `Asyncronous Servlet Service`

## Additional tests and observations needed
- Observe resource usage of the various services under load.
- Find a way to verify that we have equivalent configurations between the three services.
- Verify the amount of worker threads needed by the Synchronous Service to satisfy the test.

## Running the tests
1. Build and run the intended service variant. Starting from the `rtc-chap6-reactive-vs-servlet` directory:
```shell
$ cd rtc-chap6-<variant> 
$ mvn clean install
$ docker-compose up
```
2. Run the desired service variant. Starting from the `rtc-chap6-reactive-vs-servlet` directory:
```shell
$ cd k6
$ k6 run api-test.js
```