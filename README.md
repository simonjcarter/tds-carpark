# TDS Car Park Application

TDS assessment application

## Running the application
### Pre-requisites

### How to run

### How to run tests


## Design 

### Assumptions
- Parking charge  is per minute or part thereof, so parking for one second will incur a charge of a minute
- Additional charge is not for partial periods, so four minutes would not incur an additional charge.  However, four minutes and one secound would as it will be rounded up to five minutes
- The vehicles are **not** linked to a space
    - conflicting specification in `POST /parking` between description and response body

### Known limitations
- No CI/CD

### Future work

### Specification

#### Project Overview
- Your task is to create a simple car park management API. Please read this document
  fully and carefully to ensure you don’t miss any requirements.
- You should create and structure the project in the way you feel most appropriate.
- You have the freedom to come up with your own solution entirely, our only ask is that it
  must cater for the requirements and define the endpoints you will find in the table below.
- Whilst this is a take home task, and we hope you use it as an opportunity to showcase
  your abilities, we also understand that your time is valuable. Please don’t feel compelled
  to over engineer the solution or spend many hours on it, we suggest 2-3 hours.
#### Requirement
  Your API should be able to handle the following scenarios:
- Allocating vehicles when there is an available space
- Determine the number of available and full spaces
- Determine the parking charge on vehicle exit
- De-Allocate a space on vehicle exit
- Vehicles will be charged per minute they are parked
- The parking charges are:
- Small Car - £0.10/minute (1)
- Medium Car - £0.20/minute (2)
- Large Car £0.40/minute (3)
- Every 5 minutes an additional charge of £1 will be added
- The Vehicle Type argument should be either 1, 2 or 3
#### Technical guidance
- Parking data should be stored in memory and NOT be stored in persistent store (for
  example a database).
- For your initial solution, you may assume that only one client is sending an HTTP
  request at a time.
- If you are able to do so, you should consider the case where there are multiple clients
  sending HTTP request at the same time.
#### Nonfunctional Requirements
- Implement basic error handling
- Include tests at the levels that give you confidence
#### Non-Technical Requirements
  You should provide a README with your solution it should:
- Include information about how to setup and run your solution locally
- Detail any assumptions you made
- You could include any questions you would’ve asked had you needed to
#### API description
  The following are the API endpoints to be defined:
1. `GET /parking`
- Description: Gets available and occupied number of spaces
- Query Params: None
- Request Body: None
- Response:
```json
{
"availableSpaces": int,
"occupiedSpaces": int
}
```
2. `POST /parking`
- Description: Parks a given vehicle in the first available space and returns the vehicle and
  its space number
- Query Params: None
- Request Body:
```json
{
"vehicleReg": string,
"vehicleType": int
}
```
- Response:
```json
{
"vehicleReg": string,
"timeIn": date-time
}
```
3. `POST /parking/bill`
- Description: Frees up this vehicles space and return its final charge from its parking time
  until now
- Query Params: None
- Request Body:
```json
{
"vehicleReg": string
}
```
- Response:
```json
{
"vehicleReg": string,
"vehicleCharge": double,
"timeIn": date-time,
"timeOut": date-time
}
```