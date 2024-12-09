<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="queryParams.dictName" placeholder="请输入字典名称" clearable />
      </el-form-item>
      <el-form-item label="字典类型" prop="dictType">
        <el-input v-model="queryParams.dictType" placeholder="请输入字典类型" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="字典状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="typeList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="字典编号" align="center" prop="dictId" />
      <el-table-column label="字典名称" align="center" prop="dictName" />
      <el-table-column label="字典类型" align="center" prop="dictType" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="0"
            :inactive-value="1"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" width="260">
        <template #default="scope">
          <el-button type="primary" link @click="handleUpdate(scope.row)">修改</el-button>
          <el-button type="primary" link @click="handleData(scope.row)">数据</el-button>
          <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 添加或修改字典类型对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="dictTypeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">正常</el-radio>
            <el-radio :label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listType, getType, addType, updateType, delType, exportType } from '@/api/system/dict/type'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)
const typeList = ref([])
const total = ref(0)
const title = ref('')
const open = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  dictName: '',
  dictType: '',
  status: undefined
})

// 表单参数
const form = reactive({
  dictId: undefined,
  dictName: '',
  dictType: '',
  status: 0,
  remark: ''
})

// 表单校验规则
const rules = {
  dictName: [
    { required: true, message: '字典名称不能为空', trigger: 'blur' }
  ],
  dictType: [
    { required: true, message: '字典类型不能为空', trigger: 'blur' }
  ]
}

// 查询字典类型列表
const getList = async () => {
  loading.value = true
  try {
    const res = await listType(queryParams)
    typeList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 搜索按钮操作
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置按钮操作
const resetQuery = () => {
  queryParams.dictName = ''
  queryParams.dictType = ''
  queryParams.status = undefined
  handleQuery()
}

// 新增按钮操作
const handleAdd = () => {
  reset()
  open.value = true
  title.value = '添加字典类型'
}

// 修改按钮操作
const handleUpdate = async (row: any) => {
  reset()
  const dictId = row.dictId
  const res = await getType(dictId)
  Object.assign(form, res.data)
  open.value = true
  title.value = '修改字典类型'
}

// 提交按钮
const submitForm = async () => {
  try {
    if (form.dictId) {
      await updateType(form)
    } else {
      await addType(form)
    }
    open.value = false
    getList()
    ElMessage.success('操作成功')
  } catch (error) {
    console.error(error)
  }
}

// 删除按钮操作
const handleDelete = (row: any) => {
  ElMessageBox.confirm('是否确认删除字典编号为"' + row.dictId + '"的数据项?', '警告', {
    type: 'warning'
  }).then(async () => {
    await delType(row.dictId)
    getList()
    ElMessage.success('删除成功')
  })
}

// 导出按钮操作
const handleExport = () => {
  exportType(queryParams)
}

// 字典数据按钮操作
const handleData = (row: any) => {
  router.push({ path: '/system/dict/data', query: { dictType: row.dictType }})
}

// 状态修改
const handleStatusChange = async (row: any) => {
  try {
    const text = row.status === 0 ? '启用' : '停用'
    await updateType(row)
    ElMessage.success(text + '成功')
  } catch (error) {
    row.status = row.status === 0 ? 1 : 0
  }
}

// 表单重置
const reset = () => {
  form.dictId = undefined
  form.dictName = ''
  form.dictType = ''
  form.status = 0
  form.remark = ''
}

onMounted(() => {
  getList()
})
</script> 