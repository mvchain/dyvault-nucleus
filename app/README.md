#service-app

app端的代理入口.

服务采用rpc方式调用后端。若触发熔断降级为缓存读取,缓存不可用则根据规则返回托底数据