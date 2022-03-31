/*
 * @Description:
 * @Date: 2021-06-17 10:17:44
 * @LastEditTime: 2022-03-31 15:20:33
 */
// 跨域代理前缀
// const API_PROXY_PREFIX='/api'
const locationUrl = `${location.protocol}//${location.hostname}`

let BASE = `${locationUrl}:10200/api`

if (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === undefined) {
  BASE = 'http://192.168.10.160:10200/api'
}
module.exports = {
  BASE,
  // 知识库模块
  KNOWLEDGE: `${BASE}/vulcan-knowledgebase-management-provider`,
  LOGIN: `${BASE}/rbac-user-server/oauth/token`,
  ROUTES: `${BASE_URL}/routes`,
  SYSTEM_MANAGER: `${BASE}/rbac-user-server`,
  FILE: `${BASE}/escat-file-management-provider`,
  TAG: `${BASE}/mousika-tagmanagement-provider`,
  DATAINTEGRATION: `${BASE}/escat-report-portal-provider`
}
