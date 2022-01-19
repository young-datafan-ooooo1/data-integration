// import SockJS from 'sockjs-client';
var Stomp = require('@stomp/stompjs');
import store from '../vuex/store';
export default {
  ws: {},
  setWs: function(newWs) {
    this.ws = newWs;
    store.dispatch("setWebSocket",new Date().getTime());
  }
}
