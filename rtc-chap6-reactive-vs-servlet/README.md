# Chapter 6: Compare performance of Reactive and Servlet-based

A recurring problem at work is a service that keeps on degrading if downstream services it's dependent on are also degrading.
The observed behavior is other use-cases that are not dependent on the same slow service are being affected because of thread starvation.


In this chapter, we'll be implementing a service in 4 ways: (1) using a servlet-based service with a synchronous controller; (2) using a servlet-based service with an asynchronous controller for the delayed endpoint; (3) just like (2) but with both delayed and instant endpoints delegating to an async process; and (4) using webflux.
The services will all have the following endpoints:
- `/actuator/health/liveness` and `/actuator/health/readiness` to simulate the probes from K8s.
- `/instant` which instantly responds with `HTTP 200`.
- `/delayed` which responds with `HTTP 200` with a delay of 10 seconds.

Ideally, calls to `/instant` will always be serviced successfully regardless of how degraded the performance of `/delayed` is. 
However, the observed behavior on synchronous services is that all endpoints, including the probes, experience drastic degradations.

## Test Setup
We'll be using K6 to perform the tests. The `/instant` and `/delated` endpoints will be hit by a constant 10RPS load while the `/liveness` and `/readiness` will be hit by a constant 1RPS load.

## Results
### Synchronous Servlet Service
```
     http_req_duration..............: avg=44.54s   min=2.02ms  med=58.41s  max=1m0s    p(90)=59.99s   p(95)=1m0s
       { expected_response:true }...: avg=29.9s    min=2.02ms  med=31.53s  max=58.94s  p(90)=56.05s   p(95)=57.97s
       { request_type:delayed }.....: avg=47.94s   min=10s     med=59.99s  max=1m0s    p(90)=59.99s   p(95)=1m0s
       { request_type:instant }.....: avg=42.13s   min=2.02ms  med=56.06s  max=1m0s    p(90)=59.99s   p(95)=1m0s
       { request_type:liveness }....: avg=40.32s   min=13.73ms med=47.97s  max=1m0s    p(90)=59.99s   p(95)=59.99s
       { request_type:readiness }...: avg=40.3s    min=11.4ms  med=47.97s  max=1m0s    p(90)=59.99s   p(95)=59.99s
```
**Observations:**
- All requests are degraded. Worse still, the requests to `/delayed` seems to have had a compounding negative effect pushing the latencies beyond 50 seconds.


### Asynchronous Servlet Service (/delayed only)
```
     http_req_duration..............: avg=4.14s   min=875µs  med=5.77ms max=10.08s   p(90)=10s     p(95)=10s
       { expected_response:true }...: avg=4.14s   min=875µs  med=5.77ms max=10.08s   p(90)=10s     p(95)=10s
       { request_type:delayed }.....: avg=10s     min=10s    med=10s    max=10.08s   p(90)=10s     p(95)=10.01s
       { request_type:instant }.....: avg=4.02ms  min=875µs  med=3.69ms max=83.37ms  p(90)=5.59ms  p(95)=6.03ms
       { request_type:liveness }....: avg=7.37ms  min=1.37ms med=6.05ms max=111.4ms  p(90)=7.61ms  p(95)=8.04ms
       { request_type:readiness }...: avg=7.31ms  min=1.46ms med=5.96ms max=111.37ms p(90)=7.51ms  p(95)=7.63ms
```
**Observations:**
- No apparent degradation to other endpoints. Interestingly, even the requests to `/delayed` have had a consistent performance.

### Asynchronous Servlet Service (/delayed and /instant)
```
     http_req_duration..............: avg=4.09s   min=1.07ms med=4.79ms max=10.01s  p(90)=10s    p(95)=10s
       { expected_response:true }...: avg=4.09s   min=1.07ms med=4.79ms max=10.01s  p(90)=10s    p(95)=10s
       { request_type:delayed }.....: avg=9.87s   min=9.34s  med=10s    max=10.01s  p(90)=10s    p(95)=10s
       { request_type:instant }.....: avg=3.63ms  min=1.07ms med=3.67ms max=9.75ms  p(90)=4.69ms p(95)=5.02ms
       { request_type:liveness }....: avg=4.76ms  min=1.77ms med=4.73ms max=10.46ms p(90)=5.72ms p(95)=6.34ms
       { request_type:readiness }...: avg=4.78ms  min=1.75ms med=4.78ms max=10.48ms p(90)=5.75ms p(95)=6.35ms
```
**Observations:**
- Same as `Asyncronous Servlet Service`

### Reactive Service
```
     http_req_duration..............: avg=4.15s   min=951µs  med=6.91ms max=10.11s   p(90)=10s    p(95)=10s
       { expected_response:true }...: avg=4.15s   min=951µs  med=6.91ms max=10.11s   p(90)=10s    p(95)=10s
       { request_type:delayed }.....: avg=10s     min=10s    med=10s    max=10.11s   p(90)=10.01s p(95)=10.01s
       { request_type:instant }.....: avg=4.99ms  min=951µs  med=4.7ms  max=111.82ms p(90)=6.52ms p(95)=7.03ms
       { request_type:liveness }....: avg=9.44ms  min=5.1ms  med=7.08ms max=132.97ms p(90)=8.82ms p(95)=9.24ms
       { request_type:readiness }...: avg=9.37ms  min=5.1ms  med=7.1ms  max=132.96ms p(90)=8.82ms p(95)=9.18ms
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