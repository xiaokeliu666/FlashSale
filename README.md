# Flash Sale
This project using JMeter to do load test which start multiple threads sending request to the server. All the requests target at the same row of data in the database, which simulates the flash sale.
# Problem Solved
## Overselling
Overselling happens when multiple threads(requests from users) visits the same record of stock at the same time. For example: at a certain time, there are 20 users send request to the server and visit the database and get the information that there are 10 items left still, so they are able to make orders. So, there is possiblity that more items sold than expected.

Use optimisitc lock by adding a new version field in the database. Check the value of version before updating the table.
## Rate Limit
If we don't put a limitation on the request rate, sudden increased traffic will also result in pressure on backend.

Token Bucket Algorithm is used to control the rate of request.
## Hide the API
To avoid bad people writing script to send request automatically.

MD5 + Salt
## Limit on the Time
Using the EXPIRE feture of Redis to make item expired after the flash sale

