# Chapter 1: Basic Web-app

Let's start our relearning journey by creating a very basic web app with a POST and GET endpoints. For our purposes here, we won't be adding any form of real persistence layer - only in-memory storage. We'll persistence in the next chapter. We also won't be tackling unit testing just yet. Ideally, we would have been doing this using TDD but to not overcomplicate this first step, let's aim for a very quick win. To that end, we'll create an app that manages a log of events that have happened.

## Initializing the Service
Let's use Spring Initializr to create the initial project structure with just `spring-boot-starter-web`.

## The endpoints
### Create Events
We'll need to create a simple POST endpoint to create event entities. Let's describe it as follows:

```
Request:
POST /events
{
    "start": "2024-09-22T19:27:00+0800",
    "end": "2024-09-22T19:28:00+0800",
    "name": "I_STARTED_RELEARNING" 
}

Response:
HTTP 201
{
    "id": "7681e915-625d-423c-8f2f-ba686639a783",
    "start": "2024-09-22T19:27:00+0800",
    "end": "2024-09-22T19:28:00+0800",
    "name": "I_STARTED_RELEARNING" 
}
```

The fields would be defined as follows:
- `start` and `end` are ISO8601 timestamp strings.
- `name` is a 50-character string with no spaces.
- `id` is a UUID string.

### Get an Event
We'll need to create a simple GET endpoint to get a specific event. The response body is similar the one in `Create Events`.

```
Request:
Get /events/7681e915-625d-423c-8f2f-ba686639a783

Response:
HTTP 200
{
    "id": "7681e915-625d-423c-8f2f-ba686639a783",
    "start": "2024-09-22T19:27:00+0800",
    "end": "2024-09-22T19:28:00+0800",
    "name": "I_STARTED_RELEARNING" 
}
```

The fields would be defined as follows:
- `start` and `end` are ISO8601 timestamp strings.
- `name` is a 50-character string with no spaces.
- `id` is a UUID string.

### Get Events
To round things off, we'll also create an endpoint to list all known events. Let's keep it simple and omit any pagination.

```
Request:
Get /events/

Response:
HTTP 200
[
    {
        "id": "7681e915-625d-423c-8f2f-ba686639a783",
        "start": "2024-09-22T19:27:00+0800",
        "end": "2024-09-22T19:28:00+0800",
        "name": "I_STARTED_RELEARNING" 
    },
    {
        "id": "e738d7ed-9eaf-4b4c-b2ca-81ee25bff4ff",
        "start": "2024-09-22T19:36:00+0800",
        "end": "2024-09-22T19:55:00+0800",
        "name": "I_HAD_DINNER" 
    }
]
```

The fields would be defined as follows:
- `start` and `end` are ISO8601 timestamp strings.
- `name` is a 50-character string with no spaces.
- `id` is a UUID string.
- The response is an array of event resources.