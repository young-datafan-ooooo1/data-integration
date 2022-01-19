const tagsView = {
  state: {
    visitedViews: [],
    cachedViews: []
  },
  mutations: {
    ADD_VISITED_VIEWS: (state, view) => {
      let currentViews = state.visitedViews;
      let flag = false;
      currentViews.forEach((item, index) => {
        if (item.path === view.path) {
          flag = true;
        }
      })
      if (!flag) {
        if (state.visitedViews.some(v => v.path === view.fullPath)) return
        state.visitedViews.push({
          name: view.name,
          path: view.fullPath,
          title: view.meta.title || 'no-name',
          title1: view.meta.title1,
        })
        if (!view.meta.noCache) {
          state.cachedViews.push(view.name)
        }
      }
    },
    DEL_VISITED_VIEWS: (state, view) => {
      for (const [i, v] of state.visitedViews.entries()) {
        if (v.path === view.path) {
          state.visitedViews.splice(i, 1)
          break
        }
      }
      for (const i of state.cachedViews) {
        if (i === view.name) {
          const index = state.cachedViews.indexOf(i)
          state.cachedViews.splice(index, 1)
          break
        }
      }
    },
    DEL_OTHERS_VIEWS: (state, view) => {
      for (const [i, v] of state.visitedViews.entries()) {
        if (v.path === view.path) {
          state.visitedViews = state.visitedViews.slice(i, i + 1)
          break
        }
      }
      for (const i of state.cachedViews) {
        if (i === view.name) {
          const index = state.cachedViews.indexOf(i)
          state.cachedViews = state.cachedViews.slice(index, i + 1)
          break
        }
      }
    },
    DEL_ALL_VIEWS: (state) => {
      state.visitedViews = []
      state.cachedViews = []
    },
    // 修改路由名称
    UPDATE_VIEWS: (state, view) => {
      for (let i = 0; i < state.visitedViews.length; i++) {
        if (state.visitedViews[i].path === view.path) {
          state.visitedViews[i].title1 = view.meta.title;
          // state.visitedViews[i].title1 = view.meta.title1;
        }
      }
    }
  },
  actions: {
    addVisitedViews({
      commit
    }, view) {
      // 判断路由是否已经存在
      //判断是否是弹窗路由 如果是不加到tags标签页
      commit('ADD_VISITED_VIEWS', view)
    },
    delVisitedViews({
      commit,
      state
    }, view) {
      return new Promise((resolve) => {
        commit('DEL_VISITED_VIEWS', view)
        resolve([...state.visitedViews])
      })
    },
    delOthersViews({
      commit,
      state
    }, view) {
      return new Promise((resolve) => {
        commit('DEL_OTHERS_VIEWS', view)
        resolve([...state.visitedViews])
      })
    },
    delAllViews({
      commit,
      state
    }) {
      return new Promise((resolve) => {
        commit('DEL_ALL_VIEWS')
        resolve([...state.visitedViews])
      })
    },
    // 修改路由信息
    updateView({
      commit
    }, view) {
      commit('UPDATE_VIEWS', view)
    }
  }
}

export default tagsView
