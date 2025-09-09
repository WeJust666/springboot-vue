<script lang="js">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "HeaderPart",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  props: {
    icon: String
  },
  methods: {
    toUser() {
      console.log("toUser");
      this.$router.push("/Home");
    },
    logout() {
      console.log("logout");
      this.$confirm('是否确认退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        this.$router.push("/");
        sessionStorage.removeItem("CurUser");
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出登录'
        });
      });
    },
    collapse() {
      this.$emit('doCollapse')
    }
  },
  created() {
    this.$router.push("/Home");
  }
})
</script>

<template>
  <div style="display: flex;line-height: 60px;">
    <div style="cursor: pointer;">
      <i :class ="icon" style="line-height: 60px;font-size: 25px;" @click="collapse"></i>
    </div>
    <div style="flex: 1;text-align: center;font-size: 25px;">
      <span>欢迎来到仓库管理系统</span>
    </div>
    <span>{{user.name}}</span>
    <el-dropdown>
      <div style="cursor: pointer;">
        <i class="el-icon-arrow-down" style="margin-left: 10px;"></i>
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<style scoped>

</style>