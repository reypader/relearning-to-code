import { check } from 'k6';
import http from 'k6/http';

export const options = {
    discardResponseBodies: true,
    thresholds: {
        'http_req_duration{request_type:readiness}': ['p(99)<100'],
        'http_req_duration{request_type:liveness}': ['p(99)<100'],
        'http_req_duration{request_type:instant}': ['p(99)<100'],
        'http_req_duration{request_type:delayed}': ['p(90)==10000'],
    },
    scenarios: {
        readiness: {
            executor: 'constant-arrival-rate',
            exec: 'readiness',
            duration: '60s',
            rate: 1,
            timeUnit: '1s',
            preAllocatedVUs: 1,
            maxVUs: 500,
        },
        liveness: {
            executor: 'constant-arrival-rate',
            exec: 'liveness',
            duration: '60s',
            rate: 1,
            timeUnit: '1s',
            preAllocatedVUs: 1,
            maxVUs: 500,
        },
        instant: {
            executor: 'constant-arrival-rate',
            exec: 'instant',
            duration: '60s',
            rate: 10,
            timeUnit: '1s',
            preAllocatedVUs: 10,
            maxVUs: 500,
        },
        delayed: {
            executor: 'constant-arrival-rate',
            exec: 'delayed',
            duration: '60s',
            rate: 10,
            timeUnit: '1s',
            preAllocatedVUs: 10,
            maxVUs: 500,
        },

    },
};

export function instant() {
    const res = http.get('http://localhost:8080/instant', {
        tags: { request_type: 'instant' }
    });
    check(res, {
        'status 200': (r) => r.status === 200,
    });
}

export function delayed() {
    const res = http.get('http://localhost:8080/delayed', {
        tags: { request_type: 'delayed' }
    });
    check(res, {
        'status 200': (r) => r.status === 200,
    });
}

export function readiness() {
    const res = http.get('http://localhost:8080/actuator/health/readiness', {
        tags: { request_type: 'readiness' }
    });
    check(res, {
        'status 200': (r) => r.status === 200,
    });
}

export function liveness() {
    const res = http.get('http://localhost:8080/actuator/health/liveness', {
        tags: { request_type: 'liveness' }
    });
    check(res, {
        'status 200': (r) => r.status === 200,
    });
}