import redis

# 连接到本地 Redis 服务器
redis_host = 'localhost'
redis_port = 6379
redis_db = 0
redis_password = 123456

# 创建 Redis 连接对象
r = redis.StrictRedis(host=redis_host, port=redis_port, db=redis_db, password=redis_password)

# 设置键值对
r.set('key1', 'Hello, Redis!')

# 获取键值对
value = r.get('key1')
print(value.decode('utf-8'))  # 解码字节流为字符串

# 列出所有键
keys = r.keys()
print(keys)

# 删除键
r.delete('key1')

# 判断键是否存在
key_exists = r.exists('key1')
print(key_exists)