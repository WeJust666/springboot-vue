<script lang="js">
import {defineComponent} from 'vue'
import SelectUser from "@/components/user/SelectUser.vue";

export default defineComponent({
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      tableData: [],
      storageData: [],
      goodstypeData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      storage: '',
      goodstype: '',
      centerDialogVisible: false,
      inDialogVisible: false,
      selectUserDialogVisible: false,
      currentRow: {},
      tempUser: {},
      form: {
        no: '',
        name: '',
        storage: '',
        goodstype: '',
        count: '',
        remark: ''
      },
      formIn: {
        goods: '',
        goodsname: '',
        count: '',
        username:'',
        userid:'',
        adminId:'',
        remark:'',
        action:'1'
      },
      rules: {
        name: [
          { required: true, message: '物品名不能为空', trigger: 'blur' },
        ],
        storage: [
          { required: true, message: '仓库不能为空', trigger: 'blur' },
        ],
        goodstype: [
          { required: true, message: '分类不能为空', trigger: 'blur' },
        ],
        count: [
          { required: true, message: '数量不能为空', trigger: 'blur' },
          { pattern: /^[0-9]+$/, message: '数量应为正整数', trigger: 'blur' }
        ]
      },
      rulesIn:{
        name: [
          { required: true, message: '物品名不能为空', trigger: 'blur' },
        ],
        username: [
          { required: true, message: '申请人不能为空', trigger: 'blur' }
        ],
        count: [
          { required: true, message: '数量不能为空', trigger: 'blur' },
          { pattern: /^[0-9]+$/, message: '数量应为正整数', trigger: 'blur' }
        ]
      }
    }
  },

  methods: {
    selectCurrentChange(val) {
      this.currentRow = val;
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
      this.storage = ''
      this.goodstype = ''
    },
    resetForm(){
      this.$refs.form.resetFields()
    },
    resetInForm(){
      this.$refs.formIn.resetFields()
    },
    loadPost() {
      this.$axios.post('http://localhost:8090/goods/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param:{
          name: this.name,
          storage: this.storage,
          goodstype: this.goodstype
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
    loadStorage() {
      this.$axios.get('http://localhost:8090/storage/list').then(res=>res.data).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.storageData = res.data
        } else {
          alert('获取仓库数据失败')
        }
      })
    },
    loadGoodstype() {
      this.$axios.get('http://localhost:8090/goodstype/list').then(res=>res.data).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.goodstypeData = res.data
        } else {
          alert('获取分类数据失败')
        }
      })
    },
    add(){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm()
      })
    },
    inGoods(){
      if(!this.currentRow.id){
        alert('请选择一条数据');
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(()=>{
        this.resetInForm()
      })
      this.formIn.goodsname = this.currentRow.name;
      this.formIn.goods = this.currentRow.id;
      this.formIn.adminId = this.user.id;
      this.formIn.action = '1'
    },
    outGoods(){
      if(!this.currentRow.id){
        alert('请选择一条数据');
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(()=>{
        this.resetInForm()
      })
      this.formIn.goodsname = this.currentRow.name;
      this.formIn.goods = this.currentRow.id;
      this.formIn.adminId = this.user.id;
      this.formIn.action = '2'
    },
    selectUser(){
      this.selectUserDialogVisible = true
    },
    confirmUser(){
      this.formIn.username = this.tempUser.name;
      this.formIn.userid = this.tempUser.id;
      this.selectUserDialogVisible = false;
    },
    doSelectUser(val){
      this.tempUser = val;
    },
    mod(row){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm()
        this.form = Object.assign({}, row)
      })
    },
    del(id){
      this.$axios.get('http://localhost:8090/goods/delete?id=' + id).then(res=>res.data).then(res => {
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
          this.$axios.post('http://localhost:8090/goods/saveOrUpdate', this.form).then(res=>res.data).then(res => {
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
    },
    doInGoods(){
      this.$refs.formIn.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8090/record/save', this.formIn).then(res=>res.data).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$message({
                message: '操作成功',
                type: 'success'
              })
              this.inDialogVisible = false
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
    },
    formatStorage(row){
      let temp = this.storageData.find((item)=>{
        return item.id === row.storage
      })
      return temp && temp.name
    },
    formatGoodstype(row){
      let temp = this.goodstypeData.find((item)=>{
        return item.id === row.goodstype
      })
      return temp && temp.name
    }
  },

  beforeMount() {
    this.loadPost()
    this.loadStorage()
    this.loadGoodstype()
  }
})
</script>

<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-input
          v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px"
          @keyup.enter.native="loadPost">
      </el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="width: 200px; margin-left: 10px;">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodstype" placeholder="请选择分类" style="width: 200px; margin-left: 10px;">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 10px;" @click="loadPost">查询</el-button>
      <el-button type="info" @click="resetParam">重置</el-button>
      <el-button type="success" style="margin-left: 10px;" @click="add" v-if="user.roleId!=2">新增</el-button>
      <el-button type="warning" style="margin-left: 10px;" @click="inGoods" v-if="user.roleId!=2">入库</el-button>
      <el-button type="warning" style="margin-left: 10px;" @click="outGoods" v-if="user.roleId!=2">出库</el-button>
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
      <el-table-column prop="name" label="物品名" width="200">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="200" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="分类" width="200" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="100">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>

      <el-table-column prop="operate" label="操作" v-if="user.roleId!=2">
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
        title="新增仓库"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="100px">
        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" placeholder="请选择仓库" style="width: 200px;">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="分类" prop="goodstype">
          <el-col :span="20">
            <el-select v-model="form.goodstype" placeholder="请选择分类" style="width: 200px;">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
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

    <el-dialog
        title="出入库"
        :visible.sync="inDialogVisible"
        width="30%"
        center>

      <el-dialog
          width="55%"
          title="选择用户" center
          :visible.sync="selectUserDialogVisible"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="selectUserDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
      </span>
      </el-dialog>

      <el-form ref="formIn" :rules="rulesIn" :model="formIn" label-width="100px">
        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="formIn.goodsname"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人" prop="username">
          <el-col :span="20">
            <el-input v-model="formIn.username" readonly @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="formIn.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="formIn.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="inDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doInGoods">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>