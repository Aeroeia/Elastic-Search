# 仿电商搜索Demo
一个基于 Elasticsearch+Redis 仿电商搜索Demo，支持中文分词和实时搜索建议。
## 技术栈 

### 后端 
- Spring Boot
- RestHighLevelClient
- SpringCache-Redis
- Mybatis-Plus
- SpringTask
### 前端 
- Vue 3
- TypeScript
- Vite

## 使用说明 

### 后端配置 (Backend Setup)
1. 确保已安装 Java 11+ 和 Maven
2. 启动 Es和Redis 服务

### 前端配置 (Frontend Setup)
1. 进入前端项目目录:
```bash
cd frontend/my-vue-app
```

2. 安装依赖:
```bash
npm install
```

3. 启动开发服务器:
```bash
npm run dev
```

## 环境要求 (Requirements)
- Java 11+
- Elasticsearch 7.x
- Node.js 14+
- Maven 3.x
## 效果演示
![](./es.gif)
