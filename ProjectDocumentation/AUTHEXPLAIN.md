# JWT Authentication Explained in Simple English
> JWT (JSON Web Token) Authentication is a way for your application to know which user is making requests, without keeping track of sessions on the server.

## How Does It Work?
1. User Logs In: When a user enters their username and password and hits login, your server checks those details.

2. Token is Created: If the credentials are correct, the server creates a JWT — a special string containing info (like user ID and role). The server signs this token to make sure it can't be changed by anyone else.

3. Token is Sent Back: The server gives this JWT to the user, usually as part of the login response.

4. User Stores Token: The user's browser or app stores this JWT (often in local storage).

5. Sending Requests: From now on, whenever the user wants to fetch something private (like an account page), the browser/app sends this JWT with each request, typically in an Authorization: Bearer <token> header.

6. Server Checks Token: The server checks that the JWT is still valid, hasn't expired, and matches its secret signature. If everything checks out, the server lets the user access the resource.

## What’s in a JWT?
- Header: Says what type of token it is (usually 'JWT') and what algorithm is used to sign it.

- Payload: Contains "claims" — info like user ID, role, and token expiration time. Anyone can read this part, so don't put secret stuff here.

- Signature: Created using the header, payload, and a secret key. This ensures the data can't be changed.

> A JWT looks like this:
> 
> xxxxx.yyyyy.zzzzz (header, payload, signature)

## Why Use JWTs?
- Stateless: The server doesn't need to remember anything about who's logged in. The token itself carries all the necessary info.

- Scalable: Good for APIs — easy to use with mobile apps, web frontends, and more.

## What About Logging Out?
- Without sessions on the server, logging out is usually just deleting the token from the browser.

- The server can't "cancel" the JWT before it expires unless you add extra code to keep track of blacklisted tokens or shorten the expiration time.

## Security Tips
- Don't put sensitive info in the JWT payload.

- The token expires (in your case, after 24 hours), which limits risk if it gets stolen.

- Always use HTTPS so tokens aren't snatched during transfer.

## Testing With Postman

> When using JWT authentication in your project and testing with Postman, if a user logs in twice and gets two different tokens, both tokens work independently—they are both valid for 24 hours unless you have some server-side token tracking or blacklisting in place.

> Each login action generates a new JWT, and if you use one token in one request and another token in a different request (by pasting them into the Authorization header), both will be accepted by your server until they expire.

> Your server does not keep a record or link between these tokens. It only validates whichever token was sent with the current request.

> If one token is deleted from your Postman workspace and another is used, the server cannot “know” which one the user has “logged out” from. Both tokens will keep working until the set expiration time.

> So, if one user is logged in on two different endpoints with two tokens, both tokens remain usable and recognized by the server, and there is no conflict or way for your API to know which is “active.” They are both valid access tokens that work until they naturally expire.

## Is Logging Out Necessary?
Since token/session management (“which token is active, which is inactive”) is not a requirement for this project, there is no need for you to add a logout endpoint that tracks or invalidates specific JWT tokens.

### Your current implementation correctly matches all required objectives and all audit questions:

- Users can log in and receive a token. The server does not need to track which tokens exist — it simply checks the token whenever the user makes an authenticated request.

- If users log in multiple times and get multiple tokens, all tokens remain valid until expiration, which is expected and accepted for stateless JWT in this kind of project.

- Auditors will focus only on whether authentication and role-based access work as intended for each request, not whether a logout endpoint exists.

- The bonus section suggests extra security measures like CORS and rate limiting, but not logout or token invalidation.

### Summary:
You do not need to add a logout endpoint or manage active/inactive tokens for audit or project correctness. The stateless JWT approach you’ve built meets all authentication requirements. The only time you would need that complexity is if the requirements or bonus specifically demanded session management, single sign-out, or session visibility, which they do not.