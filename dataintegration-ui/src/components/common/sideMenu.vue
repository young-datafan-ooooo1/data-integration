<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" unique-opened router
             collapse-transition>
      <template v-for="item in items" v-cloak>
        <template v-if="item.subset.length!==0">
          <el-submenu :index="item.url" :key="item.url">
            <template slot="title">
              <!--<img :src="item.icon"  style="width: 20px;height: 20px"/>-->
              <span slot="title">{{ item.name }}</span>
            </template>
            <el-menu-item v-for="(subItem,i) in item.subset" :key="i" :index="subItem.url">
              <!--<img :src="subItem.icon" style="width: 20px;height: 20px"/>-->
              <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.url" :key="item.url">
            <!--<img :src="item.icon" style="width: 20px;height: 20px"/>-->
            <span slot="title">{{ item.name }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        collapse: false,
        items: [{
          name: "elementUI之input",
          url: "",
          subset: [
            {name: "禁止输入", url: "/inputDisabled", subset: []},
            { name: "select联动", url: "/indexSelect",  subset: []
          }]
        }, {name: "手机下拉刷新", url: "/loadMoreUp", subset: []}]
      }
    },
    computed: {
      onRoutes() {
        //当前激活菜单的 index
        return this.$route.path.replace('/', '');
      }
    },
  }
</script>