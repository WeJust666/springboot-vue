<script lang="js">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "RecordManage",
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
      form: {
        no: '',
        name: '',
        storage: '',
        goodstype: '',
        count: '',
        remark: '',
        createtime: '',
      },
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
      this.storage = ''
      this.goodstype = ''
    },
    resetForm(){
      this.$refs.form.resetFields()
    },
    loadPost() {
      this.$axios.post('http://localhost:8090/record/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param:{
          name: this.name,
          storage: this.storage,
          goodstype: this.goodstype,
          roleId:this.user.roleId,
          userId:this.user.id
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
    </div>
    <el-table
        :data="tableData"
        :header-cell-style="{background: '#f5f7fa', color: '#333', fontWeight: 'bold'}"
        border
    >
      <el-table-column prop="id" label="ID" width="100">
      </el-table-column>
      <el-table-column prop="goodsname" label="物品名" width="200">
      </el-table-column>
      <el-table-column prop="storagename" label="仓库" width="200">
      </el-table-column>
      <el-table-column prop="goodstypename" label="分类" width="200">
      </el-table-column>
      <el-table-column prop="adminname" label="操作人" width="200">
      </el-table-column>
      <el-table-column prop="username" label="申请人" width="200">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="100">
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="200">
      </el-table-column>
      <el-table-column prop="createtime" label="操作时间" width="200">
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
  </div>
</template>

<style scoped>

</style>