# 技术架构

## 系统架构图

```
┌─────────────────────────────────────────────────┐
│                  浏览器 (Chrome)                  │
│          React 18 + Ant Design 5 + UmiJS 4       │
└───────────────────────┬─────────────────────────┘
                        │ HTTP / API Proxy
┌───────────────────────▼─────────────────────────┐
│              Spring Boot 3.3.6 (JDK 17)          │
│  ┌──────────┐ ┌──────────┐ ┌──────────────────┐ │
│  │ wts-auth │ │wts-exam  │ │   wts-system     │ │
│  │ 认证授权  │ │考试业务   │ │   系统管理        │ │
│  └────┬─────┘ └────┬─────┘ └────┬─────────────┘ │
│       │            │            │                │
│  ┌────▼────────────▼────────────▼─────────────┐  │
│  │              wts-common 公共层               │  │
│  │   JWT工具 · CORS · 分页 · 异常处理           │  │
│  └────────────────┬───────────────────────────┘  │
│                   │                              │
│  ┌────────────────▼───────────────────────────┐  │
│  │         MyBatis-Plus 3.5.7                  │  │
│  └────────────────┬───────────────────────────┘  │
└───────────────────┼─────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────┐
│               MySQL 8.0 (wts)                    │
└──────────────────────────────────────────────────┘
```

## 后端模块划分

### wts-app（启动模块）
- 主启动类 `WtsApplication`
- `application.yml` / `application-dev.yml` / `application-prod.yml`
- 内嵌前端静态资源（构建后）

### wts-auth（认证授权模块）
- JWT 过滤器与认证入口
- Spring Security 配置
- 用户登录/注册 API
- 组织机构管理

### wts-common（公共模块）
- `R<T>` 统一响应封装
- `PageResult<T>` 分页响应
- `JwtUtils` JWT 工具类
- `BizException` 业务异常
- CORS 跨域配置
- MyBatis-Plus 公共配置

### wts-exam（考试业务模块）
**核心实体：**
| 实体 | 表名 | 说明 |
|------|------|------|
| ExamSubject | wts_subject | 题目 |
| ExamSubjectVersion | wts_subject_version | 题目版本（每次编辑生成新版本） |
| ExamSubjectAnswer | wts_subject_answer | 题目答案选项 |
| ExamSubjectType | wts_subject_type | 题目分类（树形） |
| ExamPaper | wts_paper | 试卷 |
| ExamPaperChapter | wts_paper_chapter | 试卷章节 |
| ExamPaperSubject | wts_paper_subject | 试卷题目关联 |
| ExamRoom | wts_room | 答题室 |
| ExamRoomPaper | wts_room_paper | 答题室试卷关联 |
| ExamCard | wts_card | 答卷 |
| ExamCardAnswer | wts_card_answer | 答卷答案 |
| ExamCardPoint | wts_card_point | 答卷得分 |

**核心流程：**
1. 创建题目 → 创建试卷 → 创建答题室并关联试卷
2. 学生进入答题室 → 系统分配试卷 → 创建答卷
3. 学生答题 → 暂存/提交 → 自动评分（客观题）
4. 教师阅卷（主观题） → 出成绩

**题目版本机制：**
- 每次编辑题目会生成新版本（ExamSubjectVersion）
- 答案绑定到版本（ExamSubjectAnswer.versionid）
- 答卷引用具体版本，保证已答试卷不受题目修改影响

### wts-system（系统管理模块）
- 用户管理 CRUD
- 组织机构树形管理

## 前端架构

### 路由结构
```
/login                    — 登录页（无布局）
/dashboard                — 首页仪表盘（管理员）
/my-exams                 — 我的考试（学生）
/exam/subject-type        — 题目分类管理
/exam/subject             — 题目管理
/exam/paper               — 试卷管理
/exam/room                — 答题室管理
/exam/room/:id/cards      — 答卷列表
/exam/random              — 随机组卷
/system/user              — 用户管理
/system/organization      — 组织机构
/exam/card/:id            — 答题页（无布局）
/exam/card/:id/result     — 成绩页（无布局）
/exam/card/:id/judge      — 阅卷页（无布局）
```

### 权限控制
- UmiJS `access` 插件 + `getInitialState`
- 管理员（type=1/3）→ `/dashboard`
- 学生（type=2）→ `/my-exams`
- `window.location.href` 全页面跳转确保状态正确加载

### API 拦截器
- 响应拦截器检查 `code !== 200` 时 reject
- Blob 类型响应（Excel导出）自动跳过检查

## 安全设计

- JWT 无状态认证，Token 通过 localStorage 存储
- `X-User-Id` / `X-User-Name` 请求头由网关注入
- Spring Security 配置白名单：登录、健康检查、答题室API
- 答卷操作校验所有权（只能操作自己的答卷）
- 答题室状态校验（未开放/已关闭时禁止进入）
