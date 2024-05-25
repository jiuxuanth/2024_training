import socket
import json

def start_server(host='127.0.0.1', port=65432):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((host, port))
        s.listen()
        print(f'Server started at {host}:{port}')
        
        conn, addr = s.accept()
        with conn:
            print('Connected by', addr)
            while True:
                data = conn.recv(1024)
                if not data:
                    break
                try:
                    json_data = json.loads(data.decode())
                    print('Received JSON data:', json_data)
                except json.JSONDecodeError:
                    print('Received non-JSON data:', data.decode())

if __name__ == "__main__":
    start_server()
    
    