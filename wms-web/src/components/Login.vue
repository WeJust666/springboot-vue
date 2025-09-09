<script lang="js">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "LoginPage",
  data() {
    return {
      confirm_disabled: false,
      loginForm: {
        no: '',
        password: ''
      },
      rules: {
        no: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
    }
  },
  methods:{
    confirm() {
      this.confirm_disabled = true;
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$axios.post("http://localhost:8090/user/login", this.loginForm)
              .then(res => res.data).then(res => {
            if (res.code === 200) {
              sessionStorage.setItem("CurUser", JSON.stringify(res.data.user))
              console.log(res.data.menu)

              this.$store.commit("setMenu", res.data.menu);
              this.$router.replace('/Index');
            } else {
              this.confirm_disabled = false;
              alert('账号或密码错误');
              return false;
            }
          });
        } else {
          this.confirm_disabled = false;
          console.log('error');
          return false;
        }
      });
    }
  }
})
</script>

<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">用户登录</h1>
        <el-form :model="loginForm" label-width="60px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="width: 250px;" type="text"
                      v-model="loginForm.no" autocomplete="off" size="middle"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 250px;" type="password" show-password
                      v-model="loginForm.password" autocomplete="off" size="middle" @keyup.enter.native="confirm()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirm" :disabled="confirm_disabled" style="margin-left: 70px;">确 定</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .loginBody {
    position: absolute;
    width: 100%;
    height: 100%;
    background: #B3C0D1;
  }

  .loginDiv {
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -200px;
    margin-left: -250px;
    width: 500px;
    height: 300px;
    background: #fff;
    border-radius: 5%;
  }

  .login-content {
    margin-left: 80px;
    margin-top: 100px;
  }

  .login-title {
    width: 400px;
    height: 250px;
    position: absolute;
    top: 25px;
    left: 180px;
  }
</style>