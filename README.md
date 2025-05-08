# Distributed Systems Project

## Installation
```
git clone https://github.com/A7med-Alaa/Distributed-Project.git
```
## Setup
First you have to start the server by running
```
java Server
```
The Server will start listening for a client connection.

Second open a new terminal and start a client by running
```
java Client
```
## Database format
**username**:**salary**:**position**
> Note that the database uses a colon as a separator.
## Usage
Client request consists of 3 main methods: `GET`, `PUT` and `DELETE`
> Note that the request uses a space as a separator.

Examples for all available requests:

`GET`:
```
GET [SUBMETHOD] [USERNAME]
```
There are 3 submethods for GET: user, salary, position
```
GET user moaz //Will check if the user mohammed exits in the server database.
```
```
GET salary moaz //Will retrieve moaz salary from the database.
```
```
GET position moaz //Will retrieve moaz position from the database.
```
`PUT` 

Adds a new user to the database.
```
PUT [USERNAME] [SALARY] [POSITION]
```
for example:
```
PUT amr 25000 CEO //Will create a new user in the database with username amr, salary = 25000 and with position = CEO.
```
`DELETE`

Removes a user entry from the database.
```
DELETE omar //Will remove user omar entry from the database.
```
