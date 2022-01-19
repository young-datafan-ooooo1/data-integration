<template>
  <div id="dataPre_div" class="dataPre" :style="{height:divHeight+'px'}">
    <div class="log_detail" id="log1">
      {{logs}}
    </div>
  </div>
</template>

<script>


  export default {

    props:{
      logs:String,
    },
    data() {
      return {
        divHeight: document.body.clientHeight * 0.7
      }
    },
    computed: {

    },

    mounted() {
      this.initLog();
      // this.divHeight = document.body.clientHeight * 0.7;
      // window.onresize = () => {
      //   return (() => {
      //     this.divHeight = document.body.clientHeight * 0.7;
      //   })()
      // }
    },
    methods: {
      initLog() {
        console.info(this.logs);
        console.info($('#log1').html(this.logs));

        this.$nextTick(()=>{
          $('#log1').html(this.logs);
        })

      }
    }
  }
</script>

<style>
</style>
