# TCP-UDP
Description:

In this assignment, you are asked to compare UDP and TCP in terms of speed and robustness. For this purpose, you must implement two client-server programs that use UDP and TCP as transport-layer protocols to transfer data and file.

Code:

You will submit four code files: TCP Client, TCP Server, UDP Client and UDP Server.
Client-side programs should ask for the IP address and port number of the server-side programs. Then, it should give the user to take one of the following actions in an infinite loop: Ping, Timestamp, File, and Exit.
- If the user selects Ping or Timestamp options, it simply sends its request, and prints the incoming information.
- If the user selects File option, it sends its request, retrieves the file's name and contents and saves them in a local file.
- Finally, if the user selects Exit option, client-side program should terminate.
Your server-side programs should wait for a client to connect, and then respond according to incoming requests.
- If a Ping request arrives, server-side program should simply answer with the string "Pong".
- On arrival of Timestamp request, current timestamp value should be sent in the format "HH:mm:ss dd/MM/yyyy", e.g. "03:19:42 29/05/2014".
- If File request arrives, server-side program should send a predetermined file's name and content to the requesting party.
