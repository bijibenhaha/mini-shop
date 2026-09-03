# mini-shop 简易微服务电商项目

## 技术栈

- **Spring Boot 3.5** + **Spring Cloud 2025.0** + **Spring Cloud Alibaba 2025.0.0.0**
- **JDK 17**，Maven 多模块
- **Nacos**（注册中心 + 配置中心）
- **Gateway** 网关
- **OpenFeign** 远程调用
- **Sentinel** 熔断限流
- **Seata AT** 分布式事务
- **MySQL 8**，MyBatis-Plus
- **Docker** 部署中间件

## 模块清单

| 模块 | 端口 | 说明 |
|------|------|------|
| mini-common | - | 公共模块：DTO、Result、异常枚举、常量 |
| mini-gateway | 8080 | 网关：路由转发、跨域、限流 |
| mini-user-service | 9001 | 用户服务：用户查询、余额扣减 |
| mini-product-service | 9002 | 商品服务：商品查询、库存扣减 |
| mini-order-service | 9003 | 订单服务：创建订单（核心业务） |

## 快速启动

### 1. 启动中间件（Docker）

```bash
cd mini-shop
docker-compose up -d mysql nacos sentinel seata-server
```

### 2. 初始化数据库

MySQL 容器启动后会自动执行 `sql/` 目录下的 SQL 初始化脚本，创建数据库、表并插入测试数据。

### 3. Nacos 配置中心（可选）

如果使用 Nacos 配置中心管理配置，需要在 Nacos 控制台创建以下配置：

- Data ID: `mini-user-service.yaml`，Group: `DEFAULT_GROUP`
- Data ID: `mini-product-service.yaml`，Group: `DEFAULT_GROUP`
- Data ID: `mini-order-service.yaml`，Group: `DEFAULT_GROUP`
- Data ID: `mini-gateway.yaml`，Group: `DEFAULT_GROUP`

### 4. Seata 配置

在 Nacos 配置中心创建 Data ID `seataServer.properties`，Group `SEATA_GROUP`，内容参考 `seata/seataServer.properties`。

### 5. 启动服务

按顺序启动：

```bash
# 1. 先编译公共模块
mvn clean install -pl mini-common -am

# 2. 启动服务（IDE 或命令行）
# 推荐顺序：user → product → order → gateway
```

### 6. 测试下单

```bash
# 正常下单：库存100，余额100000分(1000元)，购买1个(9999元/个)
curl -X POST http://127.0.0.1:8080/api/order/create \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":1,"count":1}'
```

## 核心业务流程

```
用户 → POST /api/order/create → Gateway(8080) → order-service(9003)
                                                      │
                                          ┌───────────┼───────────┐
                                          ▼           ▼           ▼
                                    扣库存        扣余额       创建订单
                                  (product)     (user)      (本地DB)
                                                      │
                                              Seata @GlobalTransactional
                                                ┌─────┴─────┐
                                                │           │
                                             全部成功    任意失败
                                                │           │
                                             提交事务    Seata回滚
```

## 测试场景

1. **正常下单**：库存100，余额1000元，下单2个 → 库存98，余额余额100000-19998=80002，订单生成成功
2. **库存不足**：库存只剩1，下单2个 → Seata回滚，库存不变、余额不变、订单不生成
3. **余额不足**：余额50元，商品价格9999元 → 扣余额失败，全部回滚
4. **Sentinel 限流**：快速多次调用下单接口，部分请求返回 `request too frequent`
5. **Sentinel 熔断**：关闭 product-service，触发降级，返回 `service busy`
6. **Nacos 配置热更新**：修改 Nacos 配置中心中的配置，观察服务是否热生效

## 数据库表结构

### mini_user_db.t_user
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(100) | 密码 |
| balance | INT | 余额(分) |

### mini_product_db.t_product
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| product_name | VARCHAR(100) | 商品名称 |
| price | INT | 单价(分) |
| stock | INT | 库存 |

### mini_order_db.t_order
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| product_id | BIGINT | 商品ID |
| count | INT | 数量 |
| total_price | INT | 总金额(分) |
| status | TINYINT | 状态：0-待支付，1-已创建，2-已完成 |