<template>
  <div class="tags-view-container" id="tagsView">
    <nx-scroll-pane class='tags-view-wrapper' :style="{ '--tagsViewBgColor': themeColor }" ref='scrollPane'>
      <div class="tag_home" @click="backHome">
        <i class="iconfont icon-shouye"></i>
      </div>
      <router-link ref='tag' class="tags-view-item" :class="isActive(tag)?'active':''" v-for="tag in visitedViews" :to="tag.path"
                   :key="tag.path" @contextmenu.prevent.native="openMenu(tag,$event)">
        <i class='el-icon-s-home' v-show="!isShow(tag)"></i>{{tag.title1}}
        <i class='el-icon-close' @click.prevent.stop='closeSelectedTag(tag)' v-show="isShow(tag)"></i>
      </router-link>
      <a class="tags-view-item" @click="addProject" style="user-select: none;-moz-user-select: none;-ms-user-select: none;-webkit-user-select: none">新增项目<i class='el-icon-plus'></i></a>
    </nx-scroll-pane>
  </div>
</template>

<script>
  import nxScrollPane from './scrollPane'
  import store from '../../vuex/store.js';
  import util from '../../common/utils.js'
  import {createPro} from "../../common/common";

  export default {
    name: 'tagsView',
    props: ['collapsed'],
    components: {
      nxScrollPane
    },
    data() {
      return {
        visible: false,
        top: 0,
        left: 0,
        selectedTag: {},
        themeColor: localStorage.getItem('themeColor')
      }
    },
    computed: {
      visitedViews() {
        return this.$store.state.tagsView.visitedViews
      }
    },
    watch: {
      '$route'() {
        this.addViewTags()
        this.moveToCurrentTag()
      },
      visitedViews() {},
      visible(value) {
        if (value) {
          document.body.addEventListener('click', this.closeMenu)
        } else {
          document.body.removeEventListener('click', this.closeMenu)
        }
      },
      collapsed() {
        if (!this.collapsed) {
          document.getElementById('tagsView').setAttribute('style',
            'position: fixed;z-index: 1;width:calc(100% - 250px) !important;top:70px;left: 250px;')
        } else {
          document.getElementById('tagsView').setAttribute('style',
            'position: fixed;z-index: 1;width:calc(100% - 60px) !important;top:70px;left: 60px;')
        }
      }

    },
    mounted() {
      this.addViewTags()
    },
    methods: {

      /**
       * 新建项目
       */
      addProject() {
        createPro();
        // util.$emit('addProject');
      },

      isShow(tag) {
        return tag.path !=='/genneralView'
      },
      generateRoute() {
        if (this.$route.path === '/genneralView') {
          return this.$route;
        } else {
          if (this.$route.meta) {
            let data = {
              path: this.$route.path,
              fullPath: this.$route.fullPath,
              meta: {
                title: this.$route.meta.title,
                title1: this.$store.getters.getRouteTitle,
              }
            }
            return data;
          }

        }
        return false
      },
      isActive(route) {
        return route.path === this.$route.path
      },

      addViewTags() {
        const route = this.generateRoute()
        if(route.path === '/genneralView'){

        }else{
          this.$store.dispatch('addVisitedViews', route)
        }




      },
      backHome(){
        this.$router.push('/genneralView');
      },
      moveToCurrentTag() {
        this.$nextTick(() => {
          const tags = this.$refs.tag
          for (const tag of tags) {
            if (tag.to === this.$route.path) {
              this.$refs.scrollPane.moveToTarget(tag.$el)
              break
            }
          }
        })
      },
      closeSelectedTag(view) {
        this.$store.dispatch('delVisitedViews', view).then((views) => {
          if (this.isActive(view)) {
            const latestView = views.slice(-1)[0]
            if (latestView) {
              this.$router.push(latestView.path)
            } else {
              this.$router.push('/genneralView')
            }
          }
        })
      },
      closeOthersTags() {
        this.$router.push(this.selectedTag.path)
        this.$store.dispatch('delOthersViews', this.selectedTag).then(() => {
          this.moveToCurrentTag()
        })
      },
      closeAllTags() {
        this.$store.dispatch('delAllViews')
        this.$router.push('/genneralView')
      },
      openMenu(tag, e) {
        this.visible = true
        this.selectedTag = tag
        this.left = e.clientX
        this.top = e.clientY
      },
      closeMenu() {
        this.visible = false
      }
    }
  }
</script>

<style scoped>
  a {
    text-decoration: none
  }
</style>
