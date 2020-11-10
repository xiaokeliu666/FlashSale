# Flash Sale
This project using JMeter to do load test which start multiple threads sending request to the server. All the requests target at the same row of data in the database, which simulates the flash sale.
# Problem Solved
## Overselling
Use optimisitc lock by adding a new version field in the database. Check the value of version before updating the table.
## Rate Limit
Token Bucket Algorithm is used to control the rate of request.
## Hide the API
MD5 + Salt
## Limit on the Time
Using the EXPIRE feture of Redis to make item expired after the flash sale

