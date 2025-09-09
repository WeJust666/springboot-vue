<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {},
  data(){
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
    }
  },
  //解决刷新界面时页面空白
  watch:{
    '$store.state.menu':{
      handler(val, old){
        if(!old && this.user.no){
          this.$store.commit('setMenu',val)
        }
      },
      immediate: true
    }
  }
}
</script>

<style>
#app {
  height: 100%;
}
</style>
