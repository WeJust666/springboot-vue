<script lang="js">
import {defineComponent} from "vue";

export default defineComponent({
  name: "IndexPage",
  components: {
    Aside: () => import('./Aside.vue'),
    Header: () => import('./Header.vue')},
    data() {
      return {
        isCollapse: false,
        aside_width: '200px',
        icon: 'el-icon-s-fold'
      }
    },
    methods: {
      doCollapse() {
        this.isCollapse = !this.isCollapse;
        if(this.isCollapse) {
          this.aside_width = '64px'
          this.icon = 'el-icon-s-unfold'
        } else {
          this.aside_width = '200px'
          this.icon = 'el-icon-s-fold'
        }
      }
    }
})
</script>

<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside :width="aside_width" style="background-color: rgb(238, 241, 246);margin-left: -1px;">
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="height: 100%">
      <el-header style="text-align: right; font-size: 20px">
        <Header @doCollapse="doCollapse" :icon="icon"></Header>
      </el-header>

      <el-main style="height: 100%">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
  .el-header {
    background-color: rgba(169, 169, 169, 0.3);
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }
</style>