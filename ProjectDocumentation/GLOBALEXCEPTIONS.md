# GlobalExceptionHandler Reference
> This structured approach ensures the APIs are robust, secure, and developer-friendly, 
> with every error clearly explained and handled exactly where it belongs in the application flow.

## Table of Contents
1.[Database/Uniqueness](#1-databaseuniqueness)

2.[Input/Validation](#2-inputvalidation)

3.[Routing/HTTP Method](#3-routinghttp-method)

4.[Resource-level](#4-resource-level)

5.[Security/Auth](#5-securityauth)

6.[Domain-specific](#6-domain-specific)

7.[Low-level/fallback](#7-low-levelfallback)

## 1. Database/Uniqueness
   Why first?

> Data integrity must be enforced before anything else. If a request violates database constraints 
> (like unique product fields), it is critical to reject it before proceeding further.

** Example: Two clients try to create the same product at the same time. 
The database unique index will block duplicates with a DuplicateKeyException—
this should be caught immediately and a clear error sent to the client.

Benefit: Ensures absolute prevention of data corruption and inconsistency at the lowest system layer, 
regardless of any business logic or validation quirks.

## 2. Input/Validation
   Why second?

> Client errors (malformed JSON, missing/wrong data types, invalid DTO values) are common and should be caught 
> as early as possible by your validation mechanisms.

** Example: Missing "name" field or an invalid price format in a request triggers a 400 error.

Benefit: Fast feedback for client errors, saves server resources, avoids moving forward with unusable data.

[Back to Table of Contents](#table-of-contents)

## 3. Routing/HTTP Method
   Why third?

> After confirming the request is well-formed, make sure it targets a valid endpoint and uses the correct HTTP method.

** Example: Requesting POST on a GET-only resource, or typo in URL like /user/me instead of /users/me triggers 404/405.

Benefit: Enforces RESTful standards, helps clients correct usage patterns quickly.

## 4. Resource-Level
   Why next?

>When the endpoint and method are valid, your logic checks that the requested resource actually exists.

** Example: Looking up a product by ID—if not found, respond with 404 Not Found.

Benefit: Clear separation between “endpoint does not exist” and “resource does not exist,” keeps business errors uncluttered.

[Back to Table of Contents](#table-of-contents)

## 5. Security/Auth
   Why after resource checks?

> Only attempt security/authorization checks after you’ve validated the request and confirmed the resource exists.

** Example: A valid update/delete request must be checked if the user is authenticated and authorized for the resource.

Benefit: Prevents revealing resource existence to unauthorized users, 
strictly controls access, and provides clear error messages for permission issues.

## 6. Domain-Specific
   Why then?

> Handles business logic errors not covered by framework checks—such as duplicate user registration, 
> invalid arguments (custom role values), etc.

** Example: Registering with an email that exists, specifying a non-existent role, etc.

Benefit: Keeps application-specific errors clearly separate from framework/core system errors.

[Back to Table of Contents](#table-of-contents)

## 7. Low-level/Fallback
   Why last?

> Global catch-all for any uncaught or unexpected errors.

** Example: Unanticipated null pointer, external API failure, or bugs.

Benefit: Prevents leaking stack traces or technical system details, 
guarantees user-friendly responses and audit trail for critical application failures.

## Summary and Audit Rationale
- Errors are caught at the earliest possible layer, minimizing system resource usage and maximizing clarity for clients.

- Ensures specific, predictable HTTP status codes and messages for each error type, making the API easy to consume and audit.

- Sensitive operations (auth, resource access) are checked after all other layers, 
- preventing information leaks and enforcing business/Data Integrity policies.

- System integrity is protected, and code remains maintainable and transparent for future developers, auditors, and reviewers.

