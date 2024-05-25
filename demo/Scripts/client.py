import socket
import json

def send_json_to_server(json_data, host='127.0.0.1', port=65432):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((host, port))
        s.sendall(json.dumps(json_data).encode())

if __name__ == "__main__":
    json_data = {
        "name": "Alice",
        "age": 30,
        "city": "Wonderland"
    }
    send_json_to_server(json_data)