<script lang="js">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "SelectUser",
  data() {
    let checkDuplicate = (rule, value, callback) => {
      if(this.form.id){
        return callback();
      }
      this.$axios.get("http://localhost:8090/user/findByNo?no=" + this.form.no)
          .then(res=>res.data).then(res => {
        if(res.code != 200){
          callback();
        }else{
          callback(new Error('账号已存在'));
        }
      })
    };

    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      sex: '',
      sexes: [
        {value: '1', label: '男'},
        {value: '0', label: '女'}
      ],
      centerDialogVisible: false,
      form: {
        no: '',
        name: '',
        password: '',
        age: '',
        sex: 0,
        phone: '',
        roleId: '2'
      },
      rules: {
        no: [
          { required: true, message: '账号不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
          { validator: checkDuplicate, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '姓名不能为空', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        age: [
          { required: true, message: '年龄不能为空', trigger: 'blur' },
          { pattern: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/, message: '请输入正确的年龄', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '电话不能为空', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ]
      }
    }
  },

  methods: {
    selectCurrentChange(val) {
      this.$emit('doSelectUser', val);
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val
      this.loadPost()
    },
    resetParam() {
      this.name = ''
      this.sex = ''
    },
    resetForm(){
      this.$refs.form.resetFields()
    },
    loadGet() {
      this.$axios.get('http://localhost:8090/user/list').then(res=>res.data).then(res => {
        console.log(res)
      })
    },
    loadPost() {
      this.$axios.post('http://localhost:8090/user/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param:{
          name: this.name,
          sex:this.sex,
          roleId: 2
        }
      }).then(res=>res.data).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.tableData = res.data
          this.total = res.total
        } else {
          alert('获取数据失败')
        }
      })
    },
    add(){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm()
      })
    },
    mod(row){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm()
        this.form = Object.assign({}, row)
        this.form.password = ''
      })
    },
    del(id){
      this.$axios.get('http://localhost:8090/user/delete?id=' + id).then(res=>res.data).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$message({
            message: '删除成功',
            type: 'success'
          })
          this.loadPost()
        } else {
          this.$message({
            message: '删除失败',
            type: 'error'
          })
        }
      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8090/user/saveOrUpdate', this.form).then(res=>res.data).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$message({
                message: '操作成功',
                type: 'success'
              })
              this.centerDialogVisible = false
              this.loadPost()
            } else {
              this.$message({
                message: '操作失败',
                type: 'error'
              })
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    }
  },

  beforeMount() {
    //this.loadGet()
    this.loadPost()
  }
})
</script>

<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-input
          v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width: 200px"
          @keyup.enter.native="loadPost">
      </el-input>
      <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 10px;width: 200px;">
        <el-option
            v-for="item in sexes"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 10px;" @click="loadPost">查询</el-button>
      <el-button type="info" @click="resetParam">重置</el-button>
    </div>
    <el-table
        :data="tableData"
        :header-cell-style="{background: '#f5f7fa', color: '#333', fontWeight: 'bold'}"
        border
        highlight-current-row
        @current-change="selectCurrentChange"
    >
      <el-table-column prop="id" label="ID" width="100">
      </el-table-column>
      <el-table-column prop="no" label="账号" width="150">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="150">
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="100">
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="100">
        <template slot-scope="scope">
          <el-tag
              :type ="scope.row.sex === 1 ? 'primary' : 'success'"
              disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleId" label="角色" width="100">
        <template slot-scope="scope">
          <el-tag
              :type ="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions>{{
              scope.row.roleId === 0 ? 'root管理员' : (scope.row.roleId === 1 ? '管理员' : '用户')}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话">
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-dialog
        title="新增用户"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="100px">
        <el-form-item label="账号" prop="no">
          <el-col :span="20">
            <el-input v-model="form.no"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-col :span="20">
            <el-input v-model="form.age"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别" >
          <el-radio-group v-model="form.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>