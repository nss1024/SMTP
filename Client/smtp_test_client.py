import socket

HOST = "127.0.0.1"   # localhost
PORT = 8029          # your SMTP server port

def test_smtp_connection():
    with socket.create_connection((HOST, PORT), timeout=5) as sock:
        # receive greeting
        greeting = sock.recv(1024).decode("utf-8", errors="replace")
        print("Server greeting:", greeting)

        # optionally send a basic HELO to see if server stays connected
        sock.sendall(b"HELO client.example.com\r\n")
        response = sock.recv(1024).decode("utf-8", errors="replace")
        print("Response to HELO:", response)

if __name__ == "__main__":
    test_smtp_connection()
