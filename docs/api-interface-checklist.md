# Leon 在线考试系统接口清单与回归测试

本文档列出当前后端 Controller 暴露的 78 个接口入口，并说明自动化测试覆盖方式。

## 测试方式

- 自动化入口测试：`wts-server/wts-app/src/test/java/com/wts/exam/controller/AllApiControllerSmokeTest.java`
- 覆盖目标：所有 Controller 对外方法可调用、登录用户读取正常、权限入口能进入、请求参数能正确转交给 service、统一响应码为 `200`
- 执行命令，在 `wts-server` 目录下运行：

```bash
mvn -pl wts-app -am test
```

说明：该测试使用 mock service/mapper，不连接真实 MariaDB，也不会污染本机安装后的业务数据。

## 接口清单

### 健康检查

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/health` | 服务健康检查 | 是 |

### 认证与当前用户

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| POST | `/api/v1/auth/login` | 登录 | 是 |
| POST | `/api/v1/auth/refresh` | 刷新 token | 是 |
| POST | `/api/v1/auth/logout` | 退出登录 | 是 |
| GET | `/api/v1/auth/me` | 当前用户信息 | 是 |
| GET | `/api/v1/auth/menus` | 当前用户菜单 | 是 |

### 组织管理

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/organizations/tree` | 组织树 | 是 |
| POST | `/api/v1/organizations` | 新增组织 | 是 |
| PUT | `/api/v1/organizations/{id}` | 修改组织 | 是 |
| DELETE | `/api/v1/organizations/{id}` | 删除组织 | 是 |

### 用户管理

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/users` | 用户分页列表 | 是 |
| POST | `/api/v1/users` | 新增用户 | 是 |
| POST | `/api/v1/users/import-students` | 导入学生帐号 | 是 |
| PUT | `/api/v1/users/{id}` | 修改用户 | 是 |
| DELETE | `/api/v1/users/{id}` | 禁用用户 | 是 |
| POST | `/api/v1/users/batch-disable` | 批量禁用用户 | 是 |
| DELETE | `/api/v1/users/{id}/hard-delete` | 永久删除用户 | 是 |
| POST | `/api/v1/users/batch-hard-delete` | 批量永久删除用户 | 是 |
| POST | `/api/v1/users/{id}/reset-password` | 重置密码 | 是 |
| POST | `/api/v1/users/change-password` | 修改当前用户密码 | 是 |

### 题目分类

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/subject-types/tree` | 题目分类树 | 是 |
| POST | `/api/v1/subject-types` | 新增题目分类 | 是 |
| PUT | `/api/v1/subject-types/{id}` | 修改题目分类 | 是 |
| DELETE | `/api/v1/subject-types/{id}` | 删除题目分类 | 是 |
| POST | `/api/v1/subject-types/batch-delete` | 批量删除题目分类 | 是 |

### 题目管理

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/subjects` | 题目分页列表 | 是 |
| GET | `/api/v1/subjects/{id}` | 题目详情 | 是 |
| POST | `/api/v1/subjects` | 新增题目 | 是 |
| PUT | `/api/v1/subjects/{id}` | 修改题目 | 是 |
| DELETE | `/api/v1/subjects/{id}` | 删除题目 | 是 |
| POST | `/api/v1/subjects/batch-delete` | 批量删除题目 | 是 |
| POST | `/api/v1/subjects/import` | 导入题目模板 | 是 |
| GET | `/api/v1/subjects/export` | 导出题目 | 是 |

### 试卷管理

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/papers` | 试卷分页列表 | 是 |
| GET | `/api/v1/papers/{id}` | 试卷详情 | 是 |
| POST | `/api/v1/papers` | 新增试卷 | 是 |
| PUT | `/api/v1/papers/{id}` | 修改试卷 | 是 |
| POST | `/api/v1/papers/{id}/subjects` | 给试卷添加题目 | 是 |
| GET | `/api/v1/papers/{id}/chapters` | 试卷章节列表 | 是 |
| GET | `/api/v1/papers/{id}/paper-subjects` | 试卷题目列表 | 是 |
| DELETE | `/api/v1/papers/{id}` | 删除试卷 | 是 |
| POST | `/api/v1/papers/batch-delete` | 批量删除试卷 | 是 |

### 答题室管理

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/rooms` | 答题室分页列表 | 是 |
| GET | `/api/v1/rooms/my` | 当前学生可参加的答题室 | 是 |
| GET | `/api/v1/rooms/{id}` | 答题室详情 | 是 |
| POST | `/api/v1/rooms` | 新增答题室 | 是 |
| PUT | `/api/v1/rooms/{id}` | 修改答题室 | 是 |
| POST | `/api/v1/rooms/{id}/publish` | 发布答题室 | 是 |
| POST | `/api/v1/rooms/batch-publish` | 批量发布答题室 | 是 |
| POST | `/api/v1/rooms/{id}/close` | 关闭答题室 | 是 |
| POST | `/api/v1/rooms/batch-close` | 批量关闭答题室 | 是 |
| POST | `/api/v1/rooms/{id}/papers` | 答题室绑定试卷 | 是 |
| GET | `/api/v1/rooms/{id}/papers` | 答题室试卷列表 | 是 |
| GET | `/api/v1/rooms/{id}/users` | 答题室用户列表 | 是 |
| POST | `/api/v1/rooms/{id}/users` | 分配答题室用户 | 是 |
| DELETE | `/api/v1/rooms/{id}` | 删除答题室 | 是 |
| POST | `/api/v1/rooms/batch-delete` | 批量删除答题室 | 是 |

### 答卷与批改

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| POST | `/api/v1/cards/enter` | 学生进入答题室并生成答卷 | 是 |
| GET | `/api/v1/cards/{id}` | 答卷详情/结果 | 是 |
| POST | `/api/v1/cards/{id}/save` | 保存答题进度 | 是 |
| POST | `/api/v1/cards/{id}/submit` | 提交答卷 | 是 |
| POST | `/api/v1/cards/{id}/judge` | 教师批改答卷 | 是 |
| POST | `/api/v1/cards/batch-judge` | 批量阅卷 | 是 |
| GET | `/api/v1/cards/{id}/paper` | 学生答题用试卷内容 | 是 |
| GET | `/api/v1/cards/{id}/paper-review` | 教师批改用试卷内容 | 是 |
| GET | `/api/v1/cards/rooms/{roomId}/cards` | 答题室答卷列表 | 是 |

### 随机组卷

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/random-items` | 随机规则列表 | 是 |
| POST | `/api/v1/random-items` | 新增随机规则 | 是 |
| PUT | `/api/v1/random-items/{id}` | 修改随机规则 | 是 |
| DELETE | `/api/v1/random-items/{id}` | 删除随机规则 | 是 |
| POST | `/api/v1/random-items/batch-delete` | 批量删除随机规则 | 是 |
| GET | `/api/v1/random-items/{itemId}/steps` | 随机规则步骤列表 | 是 |
| POST | `/api/v1/random-items/{itemId}/steps` | 新增随机规则步骤 | 是 |
| PUT | `/api/v1/random-steps/{id}` | 修改随机规则步骤 | 是 |
| DELETE | `/api/v1/random-steps/{id}` | 删除随机规则步骤 | 是 |
| POST | `/api/v1/random-steps/batch-delete` | 批量删除随机规则步骤 | 是 |
| POST | `/api/v1/random-items/{itemId}/generate` | 按随机规则生成试卷 | 是 |

### 首页统计

| 方法 | 路径 | 用途 | 自动化覆盖 |
| --- | --- | --- | --- |
| GET | `/api/v1/dashboard/stats` | 首页统计数据 | 是 |
