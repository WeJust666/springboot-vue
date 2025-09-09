<script lang="js">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "GoodstypeManage",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      centerDialogVisible: false,
      form: {
        no: '',
        name: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: '物品类不能为空', trigger: 'blur' },
        ]
      }
    }
  },

  methods: {
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
    },
    resetForm(){
      this.$refs.form.resetFields()
    },
    loadPost() {
      this.$axios.post('http://localhost:8090/goodstype/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param:{
          name: this.name
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
      })
    },
    del(id){
      this.$axios.get('http://localhost:8090/goodstype/delete?id=' + id).then(res=>res.data).then(res => {
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
          this.$axios.post('http://localhost:8090/goodstype/saveOrUpdate', this.form).then(res=>res.data).then(res => {
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
    this.loadPost()
  }
})
</script>

<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-input
          v-model="name" placeholder="请输入物品类" suffix-icon="el-icon-search" style="width: 200px"
          @keyup.enter.native="loadPost">
      </el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="loadPost">查询</el-button>
      <el-button type="info" @click="resetParam">重置</el-button>
      <el-button type="success" style="margin-left: 10px;" @click="add">新增</el-button>
    </div>
    <el-table
        :data="tableData"
        :header-cell-style="{background: '#f5f7fa', color: '#333', fontWeight: 'bold'}"
        border
    >
      <el-table-column prop="id" label="ID" width="100">
      </el-table-column>
      <el-table-column prop="name" label="物品类" width="200">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>

      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button size="medium" type="success" @click="mod(scope.row)" >编辑</el-button>
          <el-popconfirm
              title="确认删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 10px;"
          >
            <el-button slot="reference" size="medium" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
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
        title="新增物品类"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="100px">
        <el-form-item label="物品类" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
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