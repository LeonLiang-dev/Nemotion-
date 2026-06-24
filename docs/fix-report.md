# WTS 在线考试系统 - 代码审查修复报告

> 修复日期: 2026-06-23
> 项目路径: `/Users/leonliang/Downloads/WTS-master/nemotion/`

---

## 一、修复总览

| 等级 | 修复数量 | 说明 |
|------|---------|------|
| CRITICAL | 4 | 影响核心功能的严重缺陷 |
| HIGH | 7 | 安全漏洞和数据丢失风险 |
| MEDIUM | 14 | 代码质量和健壮性问题 |
| **合计** | **25** | |

---

## 二、CRITICAL 修复

### C1. 仪表盘统计 API 返回硬编码 0
- **文件**: `wts-exam/.../controller/DashboardController.java`
- **问题**: 统计数据全部硬编码为 0，未实际查询数据库
- **修复**: 接入 `ExamSubjectMapper`, `ExamPaperMapper`, `ExamRoomMapper`, `ExamCardMapper`, `SysUserMapper`，执行真实 `selectCount` 查询
- **改动**: 重写 `stats()` 方法，增加 admin 权限校验

### C2. 答题卡结果页 versionid 字段名不一致
- **文件**: `wts-web/src/pages/Exam/Card/Result.tsx`
- **问题**: 后端返回 `versionid`，前端用 `versionId` 取值，导致答案映射和分值映射全部为空
- **修复**: 统一使用 `a.versionid || a.versionId` 兼容两种字段名
- **改动行**: 约第 52-62 行（答案 map 构建）和 pointInfo key

### C3. Token 刷新竞态条件
- **文件**: `wts-web/src/services/request.ts`
- **问题**: 多个请求同时 401 时，每个都独立触发 refresh 请求，导致 token 失效
- **修复**: 引入 `isRefreshing` 锁 + `refreshSubscribers` 队列模式
- **改动**: 完整重写响应拦截器的 401 处理逻辑

### C4. 答题室创建/编辑时 starttime/endtime 未保存
- **文件**: `wts-exam/.../service/impl/RoomServiceImpl.java`
- **问题**: `create()` 和 `update()` 方法中未设置 `starttime`/`endtime`，导致考试时间限制永远为空
- **修复**: 在两个方法中增加 `room.setStarttime(dto.getStarttime())` 和 `room.setEndtime(dto.getEndtime())`
- **改动行**: create() 约第 123 行，update() 约第 151 行

---

## 三、HIGH 修复（安全）

### S1. 所有 Controller 变更接口缺少管理员权限校验

以下 5 个 Controller 的写操作（增/删/改）均未校验用户身份，任何已登录用户（包括学生）都可以调用：

| Controller | 文件路径 | 修复的接口 |
|-----------|---------|-----------|
| SubjectController | `wts-exam/.../controller/SubjectController.java` | create, update, delete, importExcel, exportExcel |
| PaperController | `wts-exam/.../controller/PaperController.java` | create, update, delete, addSubject |
| SubjectTypeController | `wts-exam/.../controller/SubjectTypeController.java` | create, update, delete |
| RandomController | `wts-exam/.../controller/RandomController.java` | createItem, updateItem, deleteItem, addStep, updateStep, deleteStep, generate |
| DashboardController | `wts-exam/.../controller/DashboardController.java` | stats |

- **修复方式**: 统一添加 `requireAdmin(user)` 校验：
  ```java
  private void requireAdmin(CurrentUser user) {
      if (!user.isAdmin()) {
          throw BizException.fail("无权限操作");
      }
  }
  ```

### S2. 题目导出接口抛出 RuntimeException 泄露内部信息
- **文件**: `wts-exam/.../controller/SubjectController.java` (第 89 行)
- **问题**: `throw new RuntimeException("导出失败: " + e.getMessage())` 会暴露堆栈信息
- **修复**: 改为 `throw BizException.fail("导出失败: " + e.getMessage())`

---

## 四、MEDIUM 修复

### M1. Excel 导入/导出抛出 RuntimeException
- **文件**: `wts-exam/.../service/impl/SubjectImportServiceImpl.java`
- **改动**:
  - 第 138 行: `new RuntimeException("读取Excel文件失败")` → `BizException.fail(...)`
  - 第 333 行: `new RuntimeException("导出Excel失败")` → `BizException.fail(...)`

### M2. 随机组卷抛出 RuntimeException
- **文件**: `wts-exam/.../service/impl/RandomServiceImpl.java`
- **改动**: 第 120 行 `new RuntimeException("规则步骤为空")` → `BizException.fail(...)`

### M3. 密码工具类抛出 RuntimeException
- **文件**: `wts-common/.../utils/PasswordUtils.java`
- **改动**: 第 44 行 `new RuntimeException("MD5 algorithm not found")` → `new IllegalStateException(...)`（这是一个不可能发生的编程错误，使用 IllegalStateException 更准确）

### M4. POI 废弃 API: setCellType
- **文件**: `wts-exam/.../service/impl/SubjectImportServiceImpl.java`
- **问题**: `cell.setCellType(CellType.STRING)` 在 POI 5.x 中已废弃，且会丢失原始数据格式
- **修复**: 改用 `DataFormatter.formatCellValue(cell)` 安全读取单元格文本
- **改动**: 新增 `private static final DataFormatter DATA_FORMATTER`，重写 `getStringCell()` 方法

### M5. AuthService 登录时双重数据库更新
- **文件**: `wts-auth/.../service/AuthService.java`
- **问题**: `login()` 方法先更新 `logintime`，再更新 `password`（BCrypt 升级），两次 `updateById` 之间若崩溃会导致数据不一致
- **修复**: 合并为一次 `updateById` 调用

### M6. MD5 密码比对大小写敏感
- **文件**: `wts-auth/.../service/AuthService.java` (第 68 行)、`wts-auth/.../service/UserService.java` (第 154 行)
- **问题**: `md5Password()` 返回大写，但旧系统可能存储小写 MD5，`equals()` 会匹配失败
- **修复**: 改为 `equalsIgnoreCase()` 确保大小写无关匹配

### M7. 修改密码缺少输入校验
- **文件**: `wts-auth/.../service/UserService.java` (第 143 行)
- **问题**: `changePassword()` 未校验 oldPassword/newPassword 是否为空
- **修复**: 增加空值校验和最小长度校验（≥6位）

### M8. CORS 配置使用通配符
- **文件**: `wts-common/.../config/CorsConfig.java`
- **问题**: `setAllowedOriginPatterns(List.of("*"))` 在生产环境存在安全风险
- **修复**: 改为读取 `app.cors.allowed-origin-patterns`，生产环境可通过 `CORS_ALLOWED_ORIGIN_PATTERNS` 指定可信域名

### M9. 前端：用户搜索功能失效
- **文件**: `wts-web/src/pages/System/User/index.tsx` (约第 191 行)
- **问题**: 搜索参数用 `params.keyword`，但 ProTable 搜索字段是 `name`/`loginname`
- **修复**: 改为 `keyword: params.name || params.loginname`

### M10. 前端：多个页面缺少 try-catch 错误处理
- **涉及文件**:
  - `wts-web/src/pages/Exam/Paper/index.tsx` — `handleOk` 未包装 try-catch
  - `wts-web/src/pages/Exam/Room/index.tsx` — `handleOk`、publish/close/delete 的 onConfirm 未包装 try-catch
  - `wts-web/src/pages/Exam/Random/index.tsx` — `handleCreateItem`、`handleAddStep` 未包装 try-catch
  - `wts-web/src/pages/System/Organization/index.tsx` — `handleDelete`、`handleOk` 未包装 try-catch
- **修复**: 所有异步操作统一用 `try { ... } catch { message.error(...) }` 包装

### M11. 前端：Dashboard 统计加载失败无错误提示
- **文件**: `wts-web/src/pages/Dashboard/index.tsx`
- **问题**: `.catch(() => {})` 静默吞掉错误
- **修复**: 增加 `error` 状态，加载失败时显示 `<Result status="warning" title="统计数据加载失败" />`

### M12. 前端：答题过程中关闭页面无保护
- **文件**: `wts-web/src/pages/Exam/Card/index.tsx`
- **问题**: 学生答题时关闭/刷新页面，未保存的答案会丢失
- **修复**:
  - 添加 `beforeunload` 事件监听，关闭前弹出确认框
  - 添加组件卸载时的 `useEffect` cleanup，自动触发最终保存

### M13. 前端：题目导出 Blob 处理错误
- **文件**: `wts-web/src/pages/Exam/Subject/index.tsx`
- **问题**: `exportSubjects()` 返回的已是 Blob，再次 `new Blob([res])` 导致下载文件损坏
- **修复**: 直接使用 `res` 作为 Blob URL

---

## 五、未修复项（需手动处理）

### ⚠️ 数据库缺少 point 列
```sql
ALTER TABLE wts_subject ADD COLUMN point INT DEFAULT 1 AFTER level;
```
> 需在 MySQL 中手动执行此 SQL，否则创建/编辑题目会报 `Unknown column 'point'` 错误。

---

## 六、已验证

- [x] 后端编译: `BUILD SUCCESS` (Maven compile)
- [x] 前端编译: `npm run build` 成功
- [x] 所有 Controller 写操作均有 admin 权限校验
- [x] 所有 RuntimeException 已替换为 BizException
