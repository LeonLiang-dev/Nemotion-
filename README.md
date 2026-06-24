# Leon在线考试系统

基于 Spring Boot 3 + React 18 + MyBatis-Plus 的在线考试管理系统。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.3.6 + JDK 17 |
| 持久层 | MyBatis-Plus 3.5.7 + MySQL 8.0 |
| 安全认证 | Spring Security 6 + JWT (jjwt 0.12.6) |
| 前端框架 | React 18 + TypeScript + UmiJS 4 |
| UI组件 | Ant Design 5 + Ant Design Pro Components |
| Excel处理 | Apache POI 5.2.5 |

## 项目结构

```
nemotion/
├── wts-server/           # Spring Boot 后端
│   ├── wts-app/          # 启动模块（含 application.yml）
│   ├── wts-auth/         # 认证授权模块
│   ├── wts-common/       # 公共模块（JWT、CORS、分页等）
│   ├── wts-exam/         # 考试业务模块（核心）
│   └── wts-system/       # 系统管理模块
├── wts-web/              # React 前端
│   ├── src/pages/        # 页面组件
│   ├── src/services/     # API 服务
│   └── .umirc.ts         # UmiJS 配置
├── sql/                  # 数据库脚本
│   ├── init/             # 初始化SQL
│   └── migrations/       # 增量迁移
├── templates/            # Excel导入模板
└── docs/                 # 项目文档
```

## 快速启动

### 环境要求

- JDK 17+ (推荐 Eclipse Temurin)
- Maven 3.8+
- Node.js 18+ / npm 9+
- MySQL 8.0

### 1. 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE wts DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 导入初始化SQL
mysql -u root -p wts < sql/init/wts.v1.4.1.sql

# 执行增量迁移
mysql -u root -p wts < sql/migrations/V2_add_point_column.sql
```

### 2. 配置数据库连接

编辑 `wts-server/wts-app/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/wts?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: 12345678
```

### 3. 启动后端

```bash
cd wts-server
mvn clean package -DskipTests
java -jar wts-app/target/wts-app-2.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

后端默认运行在 `http://localhost:8080`

### 4. 启动前端

```bash
cd wts-web
npm install --registry=https://registry.npmmirror.com
npm run dev
```

前端默认运行在 `http://localhost:8000`，通过代理转发 `/api` 请求到后端。

### 5. 访问系统

- 前端地址: `http://localhost:8000`
- 管理员登录: sysadmin / 12345678
- 学生账号: 由管理员在用户管理中创建或批量导入

## 一键构建

```bash
chmod +x build.sh
./build.sh
```

构建完成后会在 `dist/` 目录生成可部署的 Spring Boot JAR（内嵌前端静态资源）。

## 功能模块

### 管理端
- **题目管理** — 题目分类、题目CRUD、Excel批量导入导出、6种题型支持
- **试卷管理** — 试卷创建、批量添加题目、自定义分值
- **答题室管理** — 创建答题室、关联试卷、发布/关闭、设置时间
- **答卷管理** — 查看答卷列表、人工阅卷（问答题/附件题）
- **随机组卷** — 按规则随机生成试卷
- **系统管理** — 用户管理、组织机构
- **数据面板** — 首页统计（题目数、试卷数、答题室、答卷、用户）

### 学生端
- **我的考试** — 查看可参加的答题室、进入考试
- **在线答题** — 计时器、自动暂存、到时自动提交
- **查看成绩** — 查看答卷结果、得分明细

## 数据库迁移

| 版本 | 文件 | 说明 |
|------|------|------|
| v1.0 | `sql/init/wts.v1.4.1.sql` | 完整初始化 |
| v2.0 | `sql/migrations/V2_add_point_column.sql` | 题目默认分值字段 |

## 文档

- [技术架构](docs/architecture.md)
- [重构变更清单](docs/migration-changes.md)
- [数据库说明](docs/database.md)
- [部署指南](docs/deployment.md)
- [Windows 教师机安装包](docs/windows-teacher-installer.md)

## License

本项目基于原 WTS 开源考试系统进行重构升级。
