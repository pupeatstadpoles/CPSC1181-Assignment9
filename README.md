# CPSC1181-Assignment9
Multithreaded Server-Client Hotel Reservation - Completed Mar 2023

Coded a hotel reservation server-client program that utilizes multithreading to allow for simultaneous handling of multiple clients. Server continues until manual shutdown by handling exceptions in the service class. 


When the server is started, it creates a ServerSocket and waits for any clients to connect. A socket is created with each connection accepted from a client and a service is instantiated using that socket. That service is then placed into a thread ensuring that each client can perform bookings and cancellations independent of any other clients. 


Client program contains a simple menu to facilitate processing of commands input by the user. The client program runs as long as the user has not entered the QUIT command or an invalid command. 


The service class adds or removes the client's `username` to/from an ArrayList in the Hotel object. It is also the one to call the Hotel's different methods to check for availability of rooms and close the socket for that client. 



Some tricky points:

- I had to make sure that my `doService()` method was not called in the service's constructor and instead place it in the `run()` method. 
- The `run()` method needed a nested `try{} catch{}` inside another `try{}` because of the socket closing I included in the inner `finally{}` block. The IOException that might result is then handled in the outer `catch{}`.
- Remembering to flush the buffer.
