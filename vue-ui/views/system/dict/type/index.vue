<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>字典类型</span>
          <div class="right-menu">
            <el-button type="primary" @click="handleAdd">新增字典</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="字典名称" prop="dictName">
          <el-input
            v-model="queryParams.dictName"
            placeholder="请输入字典名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input
            v-model="queryParams.dictType"
            placeholder="请输入字典类型"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="字典状态" clearable>
            <el-option
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="typeList"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="字典编号" align="center" prop="dictId" />
        <el-table-column label="字典名称" align="center" prop="dictName" />
        <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true" prop="dictType" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
              {{ scope.row.status === '0' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="260">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleData(scope.row)"
            >数据</el-button>
            <el-button
              type="primary"
              link
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="primary"
              link
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改字典类型对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="dictTypeForm" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listType, getType, addType, updateType, delType } from '@/api/system/dict/type'

const router = useRouter()

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 字典类型表格数据
const typeList = ref([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)
// 状态数据字典
const statusOptions = [
  { value: '0', label: '正常' },
  { value: '1', label: '停用' }
]

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  dictName: undefined,
  dictType: undefined,
  status: undefined
})

// 表单参数
const form = reactive({
  dictId: undefined,
  dictName: undefined,
  dictType: undefined,
  status: '0',
  remark: undefined
})

// 表单校验
const rules = {
  dictName: [
    { required: true, message: '字典名称不能为空', trigger: 'blur' }
  ],
  dictType: [
    { required: true, message: '字典类型不能为空', trigger: 'blur' }
  ]
}

/** 查询字典类型列表 */
function getList() {
  loading.value = true
  listType(queryParams).then(response => {
    typeList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.dictName = undefined
  queryParams.dictType = undefined
  queryParams.status = undefined
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '添加字典类型'
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  reset()
  const dictId = row.dictId
  getType(dictId).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = '修改字典类型'
  })
}

/** 提交按钮 */
function submitForm() {
  dictTypeForm.value.validate((valid: boolean) => {
    if (valid) {
      if (form.dictId) {
        updateType(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        addType(form).then(response => {
          ElMessage.success('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  const dictIds = [row.dictId]
  ElMessageBox.confirm('是否确认删除字典编号为"' + dictIds + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delType(dictIds)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

/** 查看字典数据按钮操作 */
function handleData(row: any) {
  router.push('/system/dict/data/' + row.dictId)
}

/** 表单重置 */
function reset() {
  form.dictId = undefined
  form.dictName = undefined
  form.dictType = undefined
  form.status = '0'
  form.remark = undefined
  dictTypeForm.value?.resetFields()
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 