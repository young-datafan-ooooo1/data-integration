import * as api from './api';
import Api from './api';
//import { Notification } from 'element-ui';
export default api;
export async function getRefreshToken(reqData) {
  let res = await Api.refreshToken(reqData);
  return res;
}
