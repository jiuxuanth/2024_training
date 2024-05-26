import socket
import json
import time
import psutil

def get_system_metrics():
    timestamp = int(time.time())
    cpu_percent = psutil.cpu_percent(interval=1)
    mem_percent = psutil.virtual_memory().percent
    metrics = [
        {
            "metric": "cpu.used.percent",
            "endpoint": "my-computer",
            "timestamp": timestamp,
            "step": 60,
            "value": cpu_percent
        },
        {
            "metric": "mem.used.percent",
            "endpoint": "my-computer",
            "timestamp": timestamp,
            "step": 60,
            "value": mem_percent
        }
    ]
    return metrics

def send_json_to_server(json_data, host='127.0.0.1', port=65432):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((host, port))
        s.sendall(json.dumps(json_data).encode())

if __name__ == "__main__":
    metrics = get_system_metrics()
    send_json_to_server(metrics)