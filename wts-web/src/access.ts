/**
 * access 插件 — 根据用户角色控制页面访问和菜单可见性
 *
 * SysUser.type:
 *   "3" = 超级管理员 → 完整后台
 *   "1" = 系统用户   → 完整后台
 *   "2" = 普通用户   → 仅考试端
 */
export default function access(initialState: { currentUser?: any }) {
  const userType = initialState?.currentUser?.type;
  const isAdmin = userType === '3' || userType === '1';

  return {
    isAdmin,
    isStudent: !isAdmin,
  };
}
