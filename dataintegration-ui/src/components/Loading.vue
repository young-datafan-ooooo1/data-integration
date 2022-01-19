<template>
  <div v-loading.fullscreen.lock="loading">

  </div>
</template>

<script>
  export default{
    data(){
      return{
        loading:true,
      }
    },
    mounted() {
      this.init();
    },
    methods:{
      init(){
        let url = window.location.href;
        // console.info(url.indexOf('?'));
        // console.info("字符串截取====",url.substr(url.indexOf('=')+1,url.length))
        // let token = url.substr(url.indexOf('=')+1,url.length);
        // console.info("token",token);
        // this.$store.dispatch('setToken',token);
        // // todo 获取菜单
        // this.$router.push('/genneralView');
      }
    }
  }
</script>

<style>
</style>
