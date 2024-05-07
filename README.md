# Multithreaded Key-Value Store Client-Server Application

This project implements a multithreaded client-server architecture for a key-value store. The server can handle multiple client connections concurrently using threads. Clients can send commands to the server to perform operations on the key-value store.

## Features
- **Client**: Connects to the server and sends commands to perform operations on the key-value store.
- **Server**: Listens for incoming connections from clients and handles requests concurrently using threads.
- **Key-Value Store**: Stores key-value pairs and supports various operations like SET, GET, DELETE, etc.

## Usage
1. **Server**: Start the server by running the `Server` class with the desired port number as a command-line argument.

   Example: `java Server 5000`

2. **Client**: Run the `Client` class with the server's host address and port number as command-line arguments.

   Example: `java Client localhost 5000`

3. **Commands**: Use the following commands to interact with the server:

   - `SET key value`: Sets the value of the specified key.
   - `GET key`: Retrieves the value of the specified key.
   - `DELETE key`: Deletes the specified key-value pair.
   - `INCREMENT key`: Increments the value of the specified key if it's a number.
   - `DECREMENT key`: Decrements the value of the specified key if it's a number.
   - `LPUSH key value`: Pushes a value to the beginning of a list stored at the specified key.
   - `RPUSH key value`: Pushes a value to the end of a list stored at the specified key.
   - `LLEN key`: Gets the length of the list stored at the specified key.
   - `SAVE filename`: Saves the key-value store to a file.
   - `LOAD filename`: Loads the key-value store from a file.

## Requirements
- Java 8 or higher
